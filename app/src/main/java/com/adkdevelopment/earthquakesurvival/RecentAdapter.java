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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adkdevelopment.earthquakesurvival.earthquake_data_objects.EarthquakeObject;
import com.adkdevelopment.earthquakesurvival.earthquake_data_objects.Feature;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Creates RecyclerView adapter which populates task items in MainFragment
 * Each element has an onClickListener
 */
public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {

    private final EarthquakeObject mEarthquakeData;
    private final Context mContext;

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        @Bind(R.id.news_item_title) TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            ButterKnife.bind(this, v);
        }

        @Override
        public void onClick(View v) {

            if (BuildConfig.DEBUG) Log.d("ViewHolder", v.getClass().getSimpleName());

            /*Intent intent = new Intent(mContext.getApplicationContext(), DetailActivity.class);
            intent.putExtra(RSSNewsItem.TASKITEM, mDataset.get(getAdapterPosition()));

            // Shared Transitions for SDK >= 21
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                @SuppressWarnings("unchecked") Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity) mContext).toBundle();
                mContext.startActivity(intent, bundle);
            } else {
                mContext.startActivity(intent);
            }*/
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecentAdapter(EarthquakeObject item, Activity activity) {
        mContext = activity;
        mEarthquakeData = item;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recent_news_item, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Feature earthquakeObject = mEarthquakeData.getFeatures().get(position);
        String listItemTitle = earthquakeObject.getProperties().getTitle();
        holder.mTextView.setText(listItemTitle);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        if (mEarthquakeData != null) {
            return mEarthquakeData.getFeatures().size();
        } else {
            return 0;
        }
    }

}