# Earthquake Survival
Final Nanodegree Project

## Project Schema
* [Detailed overview of the proposed application](Capstone_Stage1.pdf)

## Required Components
* [Third Party Libraries](https://github.com/dmytroKarataiev/EarthquakeSurvival/blob/34f24be78184e83985284d44d99b91444b41257d/app/build.gradle#L62)
* Accessibility features
* [Widget with meaningful information](app/src/main/java/com/adkdevelopment/earthquakesurvival/widget)
* Google Services:
  * [Locations](https://github.com/dmytroKarataiev/EarthquakeSurvival/blob/34f24be78184e83985284d44d99b91444b41257d/app/src/main/java/com/adkdevelopment/earthquakesurvival/PagerActivity.java#L117)
  * [Maps](app/src/main/java/com/adkdevelopment/earthquakesurvival/MapviewFragment.java)
* [Gradle with signing configuration](app/build.gradle)
* [ContentProvider](https://github.com/dmytroKarataiev/EarthquakeSurvival/blob/34f24be78184e83985284d44d99b91444b41257d/app/src/main/java/com/adkdevelopment/earthquakesurvival/provider/EarthquakeProvider.java)
* [SyncAdapter](https://github.com/dmytroKarataiev/EarthquakeSurvival/blob/34f24be78184e83985284d44d99b91444b41257d/app/src/main/java/com/adkdevelopment/earthquakesurvival/syncadapter/SyncAdapter.java#L63)
* [Loader to move data to views](https://github.com/dmytroKarataiev/EarthquakeSurvival/blob/34f24be78184e83985284d44d99b91444b41257d/app/src/main/java/com/adkdevelopment/earthquakesurvival/RecentFragment.java#L88)
* [SharedTransitions](app/src/main/java/com/adkdevelopment/earthquakesurvival/RecentAdapter.java)
* Parallax Scrolling
* [Notifications](https://github.com/dmytroKarataiev/EarthquakeSurvival/blob/b7d081d0fafe7db03648ba55b8dd22326ddfca5a/app/src/main/java/com/adkdevelopment/earthquakesurvival/geofence/GeofenceService.java#L97)
* [ShareActionProvider](app/src/main/java/com/adkdevelopment/earthquakesurvival/DetailFragment.java)
* [Broadcast Events](https://github.com/dmytroKarataiev/EarthquakeSurvival/blob/d70d8f53387c5aac9ad0d7df337542722549e9d9/app/src/main/AndroidManifest.xml#L120)
* Custom Views

## Important information
* For the app to work you need to add Google Maps Api key to the apikeys.xml file in values folder <br>
String should look like this: \<string name="google.maps.api.key.release">YOUR API KEY\</string>