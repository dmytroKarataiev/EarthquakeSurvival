package com.adkdevelopment.earthquakesurvival.provider.news;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.adkdevelopment.earthquakesurvival.provider.base.AbstractSelection;

/**
 * Selection for the {@code news} table.
 */
public class NewsSelection extends AbstractSelection<NewsSelection> {
    @Override
    protected Uri baseUri() {
        return NewsColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code NewsCursor} object, which is positioned before the first entry, or null.
     */
    public NewsCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new NewsCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public NewsCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code NewsCursor} object, which is positioned before the first entry, or null.
     */
    public NewsCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new NewsCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public NewsCursor query(Context context) {
        return query(context, null);
    }


    public NewsSelection id(long... value) {
        addEquals("news." + NewsColumns._ID, toObjectArray(value));
        return this;
    }

    public NewsSelection idNot(long... value) {
        addNotEquals("news." + NewsColumns._ID, toObjectArray(value));
        return this;
    }

    public NewsSelection orderById(boolean desc) {
        orderBy("news." + NewsColumns._ID, desc);
        return this;
    }

    public NewsSelection orderById() {
        return orderById(false);
    }

    public NewsSelection date(String... value) {
        addEquals(NewsColumns.DATE, value);
        return this;
    }

    public NewsSelection dateNot(String... value) {
        addNotEquals(NewsColumns.DATE, value);
        return this;
    }

    public NewsSelection dateLike(String... value) {
        addLike(NewsColumns.DATE, value);
        return this;
    }

    public NewsSelection dateContains(String... value) {
        addContains(NewsColumns.DATE, value);
        return this;
    }

    public NewsSelection dateStartsWith(String... value) {
        addStartsWith(NewsColumns.DATE, value);
        return this;
    }

    public NewsSelection dateEndsWith(String... value) {
        addEndsWith(NewsColumns.DATE, value);
        return this;
    }

    public NewsSelection orderByDate(boolean desc) {
        orderBy(NewsColumns.DATE, desc);
        return this;
    }

    public NewsSelection orderByDate() {
        orderBy(NewsColumns.DATE, false);
        return this;
    }

    public NewsSelection title(String... value) {
        addEquals(NewsColumns.TITLE, value);
        return this;
    }

    public NewsSelection titleNot(String... value) {
        addNotEquals(NewsColumns.TITLE, value);
        return this;
    }

    public NewsSelection titleLike(String... value) {
        addLike(NewsColumns.TITLE, value);
        return this;
    }

    public NewsSelection titleContains(String... value) {
        addContains(NewsColumns.TITLE, value);
        return this;
    }

    public NewsSelection titleStartsWith(String... value) {
        addStartsWith(NewsColumns.TITLE, value);
        return this;
    }

    public NewsSelection titleEndsWith(String... value) {
        addEndsWith(NewsColumns.TITLE, value);
        return this;
    }

    public NewsSelection orderByTitle(boolean desc) {
        orderBy(NewsColumns.TITLE, desc);
        return this;
    }

    public NewsSelection orderByTitle() {
        orderBy(NewsColumns.TITLE, false);
        return this;
    }

    public NewsSelection description(String... value) {
        addEquals(NewsColumns.DESCRIPTION, value);
        return this;
    }

    public NewsSelection descriptionNot(String... value) {
        addNotEquals(NewsColumns.DESCRIPTION, value);
        return this;
    }

    public NewsSelection descriptionLike(String... value) {
        addLike(NewsColumns.DESCRIPTION, value);
        return this;
    }

    public NewsSelection descriptionContains(String... value) {
        addContains(NewsColumns.DESCRIPTION, value);
        return this;
    }

    public NewsSelection descriptionStartsWith(String... value) {
        addStartsWith(NewsColumns.DESCRIPTION, value);
        return this;
    }

    public NewsSelection descriptionEndsWith(String... value) {
        addEndsWith(NewsColumns.DESCRIPTION, value);
        return this;
    }

    public NewsSelection orderByDescription(boolean desc) {
        orderBy(NewsColumns.DESCRIPTION, desc);
        return this;
    }

    public NewsSelection orderByDescription() {
        orderBy(NewsColumns.DESCRIPTION, false);
        return this;
    }

    public NewsSelection url(String... value) {
        addEquals(NewsColumns.URL, value);
        return this;
    }

    public NewsSelection urlNot(String... value) {
        addNotEquals(NewsColumns.URL, value);
        return this;
    }

    public NewsSelection urlLike(String... value) {
        addLike(NewsColumns.URL, value);
        return this;
    }

    public NewsSelection urlContains(String... value) {
        addContains(NewsColumns.URL, value);
        return this;
    }

    public NewsSelection urlStartsWith(String... value) {
        addStartsWith(NewsColumns.URL, value);
        return this;
    }

    public NewsSelection urlEndsWith(String... value) {
        addEndsWith(NewsColumns.URL, value);
        return this;
    }

    public NewsSelection orderByUrl(boolean desc) {
        orderBy(NewsColumns.URL, desc);
        return this;
    }

    public NewsSelection orderByUrl() {
        orderBy(NewsColumns.URL, false);
        return this;
    }

    public NewsSelection guid(String... value) {
        addEquals(NewsColumns.GUID, value);
        return this;
    }

    public NewsSelection guidNot(String... value) {
        addNotEquals(NewsColumns.GUID, value);
        return this;
    }

    public NewsSelection guidLike(String... value) {
        addLike(NewsColumns.GUID, value);
        return this;
    }

    public NewsSelection guidContains(String... value) {
        addContains(NewsColumns.GUID, value);
        return this;
    }

    public NewsSelection guidStartsWith(String... value) {
        addStartsWith(NewsColumns.GUID, value);
        return this;
    }

    public NewsSelection guidEndsWith(String... value) {
        addEndsWith(NewsColumns.GUID, value);
        return this;
    }

    public NewsSelection orderByGuid(boolean desc) {
        orderBy(NewsColumns.GUID, desc);
        return this;
    }

    public NewsSelection orderByGuid() {
        orderBy(NewsColumns.GUID, false);
        return this;
    }
}
