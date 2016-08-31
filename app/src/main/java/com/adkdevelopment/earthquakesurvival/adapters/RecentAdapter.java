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

package com.adkdevelopment.earthquakesurvival.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adkdevelopment.earthquakesurvival.App;
import com.adkdevelopment.earthquakesurvival.DetailActivity;
import com.adkdevelopment.earthquakesurvival.R;
import com.adkdevelopment.earthquakesurvival.eventbus.RxBus;
import com.adkdevelopment.earthquakesurvival.objects.earthquake.Feature;
import com.adkdevelopment.earthquakesurvival.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.ui.CursorRecyclerViewAdapter;
import com.adkdevelopment.earthquakesurvival.utils.LocationUtils;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Creates RecyclerView adapter which populates task items in MainFragment
 * Each element has an onClickListener
 */
public class RecentAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private final Context mContext;

    private RxBus _rxBus;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.earthquake_item_place) TextView mEarthquakePlace;
        @BindView(R.id.earthquake_item_magnitude) TextView mEarthquakeMagnitude;
        @BindView(R.id.earthquake_item_date) TextView mEarthquakeDate;
        @BindView(R.id.earthquake_item_distance) TextView mEarthquakeDistance;
        @BindView(R.id.earthquake_item_depth) TextView mEarthquakeDepth;
        @BindView(R.id.earthquake_item_click) RelativeLayout mEarthquakeClick;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecentAdapter(Context context, Cursor c) {
        super(c);
        mContext = context;
        _rxBus = App.getRxBusSingleton();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_earthquake_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {

        String link = cursor.getString(cursor.getColumnIndex(EarthquakeColumns.URL));
        String place = cursor.getString(cursor.getColumnIndex(EarthquakeColumns.PLACE));
        long dateMillis = cursor.getLong(cursor.getColumnIndex(EarthquakeColumns.TIME));
        double magnitude = cursor.getDouble(cursor.getColumnIndex(EarthquakeColumns.MAG));
        double latitude = cursor.getDouble(cursor.getColumnIndex(EarthquakeColumns.LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndex(EarthquakeColumns.LONGITUDE));
        double depth = cursor.getDouble(cursor.getColumnIndex(EarthquakeColumns.DEPTH)) / 1.6;

        LatLng latLng = new LatLng(latitude, longitude);

        String distance = mContext.getString(R.string.earthquake_distance,
                LocationUtils.getDistance(latLng, LocationUtils.getLocation(mContext)));

        ((ViewHolder) viewHolder).mEarthquakePlace.setText(place);
        ((ViewHolder) viewHolder).mEarthquakeDate.setText(Utilities.getRelativeDate(dateMillis));
        ((ViewHolder) viewHolder).mEarthquakeMagnitude.setText(mContext.getString(R.string.earthquake_magnitude, magnitude));
        ((ViewHolder) viewHolder).mEarthquakeDistance.setText(distance);
        ((ViewHolder) viewHolder).mEarthquakeDepth.setText(mContext.getString(R.string.earthquake_depth, depth));

        ((ViewHolder) viewHolder).mEarthquakeClick.setOnClickListener(click -> {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(Feature.MAGNITUDE, magnitude);
            intent.putExtra(Feature.PLACE, place);
            intent.putExtra(Feature.DATE, Utilities.getRelativeDate(dateMillis));
            intent.putExtra(Feature.LINK, link);
            intent.putExtra(Feature.LATLNG, latLng);
            intent.putExtra(Feature.DISTANCE, distance);
            intent.putExtra(Feature.DEPTH, depth);

            if (_rxBus.hasObservers()) {
                // Check if a phone supports shared transitions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    Utilities.animationCard(viewHolder);

                    Pair pair = Pair.create(viewHolder.itemView.findViewById(R.id.survive_card_image),
                            viewHolder.itemView.findViewById(R.id.survive_card_image).getTransitionName());
                    _rxBus.send(Pair.create(intent, pair));
                } else {
                    _rxBus.send(intent);
                }

            }
        });
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

}