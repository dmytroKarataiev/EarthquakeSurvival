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
 */

package com.adkdevelopment.earthquakesurvival;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adkdevelopment.earthquakesurvival.objects.earthquake.Feature;
import com.adkdevelopment.earthquakesurvival.provider.earthquake.EarthquakeColumns;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

    private GoogleMap mGoogleMap;
    private CameraPosition mCameraPosition;
    private Cursor mCursor;
    private static final int CURSOR_LOADER_ID = 20;

    @BindView(R.id.map) MapView mMapView;
    @BindDrawable(R.drawable.marker) Drawable mOval;
    private Unbinder mUnbinder;

    // data to start detail activity on info window click
    private HashMap<String, Intent> mMarkers = new HashMap<>();

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

        if (mCameraPosition != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        }
        addMarkers();
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
                new String[] {String.valueOf(Utilities.getMagnitudePrefs(getContext()))},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        addMarkers();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }

    /**
     * Adds markers to the map from the database
     */
    private void addMarkers() {
        if (mGoogleMap != null && mCursor != null) {
            mGoogleMap.clear();
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

                mMarkers.put(mGoogleMap.addMarker(markerOptions).getId(), intent);
            }

            mGoogleMap.setOnInfoWindowClickListener(l -> startActivity(mMarkers.get(l.getId())));
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
            
        }
    }
}
