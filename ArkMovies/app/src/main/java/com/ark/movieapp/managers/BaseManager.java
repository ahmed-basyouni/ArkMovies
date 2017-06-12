package com.ark.movieapp.managers;

import com.ark.movieapp.data.model.BaseEntity;
import com.ark.movieapp.data.network.NetworkDispatcher;
import com.ark.movieapp.data.network.NetworkListener;
import com.ark.movieapp.utils.InjectorHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Map;

import rx.Observer;

/**
 *
 * Created by ahmedb on 12/11/16.
 */

public class BaseManager<T extends BaseEntity> implements Observer<JsonElement>{


    private Class<T> mClass;
    private NetworkListener<T> wrappedCallback;
    private NetworkDispatcher networkDispatcher;

    BaseManager(Class<T> modelClass){

        this.mClass = modelClass;
        networkDispatcher = InjectorHelper.getInstance().getNetworkDispatcher();
    }

    void getObject(String url , Map<String,String> param ,  NetworkListener<T> wrappedCallback){

        this.wrappedCallback = wrappedCallback;
        networkDispatcher.dispatchRequest(param,url,this);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

        wrappedCallback.onError(e);
    }

    @Override
    public void onNext(JsonElement model) {

        Gson gson = new Gson();
        wrappedCallback.onSuccess(gson.fromJson(gson.toJson(model.getAsJsonObject()), mClass));
    }
}
