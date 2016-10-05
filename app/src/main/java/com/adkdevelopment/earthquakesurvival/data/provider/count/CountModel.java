package com.adkdevelopment.earthquakesurvival.data.provider.count;

import com.adkdevelopment.earthquakesurvival.data.provider.base.BaseModel;

import android.support.annotation.Nullable;

/**
 * Count Earthquakes
 */
public interface CountModel extends BaseModel {

    /**
     * Year
     * Can be {@code null}.
     */
    @Nullable
    Integer getCountYear();

    /**
     * Month
     * Can be {@code null}.
     */
    @Nullable
    Integer getCountMonth();

    /**
     * Week
     * Can be {@code null}.
     */
    @Nullable
    Integer getCountWeek();

    /**
     * Day
     * Can be {@code null}.
     */
    @Nullable
    Integer getCountDay();
}
