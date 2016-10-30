package com.adkdevelopment.earthquakesurvival.data.provider.earthquake;

import android.net.Uri;
import android.provider.BaseColumns;

import com.adkdevelopment.earthquakesurvival.data.provider.EarthquakeProvider;

/**
 * An earthquake object
 */
public class EarthquakeColumns implements BaseColumns {
    public static final String TABLE_NAME = "earthquake";
    public static final Uri CONTENT_URI = Uri.parse(EarthquakeProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    /**
     * Id
     */
    public static final String ID_EARTH = "id_earth";

    /**
     * Place
     */
    public static final String PLACE = "place";

    /**
     * Magnitude
     */
    public static final String MAG = "mag";

    /**
     * Type
     */
    public static final String TYPE = "type";

    /**
     * Alert type
     */
    public static final String ALERT = "alert";

    /**
     * Time
     */
    public static final String TIME = "time";

    /**
     * Url
     */
    public static final String URL = "url";

    /**
     * Detail
     */
    public static final String DETAIL = "detail";

    /**
     * Depth
     */
    public static final String DEPTH = "depth";

    /**
     * Longitude
     */
    public static final String LONGITUDE = "longitude";

    /**
     * Latitude
     */
    public static final String LATITUDE = "latitude";

    /**
     * Latitude
     */
    public static final String DISTANCE = "distance";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            ID_EARTH,
            PLACE,
            MAG,
            TYPE,
            ALERT,
            TIME,
            URL,
            DETAIL,
            DEPTH,
            LONGITUDE,
            LATITUDE,
            DISTANCE
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(ID_EARTH) || c.contains("." + ID_EARTH)) return true;
            if (c.equals(PLACE) || c.contains("." + PLACE)) return true;
            if (c.equals(MAG) || c.contains("." + MAG)) return true;
            if (c.equals(TYPE) || c.contains("." + TYPE)) return true;
            if (c.equals(ALERT) || c.contains("." + ALERT)) return true;
            if (c.equals(TIME) || c.contains("." + TIME)) return true;
            if (c.equals(URL) || c.contains("." + URL)) return true;
            if (c.equals(DETAIL) || c.contains("." + DETAIL)) return true;
            if (c.equals(DEPTH) || c.contains("." + DEPTH)) return true;
            if (c.equals(LONGITUDE) || c.contains("." + LONGITUDE)) return true;
            if (c.equals(LATITUDE) || c.contains("." + LATITUDE)) return true;
            if (c.equals(DISTANCE) || c.contains("." + DISTANCE)) return true;
        }
        return false;
    }

}
