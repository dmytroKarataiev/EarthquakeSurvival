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

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.adkdevelopment.earthquakesurvival.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Additional class with helper functions for Geofence and Maps functionality
 */
public class LocationUtils {

    private static final String TAG = LocationUtils.class.getSimpleName();

    /**
     * Returns the error string for a geofencing error code.
     */
    public static String getErrorString(Context context, int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return context.getString(R.string.geofence_error_unavailable);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return context.getString(R.string.geofence_error_many_geofences);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return context.getString(R.string.geofence_error_many_intents);
            default:
                return context.getString(R.string.geofence_error_unknown);
        }
    }

    /**
     * Returns textual description of the Geo Transition event
     * @param transitionType int of a transition type
     * @return String with a textual description
     */
    public static String getTransitionString(Context context, int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return context.getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return context.getString(R.string.geofence_transition_exited);
            default:
                return context.getString(R.string.geofence_transition_unknown);
        }
    }

    /**
     * Concatenates all the id's to one String
     * @param context from which a call is being made
     * @param geofenceTransition type of transition
     * @param triggeringGeofences list of all triggering transitions
     * @return concatenated String with the type of a transition and all triggering id's
     */
    public static String getTransitionDetails(Context context,
                                        int geofenceTransition,
                                        List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(context, geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        List<String> triggeringGeofencesIdsList = new ArrayList<>();

        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return triggeringGeofencesIdsString;
    }

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    public static final int CAMERA_DEFAULT_ZOOM = 6;
    public static final long EXP_HOURS = 12;
    public static final long EXP_MILLIS = EXP_HOURS * 60 * 60 * 1000;

    /**
     * Returns current location from the SharedPreferences
     * @param context from which call is being made
     * @return LatLng with current location
     */
    public static LatLng getLocation(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Double latitude = Double.longBitsToDouble(
                sharedPreferences.getLong(
                        context.getString(R.string.sharedprefs_key_latitude), 0));

        Double longitude = Double.longBitsToDouble(
                sharedPreferences.getLong(
                        context.getString(R.string.sharedprefs_key_longitude), 0));

        return new LatLng(latitude, longitude);
    }

    /**
     * Saves the current location in SharedPreferences
     * @param context from which call is being made
     * @param location current location
     */
    public static void setLocation(Context context, Location location) {

        if (location != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            // save the precision of Double by converting it to raw long bits
            editor.putLong(context.getString(R.string.sharedprefs_key_latitude),
                    Double.doubleToRawLongBits(location.getLatitude()));

            editor.putLong(context.getString(R.string.sharedprefs_key_longitude),
                    Double.doubleToRawLongBits(location.getLongitude()));

            editor.apply();
        } else {
            Log.e(TAG, "Location is null");
        }
    }

    /**
     * Metho uses the ‘haversine’ formula to calculate the great-circle distance between two points –
     * that is, the shortest distance over the earth’s surface – giving an ‘as-the-crow-flies’
     * distance between the points (ignoring any hills they fly over, of course!).
     * @param latLng initial coordiantes
     * @param location distance to calculate from
     * @return distance in miles from your location to this point
     */
    public static double getDistance(LatLng latLng, LatLng location) {

        if (location.latitude != 0) {
            double EARTH_RADIUS = 6371000;

            double phi1 = Math.toRadians(latLng.latitude);
            double phi2 = Math.toRadians(location.latitude);

            double dPhi = Math.toRadians(location.latitude - latLng.latitude);
            double dGamma = Math.toRadians(location.longitude - latLng.longitude);

            double a = Math.sin(dPhi / 2) * Math.sin(dPhi / 2) +
                    Math.cos(phi1) * Math.cos(phi2) *
                    Math.sin(dGamma / 2) * Math.sin(dGamma / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return EARTH_RADIUS * c / 1.6 / 1000;
        } else {
            return 0.0;
        }
    }

}