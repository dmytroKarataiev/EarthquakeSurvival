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
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.adkdevelopment.earthquakesurvival.R;
import com.adkdevelopment.earthquakesurvival.data.objects.earthquake.EarthquakeObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
     * Returns true if to show fault lines
     * @param context from which call is being made
     * @return true if to show fault lines
     */
    public static boolean getFaultlinesPrefs(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.sharedprefs_key_faultlines), true);
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
    public static Bitmap getEarthquakeMarker(Context context, Double magnitude) {

        if (magnitude < 1) {
            magnitude = 1.0;
        }

        GradientDrawable oval;
        oval = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.marker);


        if (oval != null) {

            int STROKE_SIZE = 5;
            float DASH_WIDTH = 9f;
            float DASH_GAP = 3f;

            if (magnitude >= 3 && magnitude <= 5) {
                oval.setColors(new int[]{Color.TRANSPARENT, Color.BLUE});
                oval.setStroke(STROKE_SIZE, Color.BLUE, DASH_WIDTH, DASH_GAP);
            } else if (magnitude > 5 && magnitude < 7) {
                oval.setColors(new int[]{Color.TRANSPARENT, Color.YELLOW});
                oval.setStroke(STROKE_SIZE, Color.YELLOW, DASH_WIDTH, DASH_GAP);
            } else if (magnitude >= 7) {
                oval.setColors(new int[]{Color.TRANSPARENT, Color.RED});
                oval.setStroke(STROKE_SIZE, Color.RED, DASH_WIDTH, DASH_GAP);
            } else {
                oval.setColors(new int[]{Color.TRANSPARENT, Color.GREEN});
                oval.setStroke(STROKE_SIZE, Color.GREEN, DASH_WIDTH, DASH_GAP);
            }

            int diameter = (int) (oval.getIntrinsicWidth() * magnitude / 4);

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

    /**
     * Returns relative date from the input millis.
     * @param millis of the event.
     * @return textrual relative interpretation of the inputted millis.
     */
    public static String getRelativeDate(Long millis) {
        Date date = new Date(millis);
        return DateUtils.getRelativeTimeSpanString(date.getTime()).toString();
    }

    /**
     * Returns formatted date in a String.
     * @param unformattedDate in millis.
     * @return String formatted in "h:mm a", Locale aware.
     */
    public static String getFormattedDate(long unformattedDate) {

        Date date = new Date(unformattedDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());

        return simpleDateFormat.format(date);
    }


    /**
     * Makes sliding from the bottom effect on elements in a RecyclerView.
     * @param context from which call is made.
     * @param viewGroup on which to perform the animation.
     */
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
                        .setDuration(300L)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

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

    /**
     * Checks if the app is in foreground
     * @param context from which call is made
     * @return true if is in foreground, false - otherwise
     */
    public static boolean checkForeground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                Log.i("Foreground App", appProcess.processName);

                if (context.getPackageName().equalsIgnoreCase(appProcess.processName)) {
                    Log.i(TAG, "foreground true:" + appProcess.processName);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Makes text version of an html string.
     *
     * @param input informatted html text.
     * @return only formatted text from html input.
     */
    public static Spanned getHtmlText(String input) {
        Spanned result;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
        } else {
            //noinspection deprecation
            result = Html.fromHtml(input);
        }

        return result;
    }

    /**
     * Creates a properly formatted Uri only if link is a correct URL
     * @param link to parse
     * @return Uri if a link was a correct URL, null otherwise.
     */
    @Nullable
    public static Uri getProperUri(String link) {
        URL finalLink;
        try {
            finalLink = new URL(link);
            return Uri.parse(finalLink.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e);
        }
        return null;
    }

    /**
     * Formats an earthquake place, e.g. "100km from Osaka, Japan"
     * becomes "Japan, 100km from Osaka"
     * @param place String to move around a coma (if it has one)
     * @return formatted String with changed places
     */
    @NonNull
    public static String formatEarthquakePlace(String place) {
        String comma = ", ";
        if (place.contains(comma)) {
            int divider = place.indexOf(comma);
            place = place.substring(divider + comma.length())
                    + comma
                    + place.substring(0, divider);
        }
        return place;
    }
}
