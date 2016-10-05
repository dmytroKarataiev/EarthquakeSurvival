package com.adkdevelopment.earthquakesurvival.data.provider.count;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.adkdevelopment.earthquakesurvival.data.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code count} table.
 */
public class CountContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return CountColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable CountSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable CountSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Year
     */
    public CountContentValues putCountYear(@Nullable Integer value) {
        mContentValues.put(CountColumns.COUNT_YEAR, value);
        return this;
    }

    public CountContentValues putCountYearNull() {
        mContentValues.putNull(CountColumns.COUNT_YEAR);
        return this;
    }

    /**
     * Month
     */
    public CountContentValues putCountMonth(@Nullable Integer value) {
        mContentValues.put(CountColumns.COUNT_MONTH, value);
        return this;
    }

    public CountContentValues putCountMonthNull() {
        mContentValues.putNull(CountColumns.COUNT_MONTH);
        return this;
    }

    /**
     * Week
     */
    public CountContentValues putCountWeek(@Nullable Integer value) {
        mContentValues.put(CountColumns.COUNT_WEEK, value);
        return this;
    }

    public CountContentValues putCountWeekNull() {
        mContentValues.putNull(CountColumns.COUNT_WEEK);
        return this;
    }

    /**
     * Day
     */
    public CountContentValues putCountDay(@Nullable Integer value) {
        mContentValues.put(CountColumns.COUNT_DAY, value);
        return this;
    }

    public CountContentValues putCountDayNull() {
        mContentValues.putNull(CountColumns.COUNT_DAY);
        return this;
    }
}
