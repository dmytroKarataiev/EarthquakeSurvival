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
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adkdevelopment.earthquakesurvival.provider.news.NewsColumns;
import com.adkdevelopment.earthquakesurvival.ui.CursorRecyclerViewAdapter;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Creates RecyclerView adapter which populates task items in MainFragment
 * Each element has an onClickListener
 */
public class NewsAdapter extends CursorRecyclerViewAdapter<NewsAdapter.ViewHolder> {

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

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {

        Long dateMillis = cursor.getLong(cursor.getColumnIndex(NewsColumns.DATE));
        Date date = new Date(dateMillis);
        holder.mDate.setText(DateUtils.getRelativeTimeSpanString(date.getTime()).toString());

        String description = cursor.getString(cursor.getColumnIndex(NewsColumns.DESCRIPTION));
        String title = cursor.getString(cursor.getColumnIndex(NewsColumns.TITLE));

        if (description != null) {
            holder.mDescription.setText(Html.fromHtml(description));
        }
        holder.mTitle.setText(title);

        holder.mTitle.setOnClickListener(click -> {
            if (BuildConfig.DEBUG) Log.d("ViewHolder", title);
            Toast.makeText(mContext, title, Toast.LENGTH_SHORT).show();
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