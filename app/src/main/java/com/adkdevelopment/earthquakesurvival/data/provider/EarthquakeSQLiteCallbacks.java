package com.adkdevelopment.earthquakesurvival.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.adkdevelopment.earthquakesurvival.data.provider.count.CountColumns;
import com.adkdevelopment.earthquakesurvival.data.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.data.provider.news.NewsColumns;

/**
 * Implement your custom database creation or upgrade code here.
 *
 * This file will not be overwritten if you re-run the content provider generator.
 */
public class EarthquakeSQLiteCallbacks {

    public void onOpen(final Context context, final SQLiteDatabase db) {
        //if (BuildConfig.DEBUG) Log.d(TAG, "onOpen");
        // Insert your db open code here.
    }

    public void onPreCreate(final Context context, final SQLiteDatabase db) {
        //if (BuildConfig.DEBUG) Log.d(TAG, "onPreCreate");
        // Insert your db creation code here. This is called before your tables are created.
    }

    public void onPostCreate(final Context context, final SQLiteDatabase db) {
        //if (BuildConfig.DEBUG) Log.d(TAG, "onPostCreate");
        // Insert your db creation code here. This is called after your tables are created.
    }

    public void onUpgrade(final Context context, final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        //if (BuildConfig.DEBUG) Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        // Insert your upgrading code here.
        db.execSQL("DROP TABLE IF EXISTS " + CountColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EarthquakeColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NewsColumns.TABLE_NAME);
        onPreCreate(context, db);
        db.execSQL(EarthquakeSQLiteOpenHelper.SQL_CREATE_TABLE_COUNT);
        db.execSQL(EarthquakeSQLiteOpenHelper.SQL_CREATE_TABLE_EARTHQUAKE);
        db.execSQL(EarthquakeSQLiteOpenHelper.SQL_CREATE_TABLE_NEWS);
        onPostCreate(context, db);
    }
}
