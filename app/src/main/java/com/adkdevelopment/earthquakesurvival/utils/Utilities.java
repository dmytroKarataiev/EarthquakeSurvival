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

package com.adkdevelopment.earthquakesurvival.utils;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.adkdevelopment.earthquakesurvival.R;
import com.adkdevelopment.earthquakesurvival.objects.earthquake.EarthquakeObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Date;

/**
 * Utilities class with helper functions
 * Created by karataev on 3/15/16.
 */
public class Utilities {

    private final static String TAG = Utilities.class.getSimpleName();

    public static int mBlueColor;
    public static int mWhiteColor;

    /**
     * Method to check if device is connected to the internet
     * @param context from which call is being made
     * @return true if connected, false otherwise
     */
    public static boolean isOnline(Context context) {
        if (context != null) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    /**
     * Returns true if to send geofence notifications
     * @param context from which call is being made
     * @return true if to send notifications
     */
    public static boolean getNotificationsPrefs(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.sharedprefs_key_notifications), true);
    }

    /**
     * Method to get SyncInterval from SharedPreferences
     * @param context from which call is being made
     * @return interval in minutes
     */
    public static int getSyncIntervalPrefs(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String syncFrequency = sharedPreferences
                .getString(context.getString(R.string.sharedprefs_key_syncfrequency), "7200");

        return Integer.parseInt(syncFrequency);
    }

    /**
     * Method to return Magnitude from SharedPreferences
     * @param context from which call is being made
     * @return value of magnitude to query and show
     */
    public static int getMagnitudePrefs(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(context.getString(R.string.sharedprefs_key_magnitude), 3);
    }

    /**
     * Method to get Distance parameter from the SharedPreferences
     * @param context from which call is being made
     * @return distance in meters to use in Geofence radius
     */
    public static int getDistancePrefs(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int distance = sharedPreferences.getInt(context.getString(R.string.sharedprefs_key_distance), 25);
        // miles
        distance = (int) (distance * 1000 * 1.6);
        return distance;
    }

    /**
     * Creates Bitmap for a Map marker depending on the magnitude of an earthquake
     * @param context from which call is being made
     * @param magnitude from 0 to 10 scale earthquake intensity
     * @return colorful oval of size depending on magnitude
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap getEarthquakeMarker(Context context, Double magnitude) {

        if (magnitude < 1) {
            magnitude = 1.0;
        }

        GradientDrawable oval;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            oval = (GradientDrawable) context.getResources().getDrawable(R.drawable.marker, context.getTheme());
        } else {
            //noinspection deprecation
            oval = (GradientDrawable) context.getResources().getDrawable(R.drawable.marker);
        }

        if (oval != null) {
            if (magnitude > 3 && magnitude < 5) {
                oval.setColor(Color.BLUE);
            } else if (magnitude > 5 && magnitude < 7) {
                oval.setColor(Color.YELLOW);
            } else if (magnitude > 7) {
                oval.setColor(Color.RED);
            } else {
                oval.setColor(Color.GREEN);
            }


            int diameter = (int) (oval.getIntrinsicWidth() * magnitude);

            Canvas canvas = new Canvas();
            Bitmap icon = Bitmap.createBitmap(
                    diameter,
                    diameter,
                    Bitmap.Config.ARGB_8888);

            canvas.setBitmap(icon);
            oval.setBounds(0, 0, diameter, diameter);
            oval.draw(canvas);

            return icon;
        } else {
            return null;
        }
    }

    public static String getNiceDate(Long millis) {
        Date date = new Date(millis);
        return DateUtils.getRelativeTimeSpanString(date.getTime()).toString();
    }

    public static void animateViewsIn(Context context, ViewGroup viewGroup) {
        if (viewGroup != null) {
            int count = viewGroup.getChildCount();
            float offset = context.getResources().getDimensionPixelSize(R.dimen.offset_y);

            Interpolator interpolator;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                interpolator =
                        AnimationUtils.loadInterpolator(context, android.R.interpolator.linear_out_slow_in);
            } else {
                interpolator =
                        AnimationUtils.loadInterpolator(context, android.R.interpolator.linear);
            }

            // loop over the children setting an increasing translation y but the same animation
            // duration + interpolation
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                view.setVisibility(View.VISIBLE);
                view.setTranslationY(offset);
                view.setAlpha(0.85f);
                // then animate back to natural position
                view.animate()
                        .translationY(0f)
                        .alpha(1f)
                        .setInterpolator(interpolator)
                        .setDuration(1000L)
                        .start();
                // increase the offset distance for the next view
                offset *= 1.5f;
            }
        }
    }

    /**
     * Returns true if Google Play Services available on the phone,
     * otherwise tries to ask user to install it
     * @param activity from which call is made
     * @return true if present, false otherwise
     */
    public static boolean checkPlayServices(Activity activity) {
        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        if (result != ConnectionResult.SUCCESS) {
            if (GoogleApiAvailability.getInstance().isUserResolvableError(result)) {
                GoogleApiAvailability.getInstance().getErrorDialog(activity, result, 0).show();
            } else {
                Log.e(TAG, "checkPlayServices not available");
            }
            return false;
        }
        return true;
    }

    /**
     * Sets sorting preferences in SharedPreferences
     * @param context from which call is being made
     * @param sort preference according to the database schema
     */
    public static void setSortingPreference(Context context, int sort) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.sharedprefs_key_sort), sort);
        editor.apply();
    }

    /**
     * Returns sorting preference
     * @param context from which call is being made
     * @return int preference according to the database schema
     */
    public static int getSortingPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(context.getString(R.string.sharedprefs_key_sort), EarthquakeObject.SORT_TIME);
    }

    /**
     * Animates RecyclerView card on click with revealing effect
     * @param viewHolder to make animation on
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void animationCard(RecyclerView.ViewHolder viewHolder) {

        if (mBlueColor == 0) {
            mBlueColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.colorPrimary);
        }
        if (mWhiteColor == 0) {
            mWhiteColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.white);
        }

        int finalRadius = (int) Math.hypot(viewHolder.itemView.getWidth() / 2, viewHolder.itemView.getHeight() / 2);

        Animator anim = ViewAnimationUtils.createCircularReveal(viewHolder.itemView,
                viewHolder.itemView.getWidth() / 2,
                viewHolder.itemView.getHeight() / 2, 0, finalRadius);

        viewHolder.itemView.setBackgroundColor(mBlueColor);
        anim.start();
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewHolder.itemView.setBackgroundColor(mWhiteColor);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
