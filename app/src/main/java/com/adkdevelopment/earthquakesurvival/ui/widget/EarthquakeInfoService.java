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

package com.adkdevelopment.earthquakesurvival.ui.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.adkdevelopment.earthquakesurvival.ui.MainActivity;
import com.adkdevelopment.earthquakesurvival.R;
import com.adkdevelopment.earthquakesurvival.data.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.utils.LocationUtils;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;
import com.google.android.gms.maps.model.LatLng;

/**
 * Updates widget on background thread
 * Created by karataev on 4/13/16.
 */
public class EarthquakeInfoService extends IntentService {

    public EarthquakeInfoService() {
        super("EarthquakeInfoWidget");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Retrieve all the widgets IDs
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, EarthquakeWidgetProvider.class));

        Cursor data = getContentResolver()
                .query(EarthquakeColumns.CONTENT_URI,
                        new String[]{EarthquakeColumns.TIME},
                        null,
                        null,
                        null);

        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        long count = data.getCount();

        data.close();

        data = getContentResolver().query(EarthquakeColumns.CONTENT_URI,
                null,
                null,
                null,
                EarthquakeColumns.MAG + " DESC LIMIT 1");

        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        String desc = data.getString(data.getColumnIndex(EarthquakeColumns.PLACE));
        desc = getBaseContext().getString(R.string.earthquake_widget_largest) + " " + desc;

        Double magnitude = data.getDouble(data.getColumnIndex(EarthquakeColumns.MAG));
        Long dateMillis = data.getLong(data.getColumnIndex(EarthquakeColumns.TIME));
        Double latitude = data.getDouble(data.getColumnIndex(EarthquakeColumns.LATITUDE));
        Double longitude = data.getDouble(data.getColumnIndex(EarthquakeColumns.LONGITUDE));

        LatLng latLng = new LatLng(latitude, longitude);

        String distance = getBaseContext().getString(R.string.earthquake_distance,
                LocationUtils.getDistance(latLng, LocationUtils.getLocation(getBaseContext())));

        data.close();

        for (int id : appWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_layout);

            remoteViews.setTextViewText(R.id.widget_text_total, getBaseContext().getString(R.string.earthquake_statistics_day, count));
            remoteViews.setTextViewText(R.id.widget_text_description, desc);
            remoteViews.setTextViewText(R.id.widget_text_magnitude, getBaseContext().getString(R.string.earthquake_magnitude, magnitude));
            remoteViews.setTextViewText(R.id.widget_text_date, Utilities.getFormattedDate(dateMillis));
            remoteViews.setTextViewText(R.id.widget_text_distance, distance);

            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);

            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(id, remoteViews);
        }

    }

}
