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

package com.adkdevelopment.earthquakesurvival;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adkdevelopment.earthquakesurvival.earthquake_objects.Feature;
import com.adkdevelopment.earthquakesurvival.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.ui.CursorRecyclerViewAdapter;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;
import com.google.android.gms.maps.model.LatLng;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Creates RecyclerView adapter which populates task items in MainFragment
 * Each element has an onClickListener
 */
public class RecentAdapter extends CursorRecyclerViewAdapter<RecentAdapter.ViewHolder> {

    private final Activity mContext;

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @Bind(R.id.earthquake_item_place) TextView mEarthquakePlace;
        @Bind(R.id.earthquake_item_magnitude) TextView mEarthquakeMagnitude;
        @Bind(R.id.earthquake_item_date) TextView mEarthquakeDate;
        @Bind(R.id.earthquake_item_click) RelativeLayout mEarthquakeClick;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        String link = cursor.getString(cursor.getColumnIndex(EarthquakeColumns.URL));
        String place = cursor.getString(cursor.getColumnIndex(EarthquakeColumns.PLACE));
        Long dateMillis = cursor.getLong(cursor.getColumnIndex(EarthquakeColumns.TIME));
        Double magnitude = cursor.getDouble(cursor.getColumnIndex(EarthquakeColumns.MAG));
        Double latitude = cursor.getDouble(cursor.getColumnIndex(EarthquakeColumns.LATITUDE));
        Double longitude = cursor.getDouble(cursor.getColumnIndex(EarthquakeColumns.LONGITUDE));

        LatLng latLng = new LatLng(latitude, longitude);

        viewHolder.mEarthquakePlace.setText(place);
        viewHolder.mEarthquakeDate.setText(Utilities.getNiceDate(dateMillis));
        viewHolder.mEarthquakeMagnitude.setText(mContext.getString(R.string.earthquake_magnitude, magnitude));

        viewHolder.mEarthquakeClick.setOnClickListener(click -> {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(Feature.MAGNITUDE, magnitude);
            intent.putExtra(Feature.PLACE, place);
            intent.putExtra(Feature.DATE, Utilities.getNiceDate(dateMillis));
            intent.putExtra(Feature.LINK, link);
            intent.putExtra(Feature.LATLNG, latLng);

            // Check if a phone supports shared transitions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //noinspection unchecked always true
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                        mContext,
                        Pair.create(viewHolder.itemView.findViewById(R.id.earthquake_item_place),
                                viewHolder.itemView.findViewById(R.id.earthquake_item_place).getTransitionName()),
                        Pair.create(viewHolder.itemView.findViewById(R.id.earthquake_item_date),
                                viewHolder.itemView.findViewById(R.id.earthquake_item_date).getTransitionName()),
                        Pair.create(viewHolder.itemView.findViewById(R.id.earthquake_item_magnitude),
                                viewHolder.itemView.findViewById(R.id.earthquake_item_magnitude).getTransitionName()))
                        .toBundle();
                mContext.startActivity(intent, bundle);
            } else {
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_earthquake_item, parent, false);

        return new ViewHolder(v);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecentAdapter(Activity context, Cursor c) {
        super(c);
        mContext = context;
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