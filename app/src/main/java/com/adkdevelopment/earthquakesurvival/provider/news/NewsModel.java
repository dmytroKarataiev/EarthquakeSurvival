package com.adkdevelopment.earthquakesurvival.provider.news;

import com.adkdevelopment.earthquakesurvival.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * News
 */
public interface NewsModel extends BaseModel {

    /**
     * Date
     * Can be {@code null}.
     */
    @Nullable
    String getDate();

    /**
     * Title
     * Can be {@code null}.
     */
    @Nullable
    String getTitle();

    /**
     * Description
     * Can be {@code null}.
     */
    @Nullable
    String getDescription();

    /**
     * Url
     * Can be {@code null}.
     */
    @Nullable
    String getUrl();

    /**
     * GUID
     * Can be {@code null}.
     */
    @Nullable
    String getGuid();
}
