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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adkdevelopment.earthquakesurvival.utils.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class SurvivalFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String SECTION = "section";

    @Nullable @Bind(R.id.parallax_bar) View mParallaxBar;
    @Bind(R.id.nested_scroll) NestedScrollView mNestedScroll;
    View mRootView;

    @OnClick({ R.id.survive_card_before,
            R.id.survive_card_during,
            R.id.survive_card_after,
            R.id.survive_card_more })
    public void startActivity(View view) {
        Intent intent = new Intent(getContext(), InfoActivity.class);
        Log.d("SurvivalFragment", "listener.getId():" + view.getId() + " " + R.id.survive_card_before);
        intent.putExtra(SECTION, view.getId());
        startActivity(intent);
    }

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

        // only in landscape mode
        if (mParallaxBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mNestedScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    int max = mParallaxBar.getHeight();
                    // scroll change
                    int dy = scrollY - oldScrollY;
                    if (dy > 0) {
                        mParallaxBar.setTranslationY(Math.max(-max, mParallaxBar.getTranslationY() - dy / 2));
                    } else {
                        mParallaxBar.setTranslationY(Math.min(0, mParallaxBar.getTranslationY() - dy / 2));
                    }
                });
            }
        }

        return mRootView;
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
    }
}
