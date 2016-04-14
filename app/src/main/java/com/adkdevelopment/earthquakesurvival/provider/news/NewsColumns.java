package com.adkdevelopment.earthquakesurvival.provider.news;

import android.net.Uri;
import android.provider.BaseColumns;

import com.adkdevelopment.earthquakesurvival.provider.EarthquakeProvider;

/**
 * News
 */
public class NewsColumns implements BaseColumns {
    public static final String TABLE_NAME = "news";
    public static final Uri CONTENT_URI = Uri.parse(EarthquakeProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    /**
     * Date
     */
    public static final String DATE = "date";

    /**
     * Title
     */
    public static final String TITLE = "title";

    /**
     * Description
     */
    public static final String DESCRIPTION = "description";

    /**
     * Url
     */
    public static final String URL = "url";

    /**
     * GUID
     */
    public static final String GUID = "guid";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            DATE,
            TITLE,
            DESCRIPTION,
            URL,
            GUID
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(DATE) || c.contains("." + DATE)) return true;
            if (c.equals(TITLE) || c.contains("." + TITLE)) return true;
            if (c.equals(DESCRIPTION) || c.contains("." + DESCRIPTION)) return true;
            if (c.equals(URL) || c.contains("." + URL)) return true;
            if (c.equals(GUID) || c.contains("." + GUID)) return true;
        }
        return false;
    }

}
