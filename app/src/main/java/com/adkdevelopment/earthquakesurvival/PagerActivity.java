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

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adkdevelopment.earthquakesurvival.adapters.PagerAdapter;
import com.adkdevelopment.earthquakesurvival.geofence.GeofenceService;
import com.adkdevelopment.earthquakesurvival.ui.ZoomOutPageTransformer;
import com.adkdevelopment.earthquakesurvival.utils.LocationUtils;
import com.adkdevelopment.earthquakesurvival.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.settings.SettingsActivity;
import com.adkdevelopment.earthquakesurvival.syncadapter.SyncAdapter;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

public class PagerActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status>, SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private PagerAdapter mPagerAdapter;

    private static final String TAG = PagerActivity.class.getSimpleName();

    // Locations, ApiClient
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final int REQUEST_FINE_LOCATION = 0;
    private Location mLocation;

    // Geofence variables
    private List<Geofence> mGeofenceList;
    private ContentObserver mObserver;

    @Bind(R.id.sliding_tabs) TabLayout mTab;
    @Bind(R.id.container) ViewPager mViewPager;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @BindColor(R.color.tab_item_selected) int mColorSelected;
    @BindColor(R.color.tab_item_unselected) int mColorUnselected;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_activity);

        mGeofenceList = new ArrayList<>();

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mTab.setupWithViewPager(mViewPager);
        setTabImages();

        // start SyncAdapter
        SyncAdapter.initializeSyncAdapter(this);

        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);

        // Set up GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Observe SyncAdapter work and update Geofences
        mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean selfChange) {
                populateGeofenceList();
                observeGeofences();
            }
        };
        getContentResolver().registerContentObserver(EarthquakeColumns.CONTENT_URI, false, mObserver);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_refresh:
                SyncAdapter.syncImmediately(getBaseContext());
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to set tab images and highlights on tab switching
     * todo: add color tint on buttons
     */
    private void setTabImages() {
        if (mTab != null) {

            int[] iconSet = {
                    R.drawable.error_grey,
                    R.drawable.map_grey,
                    R.drawable.newspaper_grey,
                    R.drawable.lightbulb_grey,
                    R.drawable.error_blue,
                    R.drawable.map_blue,
                    R.drawable.newspaper_blue,
                    R.drawable.lightbulb_blue
            };

            // Set custom layouts for each Tab
            for (int i = 0, n = mTab.getTabCount(); i < n; i++) {
                TabLayout.Tab tabLayout = mTab.getTabAt(i);

                if (tabLayout != null) {
                    tabLayout.setCustomView(R.layout.pager_tab_layout);
                    View customView = tabLayout.getCustomView();

                    if (customView != null) {
                        ImageView imageView = ButterKnife.findById(customView, R.id.tab_item_image);
                        TextView textView = ButterKnife.findById(customView, R.id.tab_item_text);
                        textView.setText(tabLayout.getText());

                        if (i == 0) {
                            textView.setTextColor(mColorSelected);
                            imageView.setImageResource(iconSet[i + iconSet.length / 2]);
                        } else {
                            textView.setTextColor(mColorUnselected);
                            imageView.setImageResource(iconSet[i]);
                        }
                    }
                }
            }

            // TODO: 4/6/16 refactor, probably add AddPageChangeListener
            // Highlight image and text on selection
            mTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    setTitle();
                    View customView = tab.getCustomView();
                    if (customView != null) {
                        ImageView imageView = ButterKnife.findById(customView, R.id.tab_item_image);
                        imageView.setImageResource(iconSet[tab.getPosition() + iconSet.length / 2]);

                        TextView textView = ButterKnife.findById(customView, R.id.tab_item_text);
                        textView.setTextColor(mColorSelected);
                    }
                    mViewPager.setCurrentItem(tab.getPosition());

                    Fragment fragment = mPagerAdapter.getRegisteredFragment(tab.getPosition());
                    if (fragment instanceof SurvivalFragment) {
                        ((SurvivalFragment) fragment).animateViewsIn();
                    } else if (fragment instanceof NewsFragment) {
                        ((NewsFragment) fragment).animateViewsIn();
                    } else if (fragment instanceof RecentFragment) {
                        ((RecentFragment) fragment).animateViewsIn();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View customView = tab.getCustomView();
                    if (customView != null) {
                        ImageView imageView = ButterKnife.findById(customView, R.id.tab_item_image);
                        imageView.setImageResource(iconSet[tab.getPosition()]);

                        TextView textView = ButterKnife.findById(customView, R.id.tab_item_text);
                        textView.setTextColor(mColorUnselected);
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    Fragment fragment = mPagerAdapter.getRegisteredFragment(tab.getPosition());

                    if (fragment instanceof RecentFragment) {
                        ((RecentFragment) fragment).scrollToTop();
                    } else if (fragment instanceof NewsFragment) {
                        ((NewsFragment) fragment).scrollToTop();
                    }
                }
            });

        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(DateUtils.HOUR_IN_MILLIS);
        mLocationRequest.setFastestInterval(DateUtils.MINUTE_IN_MILLIS);

        String perm = Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            loadPermissions(perm, REQUEST_FINE_LOCATION);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, location.toString());
        mLocation = location;

        // save location in shared preferences
        LocationUtils.setLocation(this, location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    /**
     * Method to load permissions for Android 6+ at runtime.
     *
     * @param perm        The requested permission.
     * @param requestCode Application specific request code to match with a result
     *                    reported to onRequestPermissionsResult(int, String[], int[])
     */
    private void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm}, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted, trying to get location again
                    try {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    } catch (SecurityException e) {
                        Log.e(TAG, "e:" + e);
                    }
                }
            }
        }
    }

    /**
     * Method to populate geofence list with data from the database
     */
    public void populateGeofenceList() {

        Cursor cursor = getContentResolver().query(EarthquakeColumns.CONTENT_URI,
                null,
                EarthquakeColumns.MAG + " >= ?",
                new String[] {String.valueOf(Utilities.getMagnitudePrefs(getBaseContext()))},
                null);

        if (cursor != null && cursor.getCount() > 0) {
            mGeofenceList.clear();
            int i = 0;
            while (cursor.moveToNext() && i < 80) { // Geofence limit is around 80 per device
                String place = cursor.getString(cursor.getColumnIndex(EarthquakeColumns.PLACE));
                double lat = cursor.getDouble(cursor.getColumnIndex(EarthquakeColumns.LATITUDE));
                double lng = cursor.getDouble(cursor.getColumnIndex(EarthquakeColumns.LONGITUDE));
                mGeofenceList.add(new Geofence.Builder()
                        .setRequestId(place)
                        .setCircularRegion(lat, lng, Utilities.getDistancePrefs(getBaseContext()))
                        .setExpirationDuration(LocationUtils.EXP_MILLIS)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build());
                i++;
            }
            cursor.close();
        }
    }

    /**
     * Create Geofencing request
     * @return GeofencingRequest initialised with a list of Geofences
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        // if just added and if inside the geofence
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(Status status) {
        if (status.isSuccess()) {
            Toast.makeText(this, getString(R.string.geofence_log_added), Toast.LENGTH_SHORT).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = LocationUtils.getErrorString(this, status.getStatusCode());
            Log.e(TAG, errorMessage);
        }
    }

    /**
     * Add Geofences to look for and send notifications on enter or exit
     */
    public void observeGeofences() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.googleapiclient_notconnected), Toast.LENGTH_SHORT).show();
            return;
        }

        if (mGeofenceList != null && mGeofenceList.size() > 0) {
            try {
                LocationServices.GeofencingApi.addGeofences(
                        mGoogleApiClient,
                        // The GeofenceRequest object.
                        getGeofencingRequest(),
                        // A pending intent that that is reused when calling removeGeofences(). This
                        // pending intent is used to generate an intent when a matched geofence
                        // transition is observed.
                        getGeofencePendingIntent()
                ).setResultCallback(this); // Result processed in onResult().
            } catch (SecurityException securityException) {
                // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            }
        }
    }

    /**
     * Sets ActionBar title to the corresponding view
     */
    private void setTitle() {
        if (mToolbar != null && mTab != null) {

            switch (mTab.getSelectedTabPosition()) {
                case 0:
                    mToolbar.setTitle(getString(R.string.title_recent));
                    break;
                case 1:
                    mToolbar.setTitle(getString(R.string.title_maps));
                    break;
                case 2:
                    mToolbar.setTitle(getString(R.string.title_news));
                    break;
                case 3:
                    mToolbar.setTitle(getString(R.string.title_info));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sharedprefs_key_syncfrequency))) {
            SyncAdapter.configurePeriodicSync(this,
                    Utilities.getSyncIntervalPrefs(this),
                    Utilities.getSyncIntervalPrefs(this) / 3);
        }
    }
}
