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

package com.adkdevelopment.earthquakesurvival.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adkdevelopment.earthquakesurvival.App;
import com.adkdevelopment.earthquakesurvival.InfoActivity;
import com.adkdevelopment.earthquakesurvival.R;
import com.adkdevelopment.earthquakesurvival.SurvivalFragment;
import com.adkdevelopment.earthquakesurvival.eventbus.RxBus;
import com.adkdevelopment.earthquakesurvival.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Populates InfoFragments with cards with data from resources
 * Created by karataev on 4/16/16.
 */
public class SurvivalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mTitles;
    private Context mContext;

    private RxBus _rxBus;

    public SurvivalAdapter(Context context, List<String> title) {
        super();
        mContext = context;
        _rxBus = App.getRxBusSingleton();
        mTitles = title;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.survival_item) TextView mTitle;

        public ViewHolder(View v)  {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public static class ViewHolderImage extends RecyclerView.ViewHolder {

        @Bind(R.id.survive_card_image) ImageView mImage;

        public ViewHolderImage(View v)  {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.survival_item_image, parent, false);
                return new ViewHolderImage(v);
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.survival_item, parent, false);
                return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case 0:
                Picasso.with(mContext).load(R.drawable.dropcoverholdon).into(((ViewHolderImage) holder).mImage);
                break;
            default:
                ((ViewHolder) holder).mTitle.setText(mTitles.get(position));
                holder.itemView.setOnClickListener(click -> {

                    Intent intent = new Intent(mContext, InfoActivity.class);
                    intent.putExtra(SurvivalFragment.SECTION, position);

                    if (_rxBus.hasObservers()) {
                        // Check if a phone supports shared transitions
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            Utilities.animationCard(holder);

                            Pair pair = Pair.create(holder.itemView.findViewById(R.id.survive_card),
                                    holder.itemView.findViewById(R.id.survive_card).getTransitionName());
                            _rxBus.send(Pair.create(intent, pair));
                        } else {
                            _rxBus.send(intent);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
