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

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adkdevelopment.earthquakesurvival.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.utils.LocationUtils;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MapviewFragment extends Fragment implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {
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

    @Bind(R.id.map) MapView mMapView;
    @BindDrawable(R.drawable.marker) Drawable mOval;

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

        ButterKnife.bind(this, rootView);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

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
        ButterKnife.unbind(this);
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CameraPosition save = mGoogleMap.getCameraPosition();
        outState.putParcelable(CAMERA_POSITION, save);
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
     * Creates Bitmap for a Map marker depending on the magnitude of an earthquake
     * @param magnitude from 0 to 10 scale earthquake intensity
     * @return colorful oval of size depending on magnitude
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Bitmap getEarthquakeMarker(Double magnitude) {

        if (magnitude < 1) {
            magnitude = 1.0;
        }

        GradientDrawable oval;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            oval = (GradientDrawable) getResources().getDrawable(R.drawable.marker, getActivity().getTheme());
        } else {
            oval = (GradientDrawable) getResources().getDrawable(R.drawable.marker);
        }

        if (magnitude > 3 && magnitude < 5) {
            oval.setColor(Color.BLUE);
        } else if (magnitude > 5 && magnitude < 7) {
            oval.setColor(Color.YELLOW);
        } else if (magnitude > 7) {
            oval.setColor(Color.RED);
        } else {
            oval.setColor(Color.GREEN);
        }

        int diameter = (int) (oval.getIntrinsicWidth() * magnitude);

        Canvas canvas = new Canvas();
        Bitmap icon = Bitmap.createBitmap(
                diameter,
                diameter,
                Bitmap.Config.ARGB_8888);

        canvas.setBitmap(icon);
        oval.setBounds(0, 0, diameter, diameter);
        oval.draw(canvas);

        return icon;
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

            while (mCursor.moveToNext()) {

                Double latitude = mCursor.getDouble(lat);
                Double longitude = mCursor.getDouble(lng);
                String description = mCursor.getString(desc);
                Long dateMillis = mCursor.getLong(date);
                Date time = new Date(dateMillis);

                Double magnitude = mCursor.getDouble(magn);

                LatLng latLng = new LatLng(latitude, longitude);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(DateUtils.getRelativeTimeSpanString(time.getTime()).toString() + " "
                                + description + ", "
                                + getString(R.string.earthquake_magnitude, magnitude))
                        .icon(BitmapDescriptorFactory.fromBitmap(getEarthquakeMarker(magnitude)));

                mGoogleMap.addMarker(markerOptions);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sharedprefs_key_magnitude))) {
            getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        }
    }
}
