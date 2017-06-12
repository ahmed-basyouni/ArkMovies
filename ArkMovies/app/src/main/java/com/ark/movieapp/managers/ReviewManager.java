package com.ark.movieapp.managers;

import com.ark.movieapp.BuildConfig;
import com.ark.movieapp.R;
import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.data.exception.AppException;
import com.ark.movieapp.data.model.ReviewResultModel;
import com.ark.movieapp.data.network.NetworkListener;
import com.ark.movieapp.presenters.presenterInterfaces.ReviewPresenterInterface;
import com.ark.movieapp.utils.InjectorHelper;
import com.ark.movieapp.utils.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 *
 * Created by ahmedb on 12/13/16.
 */

public class ReviewManager extends BaseManager implements ReviewPresenterInterface.ModelInterface, NetworkListener<ReviewResultModel> {


    private static ReviewManager instance;
    private ReviewPresenterInterface.PresenterInterface mPresenter;

    @Inject
    MovieAppApplicationClass applicationClass;

    public static ReviewManager getInstance() {

        if (instance == null)
            instance = new ReviewManager();
        return instance;
    }

    public ReviewManager() {
        super(ReviewResultModel.class);
        InjectorHelper.getInstance().getDeps().inject(this);
    }

    @Override
    public void getMovieReview(int id, ReviewPresenterInterface.PresenterInterface presenterInterface) {

        this.mPresenter = presenterInterface;

        if (NetworkUtils.isNetworkConnected()) {

            getTrailersFromServer(id);

        } else {

            mPresenter.onFail(new AppException(AppException.NETWORK_EXCEPTION));
        }
    }

    private void getTrailersFromServer(final int id) {

        Map<String, String> param = new HashMap<>();
        param.put(applicationClass.getString(R.string.api_key_param), BuildConfig.API_KEY);
        getObject(String.valueOf(id) + applicationClass.getString(R.string.reviewsUrl), param, ReviewManager.this);

    }

    @Override
    public void onSuccess(ReviewResultModel model) {
        mPresenter.onListDownloaded(model.getReviews());
    }

    @Override
    public void onError(Throwable t) {
        if (t instanceof Exception)
            mPresenter.onFail(AppException.getAppException((Exception) t));
    }
}
