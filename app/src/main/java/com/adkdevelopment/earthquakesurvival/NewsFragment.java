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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adkdevelopment.earthquakesurvival.news_objects.Channel;
import com.adkdevelopment.earthquakesurvival.news_objects.Item;
import com.adkdevelopment.earthquakesurvival.news_objects.Rss;
import com.adkdevelopment.earthquakesurvival.provider.news.NewsColumns;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final int CURSOR_LOADER_ID = 10;
    private Cursor mCursor;

    private Channel mNews;
    private NewsAdapter mNewsAdapter;
    private static final String TAG = NewsFragment.class.getSimpleName();

    @Bind(R.id.recyclerview) RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.list_empty_text) TextView mListEmpty;

    public NewsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NewsFragment newInstance(int sectionNumber) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_fragment, container, false);

        ButterKnife.bind(this, rootView);

        mListEmpty.setVisibility(View.INVISIBLE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mNewsAdapter = new NewsAdapter(getActivity(), mCursor);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mNewsAdapter);

        // Prevent Swipe to refresh if recyclerview isn't in top position
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visible = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();

                if (visible != 0) {
                    mSwipeRefreshLayout.setEnabled(false);
                } else {
                    mSwipeRefreshLayout.setEnabled(true);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (Utilities.isOnline(getContext())) { getData(); }
            else { mSwipeRefreshLayout.setRefreshing(false); }
        });

        getData();

        return rootView;
    }

    private void getData() {
        App.getNewsManager().getNewsService().getNews().enqueue(mCallback);
    }

    private Callback<Rss> mCallback = new Callback<Rss>() {
        @Override
        public void onResponse(Call<Rss> call, Response<Rss> response) {
            if (mRecyclerView != null) {
                mNews = response.body().getChannel();
                mNewsAdapter = new NewsAdapter(getActivity(), mCursor);
                mRecyclerView.swapAdapter(mNewsAdapter, false);

                mSwipeRefreshLayout.setRefreshing(false);

                Log.d(TAG, "onResponse: success " + mNews.getItem().size());

                Vector<ContentValues> cVVector = new Vector<>(mNews.getItem().size());

                for (Item each : mNews.getItem()) {

                    ContentValues weatherValues = new ContentValues();

                    weatherValues.put(NewsColumns.DATE, each.getPubDate());
                    weatherValues.put(NewsColumns.TITLE, each.getTitle());
                    weatherValues.put(NewsColumns.DESCRIPTION, each.getDescription());
                    weatherValues.put(NewsColumns.URL, each.getLink());
                    weatherValues.put(NewsColumns.GUID, each.getGuid().getContent());

                    cVVector.add(weatherValues);

                }

                int inserted = 0;
                // add to database
                ContentResolver resolver = getContext().getContentResolver();

                if ( cVVector.size() > 0 ) {

                    // Student: call bulkInsert to add the weatherEntries to the database here
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);

                    inserted = resolver.bulkInsert(NewsColumns.CONTENT_URI, cvArray);
                }

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                // delete old data
                //resolver.delete(EarthquakeColumns.CONTENT_URI, EarthquakeColumns.TIME + " <= ?", new String[]{Long.toString(calendar.set(julianStartDay - 1)) });

                Log.d(TAG, "Service Complete. " + inserted + " Inserted");
                Cursor cursor = getContext().getContentResolver().query(NewsColumns.CONTENT_URI, new String[] {NewsColumns.GUID}, null, null, null);
                if (BuildConfig.DEBUG && cursor != null) {
                    Log.d(TAG, "cursor.getCount():" + cursor.getCount());
                    cursor.close();

                }


            }
        }

        @Override
        public void onFailure(Call<Rss> call, Throwable t) {
            Log.d(TAG, "onFailure: " + t.toString());
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    // Attach loader to our flavors database query
    // run when loader is initialized
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new CursorLoader(getActivity(),
                NewsColumns.CONTENT_URI,
                null,
                null,
                null,
                NewsColumns._ID + " DESC LIMIT 10");
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mListEmpty != null) {
            mListEmpty.setVisibility(View.INVISIBLE);
        }
        mCursor = data;
        mNewsAdapter.swapCursor(mCursor);
    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mListEmpty != null) {
            mListEmpty.setVisibility(View.VISIBLE);
        }
        mCursor = null;
        mNewsAdapter.swapCursor(null);
    }

    public void scrollToTop() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }
}
