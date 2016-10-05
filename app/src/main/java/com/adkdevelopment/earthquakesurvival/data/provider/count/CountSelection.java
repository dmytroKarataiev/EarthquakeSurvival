package com.adkdevelopment.earthquakesurvival.data.provider.count;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.adkdevelopment.earthquakesurvival.data.provider.base.AbstractSelection;

/**
 * Selection for the {@code count} table.
 */
public class CountSelection extends AbstractSelection<CountSelection> {
    @Override
    protected Uri baseUri() {
        return CountColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code CountCursor} object, which is positioned before the first entry, or null.
     */
    public CountCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new CountCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public CountCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code CountCursor} object, which is positioned before the first entry, or null.
     */
    public CountCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new CountCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public CountCursor query(Context context) {
        return query(context, null);
    }


    public CountSelection id(long... value) {
        addEquals("count." + CountColumns._ID, toObjectArray(value));
        return this;
    }

    public CountSelection idNot(long... value) {
        addNotEquals("count." + CountColumns._ID, toObjectArray(value));
        return this;
    }

    public CountSelection orderById(boolean desc) {
        orderBy("count." + CountColumns._ID, desc);
        return this;
    }

    public CountSelection orderById() {
        return orderById(false);
    }

    public CountSelection countYear(Integer... value) {
        addEquals(CountColumns.COUNT_YEAR, value);
        return this;
    }

    public CountSelection countYearNot(Integer... value) {
        addNotEquals(CountColumns.COUNT_YEAR, value);
        return this;
    }

    public CountSelection countYearGt(int value) {
        addGreaterThan(CountColumns.COUNT_YEAR, value);
        return this;
    }

    public CountSelection countYearGtEq(int value) {
        addGreaterThanOrEquals(CountColumns.COUNT_YEAR, value);
        return this;
    }

    public CountSelection countYearLt(int value) {
        addLessThan(CountColumns.COUNT_YEAR, value);
        return this;
    }

    public CountSelection countYearLtEq(int value) {
        addLessThanOrEquals(CountColumns.COUNT_YEAR, value);
        return this;
    }

    public CountSelection orderByCountYear(boolean desc) {
        orderBy(CountColumns.COUNT_YEAR, desc);
        return this;
    }

    public CountSelection orderByCountYear() {
        orderBy(CountColumns.COUNT_YEAR, false);
        return this;
    }

    public CountSelection countMonth(Integer... value) {
        addEquals(CountColumns.COUNT_MONTH, value);
        return this;
    }

    public CountSelection countMonthNot(Integer... value) {
        addNotEquals(CountColumns.COUNT_MONTH, value);
        return this;
    }

    public CountSelection countMonthGt(int value) {
        addGreaterThan(CountColumns.COUNT_MONTH, value);
        return this;
    }

    public CountSelection countMonthGtEq(int value) {
        addGreaterThanOrEquals(CountColumns.COUNT_MONTH, value);
        return this;
    }

    public CountSelection countMonthLt(int value) {
        addLessThan(CountColumns.COUNT_MONTH, value);
        return this;
    }

    public CountSelection countMonthLtEq(int value) {
        addLessThanOrEquals(CountColumns.COUNT_MONTH, value);
        return this;
    }

    public CountSelection orderByCountMonth(boolean desc) {
        orderBy(CountColumns.COUNT_MONTH, desc);
        return this;
    }

    public CountSelection orderByCountMonth() {
        orderBy(CountColumns.COUNT_MONTH, false);
        return this;
    }

    public CountSelection countWeek(Integer... value) {
        addEquals(CountColumns.COUNT_WEEK, value);
        return this;
    }

    public CountSelection countWeekNot(Integer... value) {
        addNotEquals(CountColumns.COUNT_WEEK, value);
        return this;
    }

    public CountSelection countWeekGt(int value) {
        addGreaterThan(CountColumns.COUNT_WEEK, value);
        return this;
    }

    public CountSelection countWeekGtEq(int value) {
        addGreaterThanOrEquals(CountColumns.COUNT_WEEK, value);
        return this;
    }

    public CountSelection countWeekLt(int value) {
        addLessThan(CountColumns.COUNT_WEEK, value);
        return this;
    }

    public CountSelection countWeekLtEq(int value) {
        addLessThanOrEquals(CountColumns.COUNT_WEEK, value);
        return this;
    }

    public CountSelection orderByCountWeek(boolean desc) {
        orderBy(CountColumns.COUNT_WEEK, desc);
        return this;
    }

    public CountSelection orderByCountWeek() {
        orderBy(CountColumns.COUNT_WEEK, false);
        return this;
    }

    public CountSelection countDay(Integer... value) {
        addEquals(CountColumns.COUNT_DAY, value);
        return this;
    }

    public CountSelection countDayNot(Integer... value) {
        addNotEquals(CountColumns.COUNT_DAY, value);
        return this;
    }

    public CountSelection countDayGt(int value) {
        addGreaterThan(CountColumns.COUNT_DAY, value);
        return this;
    }

    public CountSelection countDayGtEq(int value) {
        addGreaterThanOrEquals(CountColumns.COUNT_DAY, value);
        return this;
    }

    public CountSelection countDayLt(int value) {
        addLessThan(CountColumns.COUNT_DAY, value);
        return this;
    }

    public CountSelection countDayLtEq(int value) {
        addLessThanOrEquals(CountColumns.COUNT_DAY, value);
        return this;
    }

    public CountSelection orderByCountDay(boolean desc) {
        orderBy(CountColumns.COUNT_DAY, desc);
        return this;
    }

    public CountSelection orderByCountDay() {
        orderBy(CountColumns.COUNT_DAY, false);
        return this;
    }
}
