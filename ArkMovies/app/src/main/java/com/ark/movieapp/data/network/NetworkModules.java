package com.ark.movieapp.data.network;

import com.ark.movieapp.R;
import com.ark.movieapp.app.MovieAppApplicationClass;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * Created by Ark on 6/10/2017.
 */

@Module
public class NetworkModules {

    private final String baseUrl;

    public NetworkModules(String baseUrl){
        this.baseUrl = baseUrl;
    }

    @Singleton
    @Provides
    public OkHttpClient provideHttpClient(){
        return new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30 , TimeUnit.SECONDS)
                .build();
    }

    @Singleton
    @Provides
    public Retrofit provideRetroFit(OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    public NetworkDispatcher provideNetworkDispatcher(Retrofit retrofit){
        return new NetworkDispatcher(retrofit);
    }
}
