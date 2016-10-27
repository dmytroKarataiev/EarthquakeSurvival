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
 *
 */

package com.adkdevelopment.earthquakesurvival.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.adkdevelopment.earthquakesurvival.App;
import com.adkdevelopment.earthquakesurvival.R;
import com.adkdevelopment.earthquakesurvival.data.syncadapter.SyncAdapter;
import com.adkdevelopment.earthquakesurvival.eventbus.RxBus;
import com.adkdevelopment.earthquakesurvival.ui.settings.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by karataev on 4/19/16.
 */
public class MainActivity extends AppCompatActivity {

    @Nullable @BindView(R.id.toolbar) Toolbar mToolbar;

    private static final String TAG = MainActivity.class.getSimpleName();

    private NewsFragment mNewsFragment;
    private MapviewFragment mMapviewFragment;
    private SurvivalFragment mSurvivalFragment;

    public static final String NEWS = "news";
    public static final String MAP = "maps";
    public static final String SURVIVE = "survive";

    // RxJava eventbus
    private RxBus _rxBus;
    private CompositeSubscription _subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tablet_activity);

        _rxBus = App.getRxBusSingleton();

        ButterKnife.bind(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitle(getString(R.string.app_name));
        }

        SyncAdapter.syncImmediately(this);

        if (findViewById(R.id.news_container) != null) {

            if (savedInstanceState == null) {
                mNewsFragment = NewsFragment.newInstance(0);
                mMapviewFragment = MapviewFragment.newInstance(0);

                getSupportFragmentManager().beginTransaction().add(R.id.container, mNewsFragment, NEWS).commit();
                getSupportFragmentManager().beginTransaction().add(R.id.news_container, mMapviewFragment, MAP).commit();
            } else {
                mNewsFragment = (NewsFragment) getSupportFragmentManager().findFragmentByTag(NEWS);
                mSurvivalFragment = (SurvivalFragment) getSupportFragmentManager().findFragmentByTag(SURVIVE);
                mMapviewFragment = (MapviewFragment) getSupportFragmentManager().findFragmentByTag(MAP);
            }

        } else {
            finish();
            startActivity(new Intent(this, PagerActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        _subscription = new CompositeSubscription();
        _subscription.add(_rxBus.toObservable().subscribe(o -> {
            if (o instanceof Pair) {
                Pair pair = (Pair) o;
                Bundle bundle = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    bundle = ActivityOptions.makeSceneTransitionAnimation(this, (Pair) pair.second)
                            .toBundle();
                }
                startActivity((Intent) pair.first, bundle);
            } else if (o instanceof Intent) {
                startActivity((Intent) o);
            }
        }));
    }

    @Override
    protected void onStop() {
        super.onStop();
        _subscription.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tablet_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_refresh:
                SyncAdapter.syncImmediately(getBaseContext());
                return true;
            case R.id.action_survive:
                if (getSupportFragmentManager().findFragmentByTag(NEWS) != null) {
                    mSurvivalFragment = SurvivalFragment.newInstance(0);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mSurvivalFragment, SURVIVE).commit();
                    item.setIcon(R.drawable.ic_info_black_24dp);
                } else {
                    mNewsFragment = NewsFragment.newInstance(0);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mNewsFragment, NEWS).commit();
                    item.setIcon(R.drawable.ic_local_hospital_black_24dp);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


