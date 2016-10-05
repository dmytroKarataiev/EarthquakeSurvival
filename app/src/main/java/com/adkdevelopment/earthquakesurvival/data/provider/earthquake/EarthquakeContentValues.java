package com.adkdevelopment.earthquakesurvival.data.provider.earthquake;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.adkdevelopment.earthquakesurvival.data.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code earthquake} table.
 */
public class EarthquakeContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return EarthquakeColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable EarthquakeSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param context contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable EarthquakeSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Id
     */
    public EarthquakeContentValues putIdEarth(@Nullable String value) {
        mContentValues.put(EarthquakeColumns.ID_EARTH, value);
        return this;
    }

    public EarthquakeContentValues putIdEarthNull() {
        mContentValues.putNull(EarthquakeColumns.ID_EARTH);
        return this;
    }

    /**
     * Place
     */
    public EarthquakeContentValues putPlace(@Nullable String value) {
        mContentValues.put(EarthquakeColumns.PLACE, value);
        return this;
    }

    public EarthquakeContentValues putPlaceNull() {
        mContentValues.putNull(EarthquakeColumns.PLACE);
        return this;
    }

    /**
     * Magnitude
     */
    public EarthquakeContentValues putMag(@Nullable Double value) {
        mContentValues.put(EarthquakeColumns.MAG, value);
        return this;
    }

    public EarthquakeContentValues putMagNull() {
        mContentValues.putNull(EarthquakeColumns.MAG);
        return this;
    }

    /**
     * Type
     */
    public EarthquakeContentValues putType(@Nullable String value) {
        mContentValues.put(EarthquakeColumns.TYPE, value);
        return this;
    }

    public EarthquakeContentValues putTypeNull() {
        mContentValues.putNull(EarthquakeColumns.TYPE);
        return this;
    }

    /**
     * Alert type
     */
    public EarthquakeContentValues putAlert(@Nullable String value) {
        mContentValues.put(EarthquakeColumns.ALERT, value);
        return this;
    }

    public EarthquakeContentValues putAlertNull() {
        mContentValues.putNull(EarthquakeColumns.ALERT);
        return this;
    }

    /**
     * Time
     */
    public EarthquakeContentValues putTime(long value) {
        mContentValues.put(EarthquakeColumns.TIME, value);
        return this;
    }


    /**
     * Url
     */
    public EarthquakeContentValues putUrl(@Nullable String value) {
        mContentValues.put(EarthquakeColumns.URL, value);
        return this;
    }

    public EarthquakeContentValues putUrlNull() {
        mContentValues.putNull(EarthquakeColumns.URL);
        return this;
    }

    /**
     * Detail
     */
    public EarthquakeContentValues putDetail(@Nullable String value) {
        mContentValues.put(EarthquakeColumns.DETAIL, value);
        return this;
    }

    public EarthquakeContentValues putDetailNull() {
        mContentValues.putNull(EarthquakeColumns.DETAIL);
        return this;
    }

    /**
     * Depth
     */
    public EarthquakeContentValues putDepth(@Nullable Double value) {
        mContentValues.put(EarthquakeColumns.DEPTH, value);
        return this;
    }

    public EarthquakeContentValues putDepthNull() {
        mContentValues.putNull(EarthquakeColumns.DEPTH);
        return this;
    }

    /**
     * Longitude
     */
    public EarthquakeContentValues putLongitude(@Nullable Double value) {
        mContentValues.put(EarthquakeColumns.LONGITUDE, value);
        return this;
    }

    public EarthquakeContentValues putLongitudeNull() {
        mContentValues.putNull(EarthquakeColumns.LONGITUDE);
        return this;
    }

    /**
     * Latitude
     */
    public EarthquakeContentValues putLatitude(@Nullable Double value) {
        mContentValues.put(EarthquakeColumns.LATITUDE, value);
        return this;
    }

    public EarthquakeContentValues putLatitudeNull() {
        mContentValues.putNull(EarthquakeColumns.LATITUDE);
        return this;
    }
}
