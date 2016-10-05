package com.adkdevelopment.earthquakesurvival.data.provider.news;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.adkdevelopment.earthquakesurvival.data.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code news} table.
 */
public class NewsContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return NewsColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable NewsSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param context contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable NewsSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Date
     */
    public NewsContentValues putDate(@Nullable String value) {
        mContentValues.put(NewsColumns.DATE, value);
        return this;
    }

    public NewsContentValues putDateNull() {
        mContentValues.putNull(NewsColumns.DATE);
        return this;
    }

    /**
     * Title
     */
    public NewsContentValues putTitle(@Nullable String value) {
        mContentValues.put(NewsColumns.TITLE, value);
        return this;
    }

    public NewsContentValues putTitleNull() {
        mContentValues.putNull(NewsColumns.TITLE);
        return this;
    }

    /**
     * Description
     */
    public NewsContentValues putDescription(@Nullable String value) {
        mContentValues.put(NewsColumns.DESCRIPTION, value);
        return this;
    }

    public NewsContentValues putDescriptionNull() {
        mContentValues.putNull(NewsColumns.DESCRIPTION);
        return this;
    }

    /**
     * Url
     */
    public NewsContentValues putUrl(@Nullable String value) {
        mContentValues.put(NewsColumns.URL, value);
        return this;
    }

    public NewsContentValues putUrlNull() {
        mContentValues.putNull(NewsColumns.URL);
        return this;
    }

    /**
     * GUID
     */
    public NewsContentValues putGuid(@Nullable String value) {
        mContentValues.put(NewsColumns.GUID, value);
        return this;
    }

    public NewsContentValues putGuidNull() {
        mContentValues.putNull(NewsColumns.GUID);
        return this;
    }
}
