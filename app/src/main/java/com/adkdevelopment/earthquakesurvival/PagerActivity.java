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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.adkdevelopment.earthquakesurvival.settings.SettingsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PagerActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private PagerAdapter mPagerAdapter;

    @Bind(R.id.sliding_tabs) TabLayout mTab;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_activity);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);

        mTab.setupWithViewPager(mViewPager);
        setTabImages();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTabImages() {
        if (mTab != null) {

            Context context = getApplicationContext();

            int[] iconSet = {
                    R.drawable.error_grey,
                    R.drawable.map_grey,
                    R.drawable.newspaper_grey,
                    R.drawable.lightbulb_grey,
                    R.drawable.error_blue,
                    R.drawable.map_blue,
                    R.drawable.newspaper_blue,
                    R.drawable.lightbulb_blue
            };

            for (int i = 0, n = mTab.getTabCount(); i < n; i++) {
                TabLayout.Tab tabLayout = mTab.getTabAt(i);
                tabLayout.setCustomView(R.layout.pager_tab_layout);

                ImageView imageView = (ImageView) tabLayout.getCustomView().findViewById(R.id.tab_item_image);

                TextView textView = (TextView) tabLayout.getCustomView().findViewById(R.id.tab_item_text);
                textView.setText(mTab.getTabAt(i).getText());

                if (i == 0) {
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    imageView.setImageResource(iconSet[i + iconSet.length / 2]);
                } else {
                    textView.setTextColor(getResources().getColor(R.color.grey));
                    imageView.setImageResource(iconSet[i]);
                }
            }

            mTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    ((ImageView) tab.getCustomView().findViewById(R.id.tab_item_image))
                            .setImageResource(iconSet[tab.getPosition() + iconSet.length / 2]);
                    ((TextView) tab.getCustomView().findViewById(R.id.tab_item_text))
                            .setTextColor(getResources().getColor(R.color.colorPrimary));
                    mViewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    ((ImageView) tab.getCustomView().findViewById(R.id.tab_item_image))
                            .setImageResource(iconSet[tab.getPosition()]);
                    ((TextView) tab.getCustomView().findViewById(R.id.tab_item_text))
                            .setTextColor(getResources().getColor(R.color.grey));

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

}
