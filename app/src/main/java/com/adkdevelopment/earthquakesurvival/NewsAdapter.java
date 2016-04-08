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

/**
 * Simple RecyclerView adapter with OnClickListener on each element
 * Created by karataev on 3/15/16.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adkdevelopment.earthquakesurvival.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.provider.news.NewsColumns;
import com.adkdevelopment.earthquakesurvival.ui.CursorRecyclerViewAdapter;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Creates RecyclerView adapter which populates task items in MainFragment
 * Each element has an onClickListener
 */
public class NewsAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private final Context mContext;

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @Bind(R.id.news_item_title) TextView mTitle;
        @Bind(R.id.news_item_date) TextView mDate;
        @Bind(R.id.news_item_description) TextView mDescription;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public class ViewHolderStats extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @Bind(R.id.stat_item_total) TextView mStatTotal;
        @Bind(R.id.stat_item_magnitude) TextView mStatMagnitude;
        @Bind(R.id.stat_item_description) TextView mStatDescription;
        @Bind(R.id.stat_item_date) TextView mStatDate;

        public ViewHolderStats(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_statistics, parent, false);

                return new ViewHolderStats(v);
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_item, parent, false);

                return new ViewHolder(v);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {

        // TODO: 4/7/16 refactor
        if (holder.getItemViewType() == 0) {

            Cursor tempCursor = mContext.getContentResolver()
                    .query(EarthquakeColumns.CONTENT_URI,
                            new String[]{EarthquakeColumns.TIME},
                            null,
                            null,
                            null);

            if (tempCursor != null) {

                long count = tempCursor.getCount();
                ((ViewHolderStats) holder).mStatTotal.setText(mContext.getString(R.string.earthquake_statistics_day, count));

                tempCursor.close();
                tempCursor = mContext.getContentResolver().query(EarthquakeColumns.CONTENT_URI,
                        null,
                        null,
                        null,
                        EarthquakeColumns.MAG + " DESC LIMIT 1");

                if (tempCursor != null) {
                    tempCursor.moveToFirst();

                    String desc = tempCursor.getString(tempCursor.getColumnIndex(EarthquakeColumns.PLACE));
                    ((ViewHolderStats) holder).mStatDescription.setText(desc);

                    Double magnitude = tempCursor.getDouble(tempCursor.getColumnIndex(EarthquakeColumns.MAG));
                    ((ViewHolderStats) holder).mStatMagnitude.setText(mContext.getString(R.string.earthquake_magnitude, magnitude));

                    Long dateMillis = tempCursor.getLong(tempCursor.getColumnIndex(EarthquakeColumns.TIME));
                    ((ViewHolderStats) holder).mStatDate.setText(Utilities.getNiceDate(dateMillis));

                    tempCursor.close();
                }
            }

        } else {
            Long dateMillis = cursor.getLong(cursor.getColumnIndex(NewsColumns.DATE));
            ((ViewHolder) holder).mDate.setText(Utilities.getNiceDate(dateMillis));

            String title = cursor.getString(cursor.getColumnIndex(NewsColumns.TITLE));
            String link = cursor.getString(cursor.getColumnIndex(NewsColumns.URL));

            link = mContext.getString(R.string.earthquake_link, link);
            ((ViewHolder) holder).mDescription.setText(Html.fromHtml(link));
            ((ViewHolder) holder).mDescription.setMovementMethod(LinkMovementMethod.getInstance());

            ((ViewHolder) holder).mTitle.setText(title);

            ((ViewHolder) holder).mTitle.setOnClickListener(click -> {
                Toast.makeText(mContext, title, Toast.LENGTH_SHORT).show();
            });
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
}