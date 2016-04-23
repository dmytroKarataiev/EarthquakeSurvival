package com.adkdevelopment.earthquakesurvival.provider.count;

import com.adkdevelopment.earthquakesurvival.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
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
