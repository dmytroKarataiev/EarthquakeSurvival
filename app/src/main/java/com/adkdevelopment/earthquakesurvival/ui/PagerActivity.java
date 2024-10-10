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
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adkdevelopment.earthquakesurvival.App;
import com.adkdevelopment.earthquakesurvival.R;
import com.adkdevelopment.earthquakesurvival.data.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.ui.report_earthquake.ReportActivity;
import com.adkdevelopment.earthquakesurvival.data.syncadapter.SyncAdapter;
import com.adkdevelopment.earthquakesurvival.eventbus.RxBus;
import com.adkdevelopment.earthquakesurvival.ui.adapters.PagerAdapter;
import com.adkdevelopment.earthquakesurvival.ui.behaviour.ZoomOutPageTransformer;
import com.adkdevelopment.earthquakesurvival.ui.geofence.GeofenceService;
import com.adkdevelopment.earthquakesurvival.ui.settings.SettingsActivity;
import com.adkdevelopment.earthquakesurvival.utils.LocationUtils;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;
import com.adkdevelopment.license.ui.LicenseActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Main phone activity
 */
public class PagerActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ResultCallback<Status>, SharedPreferences.OnSharedPreferenceChangeListener {

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

    // Geofence variables
    private List<Geofence> mGeofenceList;
    private ContentObserver mObserver;

    @BindView(R.id.sliding_tabs)
    TabLayout mTab;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindColor(R.color.tab_item_selected)
    int mColorSelected;
    @BindColor(R.color.tab_item_unselected)
    int mColorUnselected;

    public static final int FRAGMENT_RECENT = 0;
    public static final int FRAGMENT_MAP = 1;
    public static final int FRAGMENT_NEWS = 2;
    public static final int FRAGMENT_INFO = 3;

    // RxJava eventbus
    private RxBus _rxBus;
    private CompositeSubscription _subscription;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_activity);

        _rxBus = App.getRxBusSingleton();

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

        if (Utilities.checkPlayServices(this)) {
            // Set up GoogleApiClient
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

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
        if (mGoogleApiClient != null &&
                (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting())) {
            mGoogleApiClient.connect();
        }

        _subscription = new CompositeSubscription();
        _subscription.add(_rxBus.toObservable().subscribe(o -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && o instanceof Pair) {
                Pair pair = (Pair) o;
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this, (Pair) pair.second)
                        .toBundle();
                startActivity((Intent) pair.first, bundle);
            } else if (o instanceof Intent) {
                startActivity((Intent) o);
            }
        }));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null &&
                (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting())) {
            mGoogleApiClient.disconnect();
        }
        _subscription.clear();
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
            // TODO(Dmytro Karataiev): 3/12/17 finish this activity 
            /*case R.id.action_report:
                startActivity(new Intent(this, ReportActivity.class));
                return true;*/
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_refresh:
                SyncAdapter.syncImmediately(getBaseContext());
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.action_license:
                startActivity(new Intent(this, LicenseActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to set tab images and highlights on tab switching
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

            // Highlight image and text on selection
            mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationUtils.setLocation(this, location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
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
                        if (mGoogleApiClient.isConnected()) {
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                        }
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
                new String[]{String.valueOf(Utilities.getMagnitudePrefs(getBaseContext()))},
                null);

        if (cursor != null && cursor.getCount() > 0) {
            mGeofenceList = new ArrayList<>();
            int i = 0;

            int radius = Utilities.getDistancePrefs(getBaseContext());
            if (radius > 0) {
                while (cursor.moveToNext() && i < 80) { // Geofence limit is around 80 per device
                    String url = cursor.getString(cursor.getColumnIndex(EarthquakeColumns.URL));
                    double lat = cursor.getDouble(cursor.getColumnIndex(EarthquakeColumns.LATITUDE));
                    double lng = cursor.getDouble(cursor.getColumnIndex(EarthquakeColumns.LONGITUDE));
                    mGeofenceList.add(new Geofence.Builder()
                            .setRequestId(url)
                            .setCircularRegion(lat, lng, radius)
                            .setExpirationDuration(LocationUtils.EXP_MILLIS)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                            .build());
                    i++;
                }
            }
            cursor.close();
        }
    }

    /**
     * Create Geofencing request
     *
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
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected() || mGoogleApiClient == null) {
            //Toast.makeText(this, getString(R.string.googleapiclient_notconnected), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "GoogleApiClient is not connected or null");
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
                case FRAGMENT_RECENT:
                    mToolbar.setTitle(getString(R.string.title_recent));
                    break;
                case FRAGMENT_MAP:
                    mToolbar.setTitle(getString(R.string.title_maps));
                    break;
                case FRAGMENT_NEWS:
                    mToolbar.setTitle(getString(R.string.title_news));
                    break;
                case FRAGMENT_INFO:
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
