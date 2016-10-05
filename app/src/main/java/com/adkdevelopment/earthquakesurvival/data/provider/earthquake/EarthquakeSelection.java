package com.adkdevelopment.earthquakesurvival.data.provider.earthquake;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.adkdevelopment.earthquakesurvival.data.provider.base.AbstractSelection;

/**
 * Selection for the {@code earthquake} table.
 */
public class EarthquakeSelection extends AbstractSelection<EarthquakeSelection> {
    @Override
    protected Uri baseUri() {
        return EarthquakeColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code EarthquakeCursor} object, which is positioned before the first entry, or null.
     */
    public EarthquakeCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new EarthquakeCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public EarthquakeCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code EarthquakeCursor} object, which is positioned before the first entry, or null.
     */
    public EarthquakeCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new EarthquakeCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public EarthquakeCursor query(Context context) {
        return query(context, null);
    }


    public EarthquakeSelection id(long... value) {
        addEquals("earthquake." + EarthquakeColumns._ID, toObjectArray(value));
        return this;
    }

    public EarthquakeSelection idNot(long... value) {
        addNotEquals("earthquake." + EarthquakeColumns._ID, toObjectArray(value));
        return this;
    }

    public EarthquakeSelection orderById(boolean desc) {
        orderBy("earthquake." + EarthquakeColumns._ID, desc);
        return this;
    }

    public EarthquakeSelection orderById() {
        return orderById(false);
    }

    public EarthquakeSelection idEarth(String... value) {
        addEquals(EarthquakeColumns.ID_EARTH, value);
        return this;
    }

    public EarthquakeSelection idEarthNot(String... value) {
        addNotEquals(EarthquakeColumns.ID_EARTH, value);
        return this;
    }

    public EarthquakeSelection idEarthLike(String... value) {
        addLike(EarthquakeColumns.ID_EARTH, value);
        return this;
    }

    public EarthquakeSelection idEarthContains(String... value) {
        addContains(EarthquakeColumns.ID_EARTH, value);
        return this;
    }

    public EarthquakeSelection idEarthStartsWith(String... value) {
        addStartsWith(EarthquakeColumns.ID_EARTH, value);
        return this;
    }

    public EarthquakeSelection idEarthEndsWith(String... value) {
        addEndsWith(EarthquakeColumns.ID_EARTH, value);
        return this;
    }

    public EarthquakeSelection orderByIdEarth(boolean desc) {
        orderBy(EarthquakeColumns.ID_EARTH, desc);
        return this;
    }

    public EarthquakeSelection orderByIdEarth() {
        orderBy(EarthquakeColumns.ID_EARTH, false);
        return this;
    }

    public EarthquakeSelection place(String... value) {
        addEquals(EarthquakeColumns.PLACE, value);
        return this;
    }

    public EarthquakeSelection placeNot(String... value) {
        addNotEquals(EarthquakeColumns.PLACE, value);
        return this;
    }

    public EarthquakeSelection placeLike(String... value) {
        addLike(EarthquakeColumns.PLACE, value);
        return this;
    }

    public EarthquakeSelection placeContains(String... value) {
        addContains(EarthquakeColumns.PLACE, value);
        return this;
    }

    public EarthquakeSelection placeStartsWith(String... value) {
        addStartsWith(EarthquakeColumns.PLACE, value);
        return this;
    }

    public EarthquakeSelection placeEndsWith(String... value) {
        addEndsWith(EarthquakeColumns.PLACE, value);
        return this;
    }

    public EarthquakeSelection orderByPlace(boolean desc) {
        orderBy(EarthquakeColumns.PLACE, desc);
        return this;
    }

    public EarthquakeSelection orderByPlace() {
        orderBy(EarthquakeColumns.PLACE, false);
        return this;
    }

    public EarthquakeSelection mag(Double... value) {
        addEquals(EarthquakeColumns.MAG, value);
        return this;
    }

    public EarthquakeSelection magNot(Double... value) {
        addNotEquals(EarthquakeColumns.MAG, value);
        return this;
    }

    public EarthquakeSelection magGt(double value) {
        addGreaterThan(EarthquakeColumns.MAG, value);
        return this;
    }

    public EarthquakeSelection magGtEq(double value) {
        addGreaterThanOrEquals(EarthquakeColumns.MAG, value);
        return this;
    }

    public EarthquakeSelection magLt(double value) {
        addLessThan(EarthquakeColumns.MAG, value);
        return this;
    }

    public EarthquakeSelection magLtEq(double value) {
        addLessThanOrEquals(EarthquakeColumns.MAG, value);
        return this;
    }

    public EarthquakeSelection orderByMag(boolean desc) {
        orderBy(EarthquakeColumns.MAG, desc);
        return this;
    }

    public EarthquakeSelection orderByMag() {
        orderBy(EarthquakeColumns.MAG, false);
        return this;
    }

    public EarthquakeSelection type(String... value) {
        addEquals(EarthquakeColumns.TYPE, value);
        return this;
    }

    public EarthquakeSelection typeNot(String... value) {
        addNotEquals(EarthquakeColumns.TYPE, value);
        return this;
    }

    public EarthquakeSelection typeLike(String... value) {
        addLike(EarthquakeColumns.TYPE, value);
        return this;
    }

    public EarthquakeSelection typeContains(String... value) {
        addContains(EarthquakeColumns.TYPE, value);
        return this;
    }

    public EarthquakeSelection typeStartsWith(String... value) {
        addStartsWith(EarthquakeColumns.TYPE, value);
        return this;
    }

    public EarthquakeSelection typeEndsWith(String... value) {
        addEndsWith(EarthquakeColumns.TYPE, value);
        return this;
    }

    public EarthquakeSelection orderByType(boolean desc) {
        orderBy(EarthquakeColumns.TYPE, desc);
        return this;
    }

    public EarthquakeSelection orderByType() {
        orderBy(EarthquakeColumns.TYPE, false);
        return this;
    }

    public EarthquakeSelection alert(String... value) {
        addEquals(EarthquakeColumns.ALERT, value);
        return this;
    }

    public EarthquakeSelection alertNot(String... value) {
        addNotEquals(EarthquakeColumns.ALERT, value);
        return this;
    }

    public EarthquakeSelection alertLike(String... value) {
        addLike(EarthquakeColumns.ALERT, value);
        return this;
    }

    public EarthquakeSelection alertContains(String... value) {
        addContains(EarthquakeColumns.ALERT, value);
        return this;
    }

    public EarthquakeSelection alertStartsWith(String... value) {
        addStartsWith(EarthquakeColumns.ALERT, value);
        return this;
    }

    public EarthquakeSelection alertEndsWith(String... value) {
        addEndsWith(EarthquakeColumns.ALERT, value);
        return this;
    }

    public EarthquakeSelection orderByAlert(boolean desc) {
        orderBy(EarthquakeColumns.ALERT, desc);
        return this;
    }

    public EarthquakeSelection orderByAlert() {
        orderBy(EarthquakeColumns.ALERT, false);
        return this;
    }

    public EarthquakeSelection time(long... value) {
        addEquals(EarthquakeColumns.TIME, toObjectArray(value));
        return this;
    }

    public EarthquakeSelection timeNot(long... value) {
        addNotEquals(EarthquakeColumns.TIME, toObjectArray(value));
        return this;
    }

    public EarthquakeSelection timeGt(long value) {
        addGreaterThan(EarthquakeColumns.TIME, value);
        return this;
    }

    public EarthquakeSelection timeGtEq(long value) {
        addGreaterThanOrEquals(EarthquakeColumns.TIME, value);
        return this;
    }

    public EarthquakeSelection timeLt(long value) {
        addLessThan(EarthquakeColumns.TIME, value);
        return this;
    }

    public EarthquakeSelection timeLtEq(long value) {
        addLessThanOrEquals(EarthquakeColumns.TIME, value);
        return this;
    }

    public EarthquakeSelection orderByTime(boolean desc) {
        orderBy(EarthquakeColumns.TIME, desc);
        return this;
    }

    public EarthquakeSelection orderByTime() {
        orderBy(EarthquakeColumns.TIME, false);
        return this;
    }

    public EarthquakeSelection url(String... value) {
        addEquals(EarthquakeColumns.URL, value);
        return this;
    }

    public EarthquakeSelection urlNot(String... value) {
        addNotEquals(EarthquakeColumns.URL, value);
        return this;
    }

    public EarthquakeSelection urlLike(String... value) {
        addLike(EarthquakeColumns.URL, value);
        return this;
    }

    public EarthquakeSelection urlContains(String... value) {
        addContains(EarthquakeColumns.URL, value);
        return this;
    }

    public EarthquakeSelection urlStartsWith(String... value) {
        addStartsWith(EarthquakeColumns.URL, value);
        return this;
    }

    public EarthquakeSelection urlEndsWith(String... value) {
        addEndsWith(EarthquakeColumns.URL, value);
        return this;
    }

    public EarthquakeSelection orderByUrl(boolean desc) {
        orderBy(EarthquakeColumns.URL, desc);
        return this;
    }

    public EarthquakeSelection orderByUrl() {
        orderBy(EarthquakeColumns.URL, false);
        return this;
    }

    public EarthquakeSelection detail(String... value) {
        addEquals(EarthquakeColumns.DETAIL, value);
        return this;
    }

    public EarthquakeSelection detailNot(String... value) {
        addNotEquals(EarthquakeColumns.DETAIL, value);
        return this;
    }

    public EarthquakeSelection detailLike(String... value) {
        addLike(EarthquakeColumns.DETAIL, value);
        return this;
    }

    public EarthquakeSelection detailContains(String... value) {
        addContains(EarthquakeColumns.DETAIL, value);
        return this;
    }

    public EarthquakeSelection detailStartsWith(String... value) {
        addStartsWith(EarthquakeColumns.DETAIL, value);
        return this;
    }

    public EarthquakeSelection detailEndsWith(String... value) {
        addEndsWith(EarthquakeColumns.DETAIL, value);
        return this;
    }

    public EarthquakeSelection orderByDetail(boolean desc) {
        orderBy(EarthquakeColumns.DETAIL, desc);
        return this;
    }

    public EarthquakeSelection orderByDetail() {
        orderBy(EarthquakeColumns.DETAIL, false);
        return this;
    }

    public EarthquakeSelection depth(Double... value) {
        addEquals(EarthquakeColumns.DEPTH, value);
        return this;
    }

    public EarthquakeSelection depthNot(Double... value) {
        addNotEquals(EarthquakeColumns.DEPTH, value);
        return this;
    }

    public EarthquakeSelection depthGt(double value) {
        addGreaterThan(EarthquakeColumns.DEPTH, value);
        return this;
    }

    public EarthquakeSelection depthGtEq(double value) {
        addGreaterThanOrEquals(EarthquakeColumns.DEPTH, value);
        return this;
    }

    public EarthquakeSelection depthLt(double value) {
        addLessThan(EarthquakeColumns.DEPTH, value);
        return this;
    }

    public EarthquakeSelection depthLtEq(double value) {
        addLessThanOrEquals(EarthquakeColumns.DEPTH, value);
        return this;
    }

    public EarthquakeSelection orderByDepth(boolean desc) {
        orderBy(EarthquakeColumns.DEPTH, desc);
        return this;
    }

    public EarthquakeSelection orderByDepth() {
        orderBy(EarthquakeColumns.DEPTH, false);
        return this;
    }

    public EarthquakeSelection longitude(Double... value) {
        addEquals(EarthquakeColumns.LONGITUDE, value);
        return this;
    }

    public EarthquakeSelection longitudeNot(Double... value) {
        addNotEquals(EarthquakeColumns.LONGITUDE, value);
        return this;
    }

    public EarthquakeSelection longitudeGt(double value) {
        addGreaterThan(EarthquakeColumns.LONGITUDE, value);
        return this;
    }

    public EarthquakeSelection longitudeGtEq(double value) {
        addGreaterThanOrEquals(EarthquakeColumns.LONGITUDE, value);
        return this;
    }

    public EarthquakeSelection longitudeLt(double value) {
        addLessThan(EarthquakeColumns.LONGITUDE, value);
        return this;
    }

    public EarthquakeSelection longitudeLtEq(double value) {
        addLessThanOrEquals(EarthquakeColumns.LONGITUDE, value);
        return this;
    }

    public EarthquakeSelection orderByLongitude(boolean desc) {
        orderBy(EarthquakeColumns.LONGITUDE, desc);
        return this;
    }

    public EarthquakeSelection orderByLongitude() {
        orderBy(EarthquakeColumns.LONGITUDE, false);
        return this;
    }

    public EarthquakeSelection latitude(Double... value) {
        addEquals(EarthquakeColumns.LATITUDE, value);
        return this;
    }

    public EarthquakeSelection latitudeNot(Double... value) {
        addNotEquals(EarthquakeColumns.LATITUDE, value);
        return this;
    }

    public EarthquakeSelection latitudeGt(double value) {
        addGreaterThan(EarthquakeColumns.LATITUDE, value);
        return this;
    }

    public EarthquakeSelection latitudeGtEq(double value) {
        addGreaterThanOrEquals(EarthquakeColumns.LATITUDE, value);
        return this;
    }

    public EarthquakeSelection latitudeLt(double value) {
        addLessThan(EarthquakeColumns.LATITUDE, value);
        return this;
    }

    public EarthquakeSelection latitudeLtEq(double value) {
        addLessThanOrEquals(EarthquakeColumns.LATITUDE, value);
        return this;
    }

    public EarthquakeSelection orderByLatitude(boolean desc) {
        orderBy(EarthquakeColumns.LATITUDE, desc);
        return this;
    }

    public EarthquakeSelection orderByLatitude() {
        orderBy(EarthquakeColumns.LATITUDE, false);
        return this;
    }
}
