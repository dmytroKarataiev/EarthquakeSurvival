package com.adkdevelopment.earthquakesurvival.provider.news;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.adkdevelopment.earthquakesurvival.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code news} table.
 */
public class NewsCursor extends AbstractCursor implements NewsModel {
    public NewsCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(NewsColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Date
     * Can be {@code null}.
     */
    @Nullable
    public String getDate() {
        String res = getStringOrNull(NewsColumns.DATE);
        return res;
    }

    /**
     * Title
     * Can be {@code null}.
     */
    @Nullable
    public String getTitle() {
        String res = getStringOrNull(NewsColumns.TITLE);
        return res;
    }

    /**
     * Description
     * Can be {@code null}.
     */
    @Nullable
    public String getDescription() {
        String res = getStringOrNull(NewsColumns.DESCRIPTION);
        return res;
    }

    /**
     * Url
     * Can be {@code null}.
     */
    @Nullable
    public String getUrl() {
        String res = getStringOrNull(NewsColumns.URL);
        return res;
    }

    /**
     * GUID
     * Can be {@code null}.
     */
    @Nullable
    public String getGuid() {
        String res = getStringOrNull(NewsColumns.GUID);
        return res;
    }
}
