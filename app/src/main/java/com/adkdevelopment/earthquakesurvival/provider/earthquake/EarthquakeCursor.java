package com.adkdevelopment.earthquakesurvival.provider.earthquake;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.adkdevelopment.earthquakesurvival.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code earthquake} table.
 */
public class EarthquakeCursor extends AbstractCursor implements EarthquakeModel {
    public EarthquakeCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(EarthquakeColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Id
     * Can be {@code null}.
     */
    @Nullable
    public String getIdEarth() {
        String res = getStringOrNull(EarthquakeColumns.ID_EARTH);
        return res;
    }

    /**
     * Place
     * Can be {@code null}.
     */
    @Nullable
    public String getPlace() {
        String res = getStringOrNull(EarthquakeColumns.PLACE);
        return res;
    }

    /**
     * Magnitude
     * Can be {@code null}.
     */
    @Nullable
    public Double getMag() {
        Double res = getDoubleOrNull(EarthquakeColumns.MAG);
        return res;
    }

    /**
     * Type
     * Can be {@code null}.
     */
    @Nullable
    public String getType() {
        String res = getStringOrNull(EarthquakeColumns.TYPE);
        return res;
    }

    /**
     * Alert type
     * Can be {@code null}.
     */
    @Nullable
    public String getAlert() {
        String res = getStringOrNull(EarthquakeColumns.ALERT);
        return res;
    }

    /**
     * Time
     */
    public long getTime() {
        Long res = getLongOrNull(EarthquakeColumns.TIME);
        if (res == null)
            throw new NullPointerException("The value of 'time' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Url
     * Can be {@code null}.
     */
    @Nullable
    public String getUrl() {
        String res = getStringOrNull(EarthquakeColumns.URL);
        return res;
    }

    /**
     * Detail
     * Can be {@code null}.
     */
    @Nullable
    public String getDetail() {
        String res = getStringOrNull(EarthquakeColumns.DETAIL);
        return res;
    }

    /**
     * Depth
     * Can be {@code null}.
     */
    @Nullable
    public Double getDepth() {
        Double res = getDoubleOrNull(EarthquakeColumns.DEPTH);
        return res;
    }

    /**
     * Longitude
     * Can be {@code null}.
     */
    @Nullable
    public Double getLongitude() {
        Double res = getDoubleOrNull(EarthquakeColumns.LONGITUDE);
        return res;
    }

    /**
     * Latitude
     * Can be {@code null}.
     */
    @Nullable
    public Double getLatitude() {
        Double res = getDoubleOrNull(EarthquakeColumns.LATITUDE);
        return res;
    }
}
