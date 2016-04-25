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

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adkdevelopment.earthquakesurvival.adapters.RecentAdapter;
import com.adkdevelopment.earthquakesurvival.objects.earthquake.EarthquakeObject;
import com.adkdevelopment.earthquakesurvival.provider.earthquake.EarthquakeColumns;
import com.adkdevelopment.earthquakesurvival.syncadapter.SyncAdapter;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener,
        PopupMenu.OnMenuItemClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final int CURSOR_LOADER_ID = 0;
    private Cursor mCursor;

    private RecentAdapter mRecentAdapter;
    private static final String TAG = RecentFragment.class.getSimpleName();

    @Bind(R.id.recyclerview) RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.list_empty_text) TextView mListEmpty;
    @Nullable @Bind(R.id.parallax_bar) View mParallaxBar;

    public RecentFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RecentFragment newInstance(int sectionNumber) {
        RecentFragment fragment = new RecentFragment();
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
        View rootView = inflater.inflate(R.layout.recent_fragment, container, false);

        ButterKnife.bind(this, rootView);

        // to inflate menu in this fragment
        setHasOptionsMenu(true);

        mListEmpty.setVisibility(View.INVISIBLE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecentAdapter = new RecentAdapter(getActivity(), mCursor);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecentAdapter);

        // TODO: 4/24/16 Create a separate class 
        // Prevent Swipe to refresh if recyclerview isn't in top position
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // only in landscape mode
                if (mParallaxBar != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        int max = mParallaxBar.getHeight();
                        if (dy > 0) {
                            mParallaxBar.setTranslationY(Math.max(-max, mParallaxBar.getTranslationY() - dy / 2));
                        } else {
                            mParallaxBar.setTranslationY(Math.min(0, mParallaxBar.getTranslationY() - dy / 2));
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visible = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();

                if (mSwipeRefreshLayout != null) {
                    if (visible != 0) {
                        mSwipeRefreshLayout.setEnabled(false);
                    } else {
                        mSwipeRefreshLayout.setEnabled(true);
                    }
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (Utilities.isOnline(getContext())) {
                SyncAdapter.syncImmediately(getContext());
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        animateViewsIn();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    // Attach loader to our flavors database query
    // run when loader is initialized
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){

        int sort = Utilities.getSortingPreference(getContext());
        String sortingPreference = EarthquakeColumns.TIME;

        switch (sort) {
            case EarthquakeObject.SORT_MAGNITUDE:
                sortingPreference = EarthquakeColumns.MAG;
                break;
            case EarthquakeObject.SORT_TIME:
                sortingPreference = EarthquakeColumns.TIME;
                break;
        }

        return new CursorLoader(getActivity(),
                EarthquakeColumns.CONTENT_URI,
                null,
                EarthquakeColumns.MAG + " >= ?",
                new String[] {String.valueOf(Utilities.getMagnitudePrefs(getContext()))},
                sortingPreference + " DESC");
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            mListEmpty.setVisibility(View.VISIBLE);
        } else {
            mListEmpty.setVisibility(View.INVISIBLE);
        }

        mCursor = data;
        mRecentAdapter.swapCursor(mCursor);
    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        mRecentAdapter.swapCursor(null);
    }

    public void scrollToTop() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.sharedprefs_key_magnitude)) ||
                key.equals(getString(R.string.sharedprefs_key_sort))) {
            getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        }
    }

    public void animateViewsIn() {
        Utilities.animateViewsIn(getContext(), mRecyclerView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_recent, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sort:
                showSortMenu(getActivity().findViewById(R.id.action_sort));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows PopupMenu on Filter button click in ActionBar
     * @param view of the button itself
     */
    public void showSortMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_sort_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_sort_magnitude:
                Utilities.setSortingPreference(getContext(), EarthquakeObject.SORT_MAGNITUDE);
                return true;
            case R.id.menu_item_sort_time:
                Utilities.setSortingPreference(getContext(), EarthquakeObject.SORT_TIME);
                return true;
            default:
                return false;
        }
    }
}
