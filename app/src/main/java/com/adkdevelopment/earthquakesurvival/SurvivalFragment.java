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

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adkdevelopment.earthquakesurvival.utils.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class SurvivalFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    @Bind(R.id.section_label) TextView mSectionLabel;
    @Bind(R.id.earthquake_latitude) TextView mEarthquakeLatitude;
    @Bind(R.id.earthquake_longitude) TextView mEarthquakeLongitude;
    View mRootView;

    public SurvivalFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SurvivalFragment newInstance(int sectionNumber) {
        SurvivalFragment fragment = new SurvivalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.survival_fragment, container, false);

        ButterKnife.bind(this, mRootView);

        mSectionLabel.setText(getString(R.string.temp_string, getArguments().getInt(ARG_SECTION_NUMBER)));

        return mRootView;
    }

    /**
     * Method to set location lat and long from PagerActivity
     * @param location current location of a phone
     */
    public void setLocationText(Location location) {
        if (location != null) {
            mEarthquakeLatitude.setText(getString(R.string.earthquake_latitude, location.getLatitude()));
            mEarthquakeLongitude.setText(getString(R.string.earthquake_longitude, location.getLongitude()));
        }
    }

    public void animateViewsIn() {
        if (mRootView != null) {
            ViewGroup root = (ViewGroup) mRootView.findViewById(R.id.root);
            Utilities.animateViewsIn(getContext(), root);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        animateViewsIn();
        Log.d("SurvivalFragment", "resume");
    }
}
