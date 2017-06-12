package com.ark.movieapp.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;

import com.ark.movieapp.idleresources.SimpleIdlingResource;
import com.ark.movieapp.utils.InjectorHelper;

/**
 *
 * Created by ahmedb on 12/1/16.
 */

public class MovieAppApplicationClass extends Application{

    public static MovieAppApplicationClass instance;

    @Nullable
    public SimpleIdlingResource mIdlingResource;

    //init the executor service and make a static context instance
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        InjectorHelper.init(this);
    }

    /**
     * A method to return the static context
     * @return MovieAppApplicationClass
     */
    public static MovieAppApplicationClass getInstance(){
        return instance;
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

}
