package com.adkdevelopment.earthquakesurvival.provider.count;

import android.net.Uri;
import android.provider.BaseColumns;

import com.adkdevelopment.earthquakesurvival.provider.EarthquakeProvider;
import com.adkdevelopment.earthquakesurvival.provider.count.CountColumns;
import com.adkdevelopment.earthquakesurvival.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.provider.news.NewsColumns;

/**
 * Count Earthquakes
 */
public class CountColumns implements BaseColumns {
    public static final String TABLE_NAME = "count";
    public static final Uri CONTENT_URI = Uri.parse(EarthquakeProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    /**
     * Year
     */
    public static final String COUNT_YEAR = "count_year";

    /**
     * Month
     */
    public static final String COUNT_MONTH = "count_month";

    /**
     * Week
     */
    public static final String COUNT_WEEK = "count_week";

    /**
     * Day
     */
    public static final String COUNT_DAY = "count_day";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            COUNT_YEAR,
            COUNT_MONTH,
            COUNT_WEEK,
            COUNT_DAY
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(COUNT_YEAR) || c.contains("." + COUNT_YEAR)) return true;
            if (c.equals(COUNT_MONTH) || c.contains("." + COUNT_MONTH)) return true;
            if (c.equals(COUNT_WEEK) || c.contains("." + COUNT_WEEK)) return true;
            if (c.equals(COUNT_DAY) || c.contains("." + COUNT_DAY)) return true;
        }
        return false;
    }

}
