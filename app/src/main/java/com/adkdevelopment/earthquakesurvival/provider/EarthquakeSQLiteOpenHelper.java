package com.adkdevelopment.earthquakesurvival.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.adkdevelopment.earthquakesurvival.provider.count.CountColumns;
import com.adkdevelopment.earthquakesurvival.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.provider.news.NewsColumns;

public class EarthquakeSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = EarthquakeSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "earthquake.db";
    private static final int DATABASE_VERSION = 2;
    private static EarthquakeSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final EarthquakeSQLiteCallbacks mOpenHelperCallbacks;

    // @formatter:off
    public static final String SQL_CREATE_TABLE_COUNT = "CREATE TABLE IF NOT EXISTS "
            + CountColumns.TABLE_NAME + " ( "
            + CountColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CountColumns.COUNT_YEAR + " INTEGER, "
            + CountColumns.COUNT_MONTH + " INTEGER, "
            + CountColumns.COUNT_WEEK + " INTEGER, "
            + CountColumns.COUNT_DAY + " INTEGER "
            + " );";

    public static final String SQL_CREATE_TABLE_EARTHQUAKE = "CREATE TABLE IF NOT EXISTS "
            + EarthquakeColumns.TABLE_NAME + " ( "
            + EarthquakeColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EarthquakeColumns.ID_EARTH + " TEXT, "
            + EarthquakeColumns.PLACE + " TEXT, "
            + EarthquakeColumns.MAG + " REAL, "
            + EarthquakeColumns.TYPE + " TEXT, "
            + EarthquakeColumns.ALERT + " TEXT, "
            + EarthquakeColumns.TIME + " INTEGER NOT NULL, "
            + EarthquakeColumns.URL + " TEXT, "
            + EarthquakeColumns.DETAIL + " TEXT, "
            + EarthquakeColumns.DEPTH + " REAL, "
            + EarthquakeColumns.LONGITUDE + " REAL, "
            + EarthquakeColumns.LATITUDE + " REAL "
            + ", CONSTRAINT unique_id UNIQUE (id_earth) ON CONFLICT REPLACE"
            + " );";

    public static final String SQL_CREATE_TABLE_NEWS = "CREATE TABLE IF NOT EXISTS "
            + NewsColumns.TABLE_NAME + " ( "
            + NewsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NewsColumns.DATE + " INTEGER, "
            + NewsColumns.TITLE + " TEXT, "
            + NewsColumns.DESCRIPTION + " TEXT, "
            + NewsColumns.URL + " TEXT, "
            + NewsColumns.GUID + " TEXT "
            + ", CONSTRAINT unique_id UNIQUE (date) ON CONFLICT REPLACE"
            + " );";

    // @formatter:on

    public static EarthquakeSQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static EarthquakeSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */
    private static EarthquakeSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new EarthquakeSQLiteOpenHelper(context);
    }

    private EarthquakeSQLiteOpenHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new EarthquakeSQLiteCallbacks();
    }


    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static EarthquakeSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new EarthquakeSQLiteOpenHelper(context, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private EarthquakeSQLiteOpenHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new EarthquakeSQLiteCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_COUNT);
        db.execSQL(SQL_CREATE_TABLE_EARTHQUAKE);
        db.execSQL(SQL_CREATE_TABLE_NEWS);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
