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

License
-------

	The MIT License (MIT)

	Copyright (c) 2016 Dmytro Karataiev

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.