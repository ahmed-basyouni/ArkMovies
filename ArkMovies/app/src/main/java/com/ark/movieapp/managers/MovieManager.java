package com.ark.movieapp.managers;

import android.content.Context;

import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.BuildConfig;
import com.ark.movieapp.data.exception.AppException;
import com.ark.movieapp.data.model.ResultModel;
import com.ark.movieapp.data.network.NetworkListener;
import com.ark.movieapp.data.cache.SharedPrefrencesDataLayer;
import com.ark.movieapp.R;
import com.ark.movieapp.presenters.presenterInterfaces.MVPInterface;
import com.ark.movieapp.utils.InjectorHelper;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 *
 * Created by ahmedb on 12/1/16.
 */

public class MovieManager extends BaseManager implements MVPInterface.ModelInterface, NetworkListener<ResultModel> {

    public static final String SEARCH_BY = "searchBy";

    private static MovieManager instance;
    private MVPInterface.PresenterInterface presenter;
    private boolean userSelection;
    @Inject
    MovieAppApplicationClass applicationClass;

    public MovieManager() {
        super(ResultModel.class);
        InjectorHelper.getInstance().getDeps().inject(this);
    }

    public static MovieManager getInstance() {

        if (instance == null)
            instance = new MovieManager();
        return instance;
    }

    @Override
    public void getMovies(MVPInterface.PresenterInterface presenter, boolean userSelection) {

        this.userSelection = userSelection;

        int type = getType();

        if (isNetworkConnected()) {

            if (type != MVPInterface.FAV)
                getMoviesFromServer(type, presenter);
            else
                getFavMovies(presenter);

        } else {

            if (FavManager.getInstance().isFavNotEmpty() && (type == MVPInterface.FAV || !userSelection)) {

                getFavMovies(presenter);
                presenter.onlyFavIsAvailable();
                changeSort(MVPInterface.FAV);

            } else
                presenter.onFail(new AppException(AppException.NETWORK_EXCEPTION));
        }

    }

    private void getFavMovies(MVPInterface.PresenterInterface presenter) {

        FavManager.getInstance().getFavList(presenter);
    }

    private void getMoviesFromServer(final int type, final MVPInterface.PresenterInterface presenter) {

        this.presenter = presenter;
        final String url = createURL(type);
        Map<String, String> param = new HashMap<>();
        param.put(applicationClass.getString(R.string.api_key_param), BuildConfig.API_KEY);
        getObject(url, param, MovieManager.this);

    }

    private String createURL(int type) {
        Context context = applicationClass;
        return type == 0 ? context.getString(R.string.popularSort) : context.getString(R.string.topRatedSort);
    }

    @Override
    public int getType() {

        int type = SharedPrefrencesDataLayer.getIntPreferences(applicationClass.getApplicationContext()
                , SEARCH_BY, 0);

        if (type == MVPInterface.FAV && !FavManager.getInstance().isFavNotEmpty() && !userSelection) {
            changeSort(MVPInterface.POPULAR_SORT);
            return 0;
        }
        return type;
    }

    @Override
    public void changeSort(int sortOption) {
        SharedPrefrencesDataLayer.saveIntPreferences(applicationClass.getApplicationContext()
                , SEARCH_BY, sortOption);

    }

    @Override
    public void onSuccess(ResultModel model) {
        presenter.OnSuccess(model.getMovies());
    }

    @Override
    public void onError(Throwable t) {
        if (t instanceof Exception)
            presenter.onFail(AppException.getAppException((Exception) t));
    }
}
