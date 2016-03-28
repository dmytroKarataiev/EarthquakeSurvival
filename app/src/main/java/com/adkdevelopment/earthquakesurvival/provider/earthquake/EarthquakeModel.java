package com.adkdevelopment.earthquakesurvival.provider.earthquake;

import com.adkdevelopment.earthquakesurvival.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * An earthquake object
 */
public interface EarthquakeModel extends BaseModel {

    /**
     * Id
     * Can be {@code null}.
     */
    @Nullable
    String getIdEarth();

    /**
     * Place
     * Can be {@code null}.
     */
    @Nullable
    String getPlace();

    /**
     * Magnitude
     * Can be {@code null}.
     */
    @Nullable
    Double getMag();

    /**
     * Type
     * Can be {@code null}.
     */
    @Nullable
    String getType();

    /**
     * Alert type
     * Can be {@code null}.
     */
    @Nullable
    String getAlert();

    /**
     * Time
     */
    long getTime();

    /**
     * Url
     * Can be {@code null}.
     */
    @Nullable
    String getUrl();

    /**
     * Detail
     * Can be {@code null}.
     */
    @Nullable
    String getDetail();

    /**
     * Depth
     * Can be {@code null}.
     */
    @Nullable
    Double getDepth();

    /**
     * Longitude
     * Can be {@code null}.
     */
    @Nullable
    Double getLongitude();

    /**
     * Latitude
     * Can be {@code null}.
     */
    @Nullable
    Double getLatitude();
}
