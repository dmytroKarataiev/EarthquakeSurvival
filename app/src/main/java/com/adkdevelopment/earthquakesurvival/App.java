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

import android.app.Application;
import android.content.Context;

import com.adkdevelopment.earthquakesurvival.data.remote.ApiManager;
import com.adkdevelopment.earthquakesurvival.eventbus.RxBus;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Main Application class, which keeps singletons for managers
 * and for the eventbus.
 * Created by karataev on 3/24/16.
 */
public class App extends Application {

    private static ApiManager sApiManager, sNewsManager;
    private static Context sContext;

    // event bus from RxJava
    private static RxBus _rxBus;

    // Singleton Retrofit for Earthquakes
    public static ApiManager getApiManager() {
        if (sApiManager == null) {
            try {
                sApiManager = new ApiManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sApiManager;
    }

    // Singleton Retrofit for News
    public static ApiManager getNewsManager() {
        if (sNewsManager == null) {
            try {
                sNewsManager = new ApiManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sNewsManager;
    }

    // singleton RxBus
    public static RxBus getRxBusSingleton() {
        if (_rxBus == null) {
            _rxBus = new RxBus();
        }
        return _rxBus;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
