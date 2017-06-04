package com.ark.movieapp.data.network;

import com.ark.movieapp.data.model.BaseEntity;

/**
 *
 * Created by ahmedb on 12/11/16.
 */

public interface NetworkListener<T extends BaseEntity> {

    void onSuccess(T model);

    void onError(Throwable t);
}
