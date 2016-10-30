/*
 * MIT License
 *
 * Copyright (c) 2016. Dmytro Karataiev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.adkdevelopment.earthquakesurvival.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adkdevelopment.earthquakesurvival.App;
import com.adkdevelopment.earthquakesurvival.R;
import com.adkdevelopment.earthquakesurvival.data.objects.earthquake.Feature;
import com.adkdevelopment.earthquakesurvival.data.provider.count.CountColumns;
import com.adkdevelopment.earthquakesurvival.data.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.data.provider.news.NewsColumns;
import com.adkdevelopment.earthquakesurvival.eventbus.RxBus;
import com.adkdevelopment.earthquakesurvival.ui.DetailActivity;
import com.adkdevelopment.earthquakesurvival.utils.LocationUtils;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Creates RecyclerView adapter which populates task items in MainFragment
 * Each element has an onClickListener
 */
public class NewsAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private final Context mContext;

    private RxBus _rxBus;

    private static final int VIEW_STAT = 0;
    private static final int VIEW_LARGEST = 1;
    private static final int VIEW_CLOSEST = 2;
    private static final int VIEW_NEWS = 3;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.news_item_title) TextView mTitle;
        @BindView(R.id.news_item_date) TextView mDate;
        @BindView(R.id.news_item_description) TextView mDescription;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public static class ViewHolderStats extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.stat_item_year) TextView mStatYear;
        @BindView(R.id.stat_item_month) TextView mStatMonth;
        @BindView(R.id.stat_item_week) TextView mStatWeek;
        @BindView(R.id.stat_item_day) TextView mStatDay;

        public ViewHolderStats(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public static class ViewHolderLargest extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.stat_item_largest) TextView mStatLargest;
        @BindView(R.id.stat_item_magnitude) TextView mStatMagnitude;
        @BindView(R.id.stat_item_description) TextView mStatDescription;
        @BindView(R.id.stat_item_date) TextView mStatDate;
        @BindView(R.id.stat_item_distance) TextView mStatDistance;
        @BindView(R.id.stat_item_depth) TextView mStatDepth;
        @BindView(R.id.earthquake_item_click) CardView mEarthquakeClick;

        public ViewHolderLargest(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(Context context, Cursor cursor) {
        super(cursor);
        mContext = context;
        _rxBus = App.getRxBusSingleton();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        switch (viewType) {
            case VIEW_STAT:
                v = LayoutInflater.from(mContext)
                        .inflate(R.layout.news_statistics, parent, false);

                return new ViewHolderStats(v);
            case VIEW_LARGEST:
                v = LayoutInflater.from(mContext)
                        .inflate(R.layout.news_largest, parent, false);

                return new ViewHolderLargest(v);
            case VIEW_CLOSEST:
                v = LayoutInflater.from(mContext)
                        .inflate(R.layout.news_largest, parent, false);

                return new ViewHolderLargest(v);
            case VIEW_NEWS:
            default:
                v = LayoutInflater.from(mContext)
                        .inflate(R.layout.news_item, parent, false);

                return new ViewHolder(v);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {

        switch (holder.getItemViewType()) {
            case VIEW_STAT:
                Cursor tempCursor = mContext.getContentResolver()
                        .query(CountColumns.CONTENT_URI,
                                null,
                                null,
                                null,
                                CountColumns._ID + " LIMIT 1");

                if (tempCursor != null && tempCursor.getCount() > 0) {

                    tempCursor.moveToFirst();

                    ((ViewHolderStats) holder).mStatYear.setText(mContext.getString(R.string.earthquake_statistics_year,
                            tempCursor.getInt(tempCursor.getColumnIndex(CountColumns.COUNT_YEAR))));
                    ((ViewHolderStats) holder).mStatMonth.setText(mContext.getString(R.string.earthquake_statistics_month,
                            tempCursor.getInt(tempCursor.getColumnIndex(CountColumns.COUNT_MONTH))));
                    ((ViewHolderStats) holder).mStatWeek.setText(mContext.getString(R.string.earthquake_statistics_week,
                            tempCursor.getInt(tempCursor.getColumnIndex(CountColumns.COUNT_WEEK))));
                    ((ViewHolderStats) holder).mStatDay.setText(mContext.getString(R.string.earthquake_statistics_day,
                            tempCursor.getInt(tempCursor.getColumnIndex(CountColumns.COUNT_DAY))));

                    tempCursor.close();
                }
                break;
            case VIEW_LARGEST:
                tempCursor = mContext.getContentResolver().query(EarthquakeColumns.CONTENT_URI,
                            null,
                            null,
                            null,
                            EarthquakeColumns.MAG + " DESC LIMIT 1");

                    if (tempCursor != null) {
                        populateView(holder, tempCursor, 0);
                    }
                break;
            case VIEW_CLOSEST:
                tempCursor = mContext.getContentResolver().query(EarthquakeColumns.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

                if (tempCursor != null) {
                    int position = 0;
                    double distanceClosest = 99999.9;

                    while (tempCursor.moveToNext()) {
                        double latitude = tempCursor.getDouble(tempCursor.getColumnIndex(EarthquakeColumns.LATITUDE));
                        double longitude = tempCursor.getDouble(tempCursor.getColumnIndex(EarthquakeColumns.LONGITUDE));
                        LatLng latLng = new LatLng(latitude, longitude);

                        double currentDistance = LocationUtils.getDistance(latLng, LocationUtils.getLocation(mContext));

                        if (currentDistance < distanceClosest) {
                            distanceClosest = currentDistance;
                            position = tempCursor.getPosition();
                        }
                    }

                    ((ViewHolderLargest) holder).mStatLargest.setText(mContext.getString(R.string.earthquake_statistics_closest));

                    populateView(holder, tempCursor, position);

                }
                break;
            case VIEW_NEWS:
            default:
                Long dateMillis = cursor.getLong(cursor.getColumnIndex(NewsColumns.DATE));
                ((ViewHolder) holder).mDate.setText(Utilities.getRelativeDate(dateMillis));

                String title = cursor.getString(cursor.getColumnIndex(NewsColumns.TITLE));
                String link = cursor.getString(cursor.getColumnIndex(NewsColumns.URL));
                final Uri uriLink = Utilities.getProperUri(link);
                link = mContext.getString(R.string.earthquake_link, link);

                ((ViewHolder) holder).mDescription.setText(Utilities.getHtmlText(link));
                ((ViewHolder) holder).mDescription.setMovementMethod(LinkMovementMethod.getInstance());
                ((ViewHolder) holder).mTitle.setText(title);

                ((ViewHolder) holder).itemView.setOnClickListener(click -> {
                    if (uriLink != null) {
                        Utilities.animationCard(holder);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(uriLink.toString()));
                        mContext.startActivity(intent);
                    }
                });
                break;
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        if (getCursor() != null) {
            return getCursor().getCount();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * Populates similar cards with earthquake information
     * @param holder which is being populated with the data
     * @param tempCursor where data is taken from
     * @param position position of a cursor with correct data
     */
    private void populateView(RecyclerView.ViewHolder holder, Cursor tempCursor, int position) {

        if (tempCursor.getCount() > 0) {
            tempCursor.moveToPosition(position);

            double latitude = tempCursor.getDouble(tempCursor.getColumnIndex(EarthquakeColumns.LATITUDE));
            double longitude = tempCursor.getDouble(tempCursor.getColumnIndex(EarthquakeColumns.LONGITUDE));
            LatLng latLng = new LatLng(latitude, longitude);

            final String desc = Utilities.formatEarthquakePlace(tempCursor
                    .getString(tempCursor.getColumnIndex(EarthquakeColumns.PLACE)));
            ((ViewHolderLargest) holder).mStatDescription.setText(desc);

            double magnitude = tempCursor.getDouble(tempCursor.getColumnIndex(EarthquakeColumns.MAG));
            ((ViewHolderLargest) holder).mStatMagnitude.setText(mContext.getString(R.string.earthquake_magnitude, magnitude));

            long dateMillis = tempCursor.getLong(tempCursor.getColumnIndex(EarthquakeColumns.TIME));
            ((ViewHolderLargest) holder).mStatDate.setText(Utilities.getRelativeDate(dateMillis));

            String link = tempCursor.getString(tempCursor.getColumnIndex(EarthquakeColumns.URL));

            String distance = mContext.getString(R.string.earthquake_distance,
                    LocationUtils.getDistance(latLng, LocationUtils.getLocation(mContext)));
            ((ViewHolderLargest) holder).mStatDistance.setText(distance);

            double depth = tempCursor.getDouble(tempCursor.getColumnIndex(EarthquakeColumns.DEPTH)) / 1.6;
            ((ViewHolderLargest) holder).mStatDepth.setText(mContext.getString(R.string.earthquake_depth, depth));

            tempCursor.close();

            ((ViewHolderLargest) holder).mEarthquakeClick.setOnClickListener(click -> {

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(Feature.MAGNITUDE, magnitude);
                intent.putExtra(Feature.PLACE, desc);
                intent.putExtra(Feature.DATE, Utilities.getRelativeDate(dateMillis));
                intent.putExtra(Feature.LINK, link);
                intent.putExtra(Feature.LATLNG, latLng);
                intent.putExtra(Feature.DISTANCE, distance);
                intent.putExtra(Feature.DEPTH, depth);

                if (_rxBus.hasObservers()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        Utilities.animationCard(holder);
                        //noinspection unchecked always true
                        Pair pair = Pair.create(((ViewHolderLargest) holder).itemView.findViewById(R.id.earthquake_item_click),
                                ((ViewHolderLargest) holder).itemView.findViewById(R.id.earthquake_item_click).getTransitionName());
                        _rxBus.send(Pair.create(intent, pair));
                    } else {
                        _rxBus.send(intent);
                    }
                }
            });
        }
    }


}