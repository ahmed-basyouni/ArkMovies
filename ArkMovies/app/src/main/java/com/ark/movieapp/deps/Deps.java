package com.ark.movieapp.deps;

import com.ark.movieapp.app.AppModule;
import com.ark.movieapp.data.network.NetworkDispatcher;
import com.ark.movieapp.data.network.NetworkModules;
import com.ark.movieapp.managers.BaseManager;
import com.ark.movieapp.managers.FavManager;
import com.ark.movieapp.managers.MovieManager;
import com.ark.movieapp.managers.ReviewManager;
import com.ark.movieapp.managers.TrailerManager;
import com.ark.movieapp.presenters.presenterImp.DetailsScreenPresenterImp;
import com.ark.movieapp.presenters.presenterImp.ListFragmentPresenter;
import com.ark.movieapp.presenters.presenterImp.ReviewFragmentPresenter;
import com.ark.movieapp.presenters.presenterImp.TrailerFragmentPresenter;
import com.ark.movieapp.ui.activity.HomeActivity;
import com.ark.movieapp.ui.fragment.HomeFragment;
import com.ark.movieapp.utils.DisplayUtils;
import com.ark.movieapp.utils.InjectorHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 *
 * Created by Ark on 6/10/2017.
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModules.class})
public interface Deps {

    void inject(InjectorHelper injectorHelper);

    void inject(DetailsScreenPresenterImp screenPresenterImp);

    void inject(ListFragmentPresenter listFragmentPresenter);

    void inject(FavManager instance);

    void inject(MovieManager movieManager);

    void inject(ReviewManager reviewManager);

    void inject(TrailerManager trailerManager);

    void inject(ReviewFragmentPresenter reviewFragmentPresenter);

    void inject(DisplayUtils displayUtils);

    void inject(TrailerFragmentPresenter trailerFragmentPresenter);
}
