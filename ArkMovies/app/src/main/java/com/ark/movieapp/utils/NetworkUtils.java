package com.ark.movieapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.ark.movieapp.app.MovieAppApplicationClass;

/**
 * a class used to get the phone network state whether it's connected or not
 * Created by ahmedb on 11/1/16.
 */

public class NetworkUtils {

    public static boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) MovieAppApplicationClass.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
