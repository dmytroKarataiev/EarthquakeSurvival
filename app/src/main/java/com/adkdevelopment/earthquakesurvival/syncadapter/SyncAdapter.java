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

package com.adkdevelopment.earthquakesurvival.syncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannedString;
import android.text.format.DateUtils;
import android.util.Log;

import com.adkdevelopment.earthquakesurvival.App;
import com.adkdevelopment.earthquakesurvival.R;
import com.adkdevelopment.earthquakesurvival.objects.earthquake.EarthquakeObject;
import com.adkdevelopment.earthquakesurvival.objects.earthquake.Feature;
import com.adkdevelopment.earthquakesurvival.objects.news.Channel;
import com.adkdevelopment.earthquakesurvival.objects.news.Item;
import com.adkdevelopment.earthquakesurvival.objects.news.Rss;
import com.adkdevelopment.earthquakesurvival.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.provider.news.NewsColumns;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by karataev on 4/1/16.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();

    // Broadcast message to the widget
    public static final String ACTION_DATA_UPDATE = "com.adkdevelopment.earthquakesurvival.ACTION_DATA_UPDATED";

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {

        App.getApiManager().getEarthquakeService().getData().enqueue(new Callback<EarthquakeObject>() {
            @Override
            public void onResponse(Call<EarthquakeObject> call, Response<EarthquakeObject> response) {
                EarthquakeObject earthquake = response.body();

                //Log.d(TAG, "onResponse: success " + earthquake.getFeatures().size());

                Vector<ContentValues> cVVector = new Vector<>(earthquake.getFeatures().size());
                for (Feature each : earthquake.getFeatures()) {

                    ContentValues weatherValues = new ContentValues();

                    weatherValues.put(EarthquakeColumns.PLACE, each.getProperties().getPlace());
                    weatherValues.put(EarthquakeColumns.ID_EARTH, each.getId());
                    weatherValues.put(EarthquakeColumns.MAG, each.getProperties().getMag());
                    weatherValues.put(EarthquakeColumns.TYPE, each.getProperties().getType());
                    weatherValues.put(EarthquakeColumns.ALERT, each.getProperties().getAlert());
                    weatherValues.put(EarthquakeColumns.TIME, each.getProperties().getTime());
                    weatherValues.put(EarthquakeColumns.URL, each.getProperties().getUrl());
                    weatherValues.put(EarthquakeColumns.DETAIL, each.getProperties().getDetail());
                    weatherValues.put(EarthquakeColumns.DEPTH, each.getGeometry().getCoordinates().get(2));
                    weatherValues.put(EarthquakeColumns.LONGITUDE, each.getGeometry().getCoordinates().get(0));
                    weatherValues.put(EarthquakeColumns.LATITUDE, each.getGeometry().getCoordinates().get(1));

                    cVVector.add(weatherValues);
                }

                int inserted = 0;
                // add to database
                ContentResolver resolver = getContext().getContentResolver();

                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = resolver.bulkInsert(EarthquakeColumns.CONTENT_URI, cvArray);
                }

                // Set the date to day minus one to delete old data from the database
                Date date = new Date();
                date.setTime(date.getTime() - DateUtils.DAY_IN_MILLIS);

                int deleted = resolver.delete(EarthquakeColumns.CONTENT_URI, EarthquakeColumns.TIME + " <= ?", new String[]{String.valueOf(date.getTime())});
                //Log.v(TAG, "Service Complete. " + inserted + " Inserted, " + deleted + " deleted");
            }

            @Override
            public void onFailure(Call<EarthquakeObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });

        App.getNewsManager().getNewsService().getNews().enqueue(new Callback<Rss>() {
            @Override
            public void onResponse(Call<Rss> call, Response<Rss> response) {
                Channel news = response.body().getChannel();

                Vector<ContentValues> cVVector = new Vector<>(news.getItem().size());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
                Date date = new Date();

                for (Item each : news.getItem()) {

                    ContentValues weatherValues = new ContentValues();

                    try {
                        date = simpleDateFormat.parse(each.getPubDate());
                    } catch (ParseException e) {
                        Log.e(TAG, "e:" + e);
                    }

                    weatherValues.put(NewsColumns.DATE, date.getTime());
                    weatherValues.put(NewsColumns.TITLE, each.getTitle());
                    weatherValues.put(NewsColumns.DESCRIPTION, Html.toHtml(new SpannedString(each.getDescription())));
                    weatherValues.put(NewsColumns.URL, each.getLink());
                    weatherValues.put(NewsColumns.GUID, each.getGuid().getContent());

                    cVVector.add(weatherValues);
                }

                int inserted = 0;
                // add to database
                ContentResolver resolver = getContext().getContentResolver();

                if (cVVector.size() > 0) {

                    // Student: call bulkInsert to add the weatherEntries to the database here
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);

                    inserted = resolver.bulkInsert(NewsColumns.CONTENT_URI, cvArray);
                }

                // Set the date to day minus one to delete old data from the database
                date = new Date();
                date.setTime(date.getTime() - DateUtils.DAY_IN_MILLIS * 2);

                int deleted = resolver.delete(NewsColumns.CONTENT_URI, NewsColumns.DATE + " <= ?", new String[]{String.valueOf(date.getTime())});
                Log.v(TAG, "Service Complete. " + inserted + " Inserted, " + deleted + " deleted");

            }

            @Override
            public void onFailure(Call<Rss> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });

        // notify PagerActivity that data has been updated
        getContext().getContentResolver().notifyChange(EarthquakeColumns.CONTENT_URI, null, false);

        updateWidgets();
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);

        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        SyncAdapter.configurePeriodicSync(context,
                Utilities.getSyncIntervalPrefs(context),
                Utilities.getSyncIntervalPrefs(context) / 3);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.sync_provider_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.sync_provider_authority);
        if (syncInterval == -1) {
            ContentResolver.setSyncAutomatically(account, context.getString(R.string.sync_provider_authority), false);
        } else {
            ContentResolver.setSyncAutomatically(account, context.getString(R.string.sync_provider_authority), true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // we can enable inexact timers in our periodic sync
                SyncRequest request = new SyncRequest.Builder().
                        syncPeriodic(syncInterval, flexTime).
                        setSyncAdapter(account, authority).
                        setExtras(new Bundle()).build();
                ContentResolver.requestSync(request);
            } else {
                ContentResolver.addPeriodicSync(account,
                        authority, new Bundle(), syncInterval);
            }
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.sync_provider_authority), bundle);
    }

    /**
     * Broadcast Message to widgets to update data
     */
    public void updateWidgets() {

        // Send intent to the Widget to notify that the data was updated
        Intent dataUpdated = new Intent(ACTION_DATA_UPDATE)
                // Ensures that only components in the app will receive the broadcast
                .setPackage(getContext().getPackageName());
        getContext().sendBroadcast(dataUpdated);
    }
}
