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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.adkdevelopment.earthquakesurvival.adapters.InfoAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class InfoFragment extends Fragment {

    @Bind(R.id.recyclerview) RecyclerView mRecyclerView;
    private InfoAdapter mRecentAdapter;

    public InfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.info_fragment, container, false);
        ImageView backdrop = ButterKnife.findById(getActivity(), R.id.backdrop);
        FloatingActionButton floatingActionButton = ButterKnife.findById(getActivity(), R.id.fab);

        ButterKnife.bind(this, rootView);

        if (getActivity().getIntent() != null) {

            int section = getActivity().getIntent().getIntExtra(SurvivalFragment.SECTION, -1);
            List<String> titles = new ArrayList<>();
            List<String> text = new ArrayList<>();
            String title = "";

            int drawable = -1;

            switch (section) {
                case R.id.survive_card_before:
                    titles.addAll(Arrays.asList(getResources().getStringArray(R.array.survival_before_title)));
                    text.addAll(Arrays.asList(getResources().getStringArray(R.array.survival_before_text)));
                    drawable = R.drawable.earth1;
                    title = getString(R.string.survival_before);
                    break;
                case R.id.survive_card_during:
                    titles.addAll(Arrays.asList(getResources().getStringArray(R.array.survival_during_title)));
                    text.addAll(Arrays.asList(getResources().getStringArray(R.array.survival_during_text)));
                    drawable = R.drawable.earth2;
                    title = getString(R.string.survival_during);
                    break;
                case R.id.survive_card_after:
                    titles.addAll(Arrays.asList(getResources().getStringArray(R.array.survival_after_title)));
                    text.addAll(Arrays.asList(getResources().getStringArray(R.array.survival_after_text)));
                    drawable = R.drawable.earth3;
                    title = getString(R.string.survival_after);
                    break;
                case R.id.survive_card_more:
                    title = getString(R.string.survival_resources);
                    titles.addAll(Arrays.asList(getResources().getStringArray(R.array.survival_resources_title)));
                    text.addAll(Arrays.asList(getResources().getStringArray(R.array.survival_resources_text)));
                    Log.d("InfoFragment", Arrays.toString(text.toArray()));
                    drawable = R.drawable.earth3;
                    break;
                case R.id.survive_card_kit:
                    title = getString(R.string.survival_kit);
                    titles.addAll(Arrays.asList(getResources().getStringArray(R.array.survival_kit_title)));
                    text.addAll(Arrays.asList(getResources().getStringArray(R.array.survival_kit_text)));
                    drawable = R.drawable.earth3;
                    break;
                default:
                    break;
            }

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            mRecentAdapter = new InfoAdapter(titles, text, getContext());

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mRecentAdapter);

            floatingActionButton.setOnClickListener(click -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                if (titles.size() > 0 && text.size() > 0) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, titles.get(0));
                    intent.putExtra(Intent.EXTRA_TEXT, text.get(0));
                }
                startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
            });

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
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
