package com.ark.movieapp.data.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ahmedb on 11/26/16.
 */

public class NetworkHandler {

    public static InputStream callWebService(String url) throws IOException {

        HttpURLConnection urlConnection = null;
        URL uri = new URL(url);
        urlConnection = (HttpURLConnection) uri.openConnection();

        return urlConnection.getInputStream();
    }
}
