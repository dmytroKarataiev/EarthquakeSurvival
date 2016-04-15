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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class InfoFragment extends Fragment {

    @Bind(R.id.info_first_title) TextView mFirstTitle;
    @Bind(R.id.info_first_text) TextView mFirstText;
    @Bind(R.id.info_second_title) TextView mSecondTitle;
    @Bind(R.id.info_second_text) TextView mSecondText;
    @Bind(R.id.info_third_title) TextView mThirdTitle;
    @Bind(R.id.info_third_text) TextView mThirdText;


    public InfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.info_fragment, container, false);
        ImageView backdrop = ButterKnife.findById(getActivity(), R.id.backdrop);
        ButterKnife.bind(this, rootView);

        if (getActivity().getIntent() != null) {
            int drawable = -1;
            String actionBarTitle = null;
            String firstTitle = null;
            String firstText = null;
            String secondTitle = null;
            String secondText = null;
            String thirdTitle = null;
            String thirdText = null;
            String fourthTitle = null;
            String fourthText = null;
            String fifthTitle = null;
            String fifthText = null;

            // TODO: 4/14/16 refactor
            switch (getActivity().getIntent().getIntExtra(SurvivalFragment.SECTION, -1)) {
                case R.id.survive_card_before:
                    mFirstTitle.setText(R.string.survival_before_title);
                    mFirstText.setText(R.string.survival_before_text);
                    drawable = R.drawable.earth1;
                    actionBarTitle = getString(R.string.survival_before);
                    break;
                case R.id.survive_card_during:
                    firstTitle = getString(R.string.survival_during_building_title);
                    firstText = getString(R.string.survival_during_building);
                    secondTitle = getString(R.string.survival_during_bed_title);
                    secondText = getString(R.string.survival_during_bed);
                    thirdTitle = getString(R.string.survival_during_car_title);
                    thirdText = getString(R.string.survival_during_car);
                    drawable = R.drawable.earth2;
                    actionBarTitle = getString(R.string.survival_during);
                    break;
                default:
                    drawable = R.drawable.dropcoverholdon;
                    actionBarTitle = getString(R.string.title_activity_info);
                    break;
            }

            mFirstTitle.setText(firstTitle);
            mFirstText.setText(firstText);
            mSecondTitle.setText(secondTitle);
            mSecondText.setText(secondText);
            mThirdTitle.setText(thirdTitle);
            mThirdText.setText(thirdText);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(actionBarTitle);
            }

            Picasso.with(getContext()).load(drawable).error(R.drawable.dropcoverholdon).into(backdrop);

        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
