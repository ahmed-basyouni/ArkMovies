package com.ark.movieapp.managers;

import com.ark.movieapp.BuildConfig;
import com.ark.movieapp.R;
import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.data.exception.AppException;
import com.ark.movieapp.data.model.TrailerResultModel;
import com.ark.movieapp.data.network.NetworkListener;
import com.ark.movieapp.presenters.presenterInterfaces.TrailerPresenterInterface;
import com.ark.movieapp.utils.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmedb on 12/12/16.
 */

public class TrailerManager extends BaseManager implements TrailerPresenterInterface.ModelInterface, NetworkListener<TrailerResultModel> {


    private static TrailerManager instance;
    private TrailerPresenterInterface.PresenterInterface mPresenter;

    public TrailerManager() {
        super(TrailerResultModel.class);
    }

    public static TrailerManager getInstance(){

        if(instance == null)
            instance = new TrailerManager();
        return instance;
    }

    @Override
    public void getMovieTrailer(int id, TrailerPresenterInterface.PresenterInterface presenterInterface) {
        this.mPresenter = presenterInterface;

        if(NetworkUtils.isNetworkConnected()) {

            getTrailersFromServer(id);

        }else{

            mPresenter.onFail(new AppException(AppException.NETWORK_EXCEPTION));
        }
    }

    private void getTrailersFromServer(final int id) {

        MovieAppApplicationClass.getInstance().runInBackGround(new Runnable() {
            @Override
            public void run() {


                Map<String, String> param = new HashMap<>();
                param.put(MovieAppApplicationClass.getInstance().getString(R.string.api_key_param), BuildConfig.API_KEY);
                getObject(String.valueOf(id) + MovieAppApplicationClass.getInstance().getString(R.string.trailerUrl),param,TrailerManager.this);

            }
        });
    }

    @Override
    public void onSuccess(TrailerResultModel model) {
        mPresenter.onListDownloaded(model.getTrailers());
    }

    @Override
    public void onError(Throwable t) {
        if(t instanceof Exception)
            mPresenter.onFail(AppException.getAppException((Exception)t));
    }
}
