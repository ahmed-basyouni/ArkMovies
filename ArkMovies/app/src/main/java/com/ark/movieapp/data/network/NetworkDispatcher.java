package com.ark.movieapp.data.network;

import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.R;
import com.google.gson.JsonElement;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by ahmedb on 7/6/16.
 */
public class NetworkDispatcher {

    private final Retrofit retrofit;

    public NetworkDispatcher(Retrofit retrofit){
        this.retrofit = retrofit;
    }

    public Observable<JsonElement> dispatchRequest(Map<String, String> param , String url){

        ApiEndpointInterface apiService =
                retrofit.create(ApiEndpointInterface.class);


        return apiService.HandleRequest(url, param);

    }

    private interface ApiEndpointInterface {

        @GET
        Observable<JsonElement> HandleRequest(@Url String url, @QueryMap Map<String, String> params);
    }
}
