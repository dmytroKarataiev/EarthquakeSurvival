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

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adkdevelopment.earthquakesurvival.news_objects.Channel;
import com.adkdevelopment.earthquakesurvival.news_objects.Item;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Creates RecyclerView adapter which populates task items in MainFragment
 * Each element has an onClickListener
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final Channel mNewsData;
    private final Context mContext;

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        @Bind(R.id.news_item_title) TextView mTitle;
        @Bind(R.id.news_item_date) TextView mDate;
        @Bind(R.id.news_item_description) TextView mDescription;


        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            ButterKnife.bind(this, v);
        }

        @Override
        public void onClick(View v) {

            if (BuildConfig.DEBUG) Log.d("ViewHolder", v.getClass().getSimpleName());

            Toast.makeText(mContext, v.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(mContext.getApplicationContext(), DetailActivity.class);
            //intent.putExtra(Feature.EARTHQUAKE, mEarthquakeData.getFeatures().get(getAdapterPosition()));
            // TODO: 3/25/16 add shared transitions
            // Shared Transitions for SDK >= 21
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //    @SuppressWarnings("unchecked") Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity) mContext).toBundle();
           //     mContext.startActivity(intent, bundle);
           // } else {
           //     mContext.startActivity(intent);
           // }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(Channel item, Activity activity) {
        mContext = activity;
        mNewsData = item;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Item news = mNewsData.getItem().get(position);

        holder.mDate.setText(news.getPubDate());
        holder.mDescription.setText(Html.fromHtml(news.getDescription()));
        holder.mTitle.setText(news.getTitle());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        if (mNewsData != null) {
            return mNewsData.getItem().size();
        } else {
            return 0;
        }
    }

}