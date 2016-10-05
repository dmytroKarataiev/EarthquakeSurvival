package com.adkdevelopment.earthquakesurvival.data.provider.count;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.adkdevelopment.earthquakesurvival.data.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code count} table.
 */
public class CountCursor extends AbstractCursor implements CountModel {
    public CountCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(CountColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Year
     * Can be {@code null}.
     */
    @Nullable
    public Integer getCountYear() {
        Integer res = getIntegerOrNull(CountColumns.COUNT_YEAR);
        return res;
    }

    /**
     * Month
     * Can be {@code null}.
     */
    @Nullable
    public Integer getCountMonth() {
        Integer res = getIntegerOrNull(CountColumns.COUNT_MONTH);
        return res;
    }

    /**
     * Week
     * Can be {@code null}.
     */
    @Nullable
    public Integer getCountWeek() {
        Integer res = getIntegerOrNull(CountColumns.COUNT_WEEK);
        return res;
    }

    /**
     * Day
     * Can be {@code null}.
     */
    @Nullable
    public Integer getCountDay() {
        Integer res = getIntegerOrNull(CountColumns.COUNT_DAY);
        return res;
    }
}
