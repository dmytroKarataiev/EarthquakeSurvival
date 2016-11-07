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

package com.adkdevelopment.earthquakesurvival.data.remote;

import android.content.Context;

import com.adkdevelopment.earthquakesurvival.App;
import com.adkdevelopment.earthquakesurvival.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * REST Manager using Singleton Pattern
 */
public class ApiManager {

    private final String BASE_URL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/";

    private final Retrofit EARTHQUAKE_ADAPTER = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(new ToStringConverterFactory())
            .client(new OkHttpClient.Builder()
                    .sslSocketFactory(getSSLConfig(App.getContext()).getSocketFactory()).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final EarthquakeService EARTHQUAKE_SERVICE = EARTHQUAKE_ADAPTER.create(EarthquakeService.class);

    public ApiManager() throws Exception {
    }

    public EarthquakeService getEarthquakeService() {
        return EARTHQUAKE_SERVICE;
    }

    private final Retrofit NEWS_ADAPTER = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();

    private final EarthquakeService NEWS_SERVICE = NEWS_ADAPTER.create(EarthquakeService.class);

    public EarthquakeService getNewsService() {
        return NEWS_SERVICE;
    }

    private static SSLContext getSSLConfig(Context context) throws CertificateException, IOException,
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        // Loading CAs from an InputStream
        CertificateFactory cf = null;
        cf = CertificateFactory.getInstance("X.509");

        Certificate ca;
        // I'm using Java7. If you used Java6 close it manually with finally.
        InputStream cert = context.getResources().openRawResource(R.raw.usgs);
        ca = cf.generateCertificate(cert);

        // Creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] trustManagers = tmf.getTrustManagers();
        final X509TrustManager origTrustmanager = (X509TrustManager) trustManagers[0];

        TrustManager[] wrappedTrustManagers = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        origTrustmanager.checkClientTrusted(x509Certificates, s);
                    }

                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return origTrustmanager.getAcceptedIssuers();
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            origTrustmanager.checkServerTrusted(certs, authType);
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }
                    }
                }
        };

        // Creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext;
    }
}
