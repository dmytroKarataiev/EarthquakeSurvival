package com.adkdevelopment.earthquakesurvival.data.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.adkdevelopment.earthquakesurvival.BuildConfig;
import com.adkdevelopment.earthquakesurvival.data.provider.base.BaseContentProvider;
import com.adkdevelopment.earthquakesurvival.data.provider.count.CountColumns;
import com.adkdevelopment.earthquakesurvival.data.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.data.provider.news.NewsColumns;

public class EarthquakeProvider extends BaseContentProvider {
    private static final String TAG = EarthquakeProvider.class.getSimpleName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";

    public static final String AUTHORITY = "com.adkdevelopment.earthquakesurvival.data.provider";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;

    private static final int URI_TYPE_COUNT = 0;
    private static final int URI_TYPE_COUNT_ID = 1;

    private static final int URI_TYPE_EARTHQUAKE = 2;
    private static final int URI_TYPE_EARTHQUAKE_ID = 3;

    private static final int URI_TYPE_NEWS = 4;
    private static final int URI_TYPE_NEWS_ID = 5;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, CountColumns.TABLE_NAME, URI_TYPE_COUNT);
        URI_MATCHER.addURI(AUTHORITY, CountColumns.TABLE_NAME + "/#", URI_TYPE_COUNT_ID);
        URI_MATCHER.addURI(AUTHORITY, EarthquakeColumns.TABLE_NAME, URI_TYPE_EARTHQUAKE);
        URI_MATCHER.addURI(AUTHORITY, EarthquakeColumns.TABLE_NAME + "/#", URI_TYPE_EARTHQUAKE_ID);
        URI_MATCHER.addURI(AUTHORITY, NewsColumns.TABLE_NAME, URI_TYPE_NEWS);
        URI_MATCHER.addURI(AUTHORITY, NewsColumns.TABLE_NAME + "/#", URI_TYPE_NEWS_ID);
    }

    @Override
    protected SQLiteOpenHelper createSqLiteOpenHelper() {
        return EarthquakeSQLiteOpenHelper.getInstance(getContext());
    }

    @Override
    protected boolean hasDebug() {
        return DEBUG;
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case URI_TYPE_COUNT:
                return TYPE_CURSOR_DIR + CountColumns.TABLE_NAME;
            case URI_TYPE_COUNT_ID:
                return TYPE_CURSOR_ITEM + CountColumns.TABLE_NAME;

            case URI_TYPE_EARTHQUAKE:
                return TYPE_CURSOR_DIR + EarthquakeColumns.TABLE_NAME;
            case URI_TYPE_EARTHQUAKE_ID:
                return TYPE_CURSOR_ITEM + EarthquakeColumns.TABLE_NAME;

            case URI_TYPE_NEWS:
                return TYPE_CURSOR_DIR + NewsColumns.TABLE_NAME;
            case URI_TYPE_NEWS_ID:
                return TYPE_CURSOR_ITEM + NewsColumns.TABLE_NAME;

        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //if (DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);
        return super.insert(uri, values);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        //if (DEBUG) Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);
        return super.bulkInsert(uri, values);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //if (DEBUG) Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.update(uri, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //if (DEBUG) Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.delete(uri, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //if (DEBUG)
        //    Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder
        //            + " groupBy=" + uri.getQueryParameter(QUERY_GROUP_BY) + " having=" + uri.getQueryParameter(QUERY_HAVING) + " limit=" + uri.getQueryParameter(QUERY_LIMIT));
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    protected QueryParams getQueryParams(Uri uri, String selection, String[] projection) {
        QueryParams res = new QueryParams();
        String id = null;
        int matchedId = URI_MATCHER.match(uri);
        switch (matchedId) {
            case URI_TYPE_COUNT:
            case URI_TYPE_COUNT_ID:
                res.table = CountColumns.TABLE_NAME;
                res.idColumn = CountColumns._ID;
                res.tablesWithJoins = CountColumns.TABLE_NAME;
                res.orderBy = CountColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_EARTHQUAKE:
            case URI_TYPE_EARTHQUAKE_ID:
                res.table = EarthquakeColumns.TABLE_NAME;
                res.idColumn = EarthquakeColumns._ID;
                res.tablesWithJoins = EarthquakeColumns.TABLE_NAME;
                res.orderBy = EarthquakeColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_NEWS:
            case URI_TYPE_NEWS_ID:
                res.table = NewsColumns.TABLE_NAME;
                res.idColumn = NewsColumns._ID;
                res.tablesWithJoins = NewsColumns.TABLE_NAME;
                res.orderBy = NewsColumns.DEFAULT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("The uri '" + uri + "' is not supported by this ContentProvider");
        }

        switch (matchedId) {
            case URI_TYPE_COUNT_ID:
            case URI_TYPE_EARTHQUAKE_ID:
            case URI_TYPE_NEWS_ID:
                id = uri.getLastPathSegment();
        }
        if (id != null) {
            if (selection != null) {
                res.selection = res.table + "." + res.idColumn + "=" + id + " and (" + selection + ")";
            } else {
                res.selection = res.table + "." + res.idColumn + "=" + id;
            }
        } else {
            res.selection = selection;
        }
        return res;
    }
}
