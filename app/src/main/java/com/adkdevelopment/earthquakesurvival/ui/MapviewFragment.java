/*
 * MIT License
 *
 * Copyright (c) 2016. Dmytro Karataiev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.adkdevelopment.earthquakesurvival.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adkdevelopment.earthquakesurvival.R;
import com.adkdevelopment.earthquakesurvival.data.objects.earthquake.Feature;
import com.adkdevelopment.earthquakesurvival.data.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.utils.LocationUtils;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.adkdevelopment.earthquakesurvival.R.drawable.marker;

/**
 * Fragment with a map which monitors location changes and updates the view
 */
public class MapviewFragment extends Fragment implements OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = MapviewFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String CAMERA_POSITION = "camera";
    public static final int LOCATION_PERMISSION = 12223;

    private GoogleMap mGoogleMap;
    private CameraPosition mCameraPosition;
    private Cursor mCursor;
    private static final int CURSOR_LOADER_ID = 20;

    @BindView(R.id.map)
    MapView mMapView;
    @BindDrawable(marker)
    Drawable mOval;
    private Unbinder mUnbinder;

    // data to start detail activity on info window click
    private HashMap<String, Intent> mMarkersIntents = new HashMap<>();
    private List<Marker> mMarkers;
    private KmlLayer mFaults;
    private Subscription mSubscription;

    public MapviewFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MapviewFragment newInstance(int sectionNumber) {
        MapviewFragment fragment = new MapviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        mUnbinder = ButterKnife.bind(this, rootView);

        mMarkers = new ArrayList<>();

        if (Utilities.checkPlayServices(getActivity())) {
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        }

        if (savedInstanceState != null) {
            mCameraPosition = savedInstanceState.getParcelable(CAMERA_POSITION);
        } else {
            mCameraPosition = CameraPosition.builder()
                    .target(LocationUtils.getLocation(getContext()))
                    .zoom(LocationUtils.CAMERA_DEFAULT_ZOOM)
                    .build();
        }

        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        addMyLocation();
        uiSettings.setMyLocationButtonEnabled(true);

        if (mCameraPosition != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        }
        addMarkersObservable();
        showFaultlines();
    }

    /**
     * Adds my location button to the MapView.
     */
    private void addMyLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION);
        } else {
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addMyLocation();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mMapView.onDestroy();
        mUnbinder.unbind();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mGoogleMap != null) {
            CameraPosition save = mGoogleMap.getCameraPosition();
            outState.putParcelable(CAMERA_POSITION, save);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                EarthquakeColumns.CONTENT_URI,
                null,
                EarthquakeColumns.MAG + " >= ?",
                new String[]{String.valueOf(Utilities.getMagnitudePrefs(getContext()))},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        addMarkersObservable();
    }

    /**
     * Loads markers from the IO thread.
     */
    private void addMarkersObservable() {
        if (mSubscription == null || mSubscription.isUnsubscribed()) {
            mSubscription = Observable.fromCallable(this::getMarkers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<Pair<List<MarkerOptions>, List<Intent>>>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted: ");
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Pair<List<MarkerOptions>, List<Intent>> pair) {
                            addMarkers(pair);
                        }
                    });
        }
    }

    /**
     * Adds markers to the GoogleMap if it's available.
     * @param pair a Pair of Lists with MarkerOptions and Intents fot InfoWindows.
     */
    public void addMarkers(Pair<List<MarkerOptions>, List<Intent>> pair) {
        if (mGoogleMap != null) {
            //noinspection Convert2streamapi
            for (Marker each : mMarkers) {
                each.remove();
            }
            mMarkers.clear();

            List<MarkerOptions> first = pair.first;
            for (int i = 0; i < first.size(); i++) {
                MarkerOptions each = first.get(i);
                Marker marker = mGoogleMap.addMarker(each);
                mMarkers.add(marker);
                mMarkersIntents.put(marker.getId(), pair.second.get(i));
                mGoogleMap.setOnInfoWindowClickListener(l -> startActivity(mMarkersIntents.get(l.getId())));
            }
        }
    }

    /**
     * Creates MarkerOptions and Intents for InfoWindows.
     * @return a Pair of Lists with MarkerOptions and corresponding Intents.
     */
    @NonNull
    private Pair<List<MarkerOptions>, List<Intent>> getMarkers() {
        List<MarkerOptions> markers = new ArrayList<>();
        List<Intent> intents = new ArrayList<>();
        if (mCursor != null) {
            mCursor.moveToFirst();

            int lat = mCursor.getColumnIndex(EarthquakeColumns.LATITUDE);
            int lng = mCursor.getColumnIndex(EarthquakeColumns.LONGITUDE);
            int desc = mCursor.getColumnIndex(EarthquakeColumns.PLACE);
            int magn = mCursor.getColumnIndex(EarthquakeColumns.MAG);
            int date = mCursor.getColumnIndex(EarthquakeColumns.TIME);
            int link = mCursor.getColumnIndex(EarthquakeColumns.URL);
            int depth = mCursor.getColumnIndex(EarthquakeColumns.DEPTH);

            while (mCursor.moveToNext()) {

                Double latitude = mCursor.getDouble(lat);
                Double longitude = mCursor.getDouble(lng);
                String description = mCursor.getString(desc);
                description = Utilities.formatEarthquakePlace(description);

                Long dateMillis = mCursor.getLong(date);
                String linkDetails = mCursor.getString(link);

                Double magnitude = mCursor.getDouble(magn);
                double depthEarthquake = mCursor.getDouble(depth) / 1.6;

                LatLng latLng = new LatLng(latitude, longitude);

                String distance = getContext().getString(R.string.earthquake_distance,
                        LocationUtils.getDistance(latLng, LocationUtils.getLocation(getContext())));

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.earthquake_magnitude, magnitude) + ", "
                                + Utilities.getRelativeDate(dateMillis))
                        .snippet(description)
                        .icon(BitmapDescriptorFactory.fromBitmap(Utilities.getEarthquakeMarker(getContext(), magnitude)));

                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(Feature.MAGNITUDE, magnitude);
                intent.putExtra(Feature.PLACE, description);
                intent.putExtra(Feature.DATE, Utilities.getRelativeDate(dateMillis));
                intent.putExtra(Feature.LINK, linkDetails);
                intent.putExtra(Feature.LATLNG, latLng);
                intent.putExtra(Feature.DISTANCE, distance);
                intent.putExtra(Feature.DEPTH, depthEarthquake);

                markers.add(markerOptions);
                intents.add(intent);
            }

        }
        return new Pair<>(markers, intents);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }

    /**
     * Shows or hides fault lines on the map.
     */
    private void showFaultlines() {
        // if fault lines are enabled - show them on the map
        if (Utilities.getFaultlinesPrefs(getContext())) {
            try {
                mFaults = new KmlLayer(mGoogleMap, R.raw.tectonics, getContext());
                mFaults.addLayerToMap();
            } catch (IOException | XmlPullParserException e) {
                Log.e(TAG, "e:" + e);
            }
        } else if (mGoogleMap != null && mFaults != null) {
            mFaults.removeLayerFromMap();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sharedprefs_key_magnitude))) {
            getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        } else if (key.equals(getString(R.string.sharedprefs_key_latitude)) ||
                key.equals(getString(R.string.sharedprefs_key_longitude))) {

            if (mGoogleMap != null) {
                mCameraPosition = CameraPosition.builder()
                        .target(LocationUtils.getLocation(getContext()))
                        .zoom(LocationUtils.CAMERA_DEFAULT_ZOOM)
                        .build();

                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
            }
        } else if (key.equals(getString(R.string.sharedprefs_key_faultlines))) {
            showFaultlines();
        }
    }
}
