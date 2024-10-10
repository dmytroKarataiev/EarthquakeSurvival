package com.adkdevelopment.earthquakesurvival.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class LocationUtilsTest {

    @Mock private Context mockContext;

    @Mock private SharedPreferences mockSharedPreferences;

    @Mock private SharedPreferences.Editor mockEditor;

    @Before
    public void setUp() {
        when(mockContext.getString(anyInt())).thenReturn("mock_string");
        when(mockContext.getSharedPreferences(anyString(), anyInt()))
                .thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
    }

    @Test
    public void testGetErrorString() {
        assertEquals(
                "mock_string",
                LocationUtils.getErrorString(
                        mockContext, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE));
        assertEquals(
                "mock_string",
                LocationUtils.getErrorString(
                        mockContext, GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES));
        assertEquals(
                "mock_string",
                LocationUtils.getErrorString(
                        mockContext, GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS));
        assertEquals("mock_string", LocationUtils.getErrorString(mockContext, 9999));
    }

    @Test
    public void testGetTransitionString() {
        assertEquals(
                "mock_string",
                LocationUtils.getTransitionString(mockContext, Geofence.GEOFENCE_TRANSITION_ENTER));
        assertEquals(
                "mock_string",
                LocationUtils.getTransitionString(mockContext, Geofence.GEOFENCE_TRANSITION_EXIT));
        assertEquals("mock_string", LocationUtils.getTransitionString(mockContext, 9999));
    }

    @Test
    public void testGetTransitionDetails() {
        List<Geofence> triggeringGeofences = new ArrayList<>();
        Geofence mockGeofence1 = mock(Geofence.class);
        Geofence mockGeofence2 = mock(Geofence.class);
        when(mockGeofence1.getRequestId()).thenReturn("geofence1");
        when(mockGeofence2.getRequestId()).thenReturn("geofence2");
        triggeringGeofences.add(mockGeofence1);
        triggeringGeofences.add(mockGeofence2);

        List<String> result =
                LocationUtils.getTransitionDetails(
                        mockContext, Geofence.GEOFENCE_TRANSITION_ENTER, triggeringGeofences);

        assertEquals(3, result.size());
        assertEquals("mock_string", result.get(0));
        assertEquals("geofence1", result.get(1));
        assertEquals("geofence2", result.get(2));
    }

    @Test
    public void testGetLocation() {
        when(mockSharedPreferences.getLong(eq("mock_string"), anyLong()))
                .thenReturn(Double.doubleToRawLongBits(1.0));
        LatLng result = LocationUtils.getLocation(mockContext);
        assertEquals(1.0, result.latitude, 0.0001);
        assertEquals(1.0, result.longitude, 0.0001);
    }

    @Test
    public void testSetLocation() {
        Location mockLocation = mock(Location.class);
        when(mockLocation.getLatitude()).thenReturn(1.0);
        when(mockLocation.getLongitude()).thenReturn(2.0);

        LocationUtils.setLocation(mockContext, mockLocation);

        verify(mockEditor).putLong(eq("mock_string"), eq(Double.doubleToRawLongBits(1.0)));
        verify(mockEditor).putLong(eq("mock_string"), eq(Double.doubleToRawLongBits(2.0)));
        verify(mockEditor).apply();
    }

    @Test
    public void testSetLocationWithNullLocation() {
        LocationUtils.setLocation(mockContext, null);
        verifyNoInteractions(mockEditor);
    }

    @Test
    public void testGetDistance() {
        LatLng point1 = new LatLng(0, 0);
        LatLng point2 = new LatLng(1, 1);
        double distance = LocationUtils.getDistance(point1, point2);
        assertTrue(distance > 0);
        assertEquals(97.69, distance, 0.01);

        LatLng point3 = new LatLng(0, 0);
        LatLng point4 = new LatLng(0, 0);
        distance = LocationUtils.getDistance(point3, point4);
        assertEquals(0.0, distance, 0.0001);
    }

    @Test
    public void testGetDistanceWithZeroLatitude() {
        LatLng point1 = new LatLng(0, 0);
        LatLng point2 = new LatLng(0, 1);
        double distance = LocationUtils.getDistance(point1, point2);
        assertEquals(0.0, distance, 0.0001);
    }
}
