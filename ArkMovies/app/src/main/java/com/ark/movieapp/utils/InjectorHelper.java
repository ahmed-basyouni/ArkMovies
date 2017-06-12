package com.ark.movieapp.utils;

import com.ark.movieapp.R;
import com.ark.movieapp.app.AppModule;
import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.data.model.Movie;
import com.ark.movieapp.data.network.NetworkDispatcher;
import com.ark.movieapp.data.network.NetworkModules;
import com.ark.movieapp.deps.DaggerDeps;
import com.ark.movieapp.deps.Deps;

import javax.inject.Inject;

/**
 *
 * Created by Ark on 6/10/2017.
 */

public class InjectorHelper {

    private static InjectorHelper instance;

    @Inject
    NetworkDispatcher networkDispatcher;
    private Deps deps;

    private InjectorHelper(){

    }

    public static void init(MovieAppApplicationClass aClass){
        instance = new InjectorHelper();
        instance.deps = DaggerDeps.builder()
                .appModule(new AppModule(aClass))
                .networkModules(new NetworkModules(aClass.getString(R.string.base_url)))
                .build();
        instance.deps.inject(instance);
    }

    public NetworkDispatcher getNetworkDispatcher(){
        return networkDispatcher;
    }

    public static InjectorHelper getInstance(){
        if(instance == null)
            throw  new RuntimeException("Call init first");
        return instance;
    }

    public Deps getDeps() {
        return deps;
    }
}
