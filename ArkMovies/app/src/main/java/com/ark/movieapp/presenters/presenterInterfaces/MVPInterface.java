package com.ark.movieapp.presenters.presenterInterfaces;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ark.movieapp.data.exception.AppException;
import com.ark.movieapp.data.model.Movie;
import com.ark.movieapp.ui.views.MoviesViewHolder;

import java.util.List;

/**
 * 
 * Created by ahmedb on 11/26/16.
 */

public interface MVPInterface {

    int POPULAR_SORT = 0;
    int TOP_RATED_SORT = 1;
    int FAV = 2;

    interface ViewInterface{
        void hideProgress();
        void showLoading();
        void showErrorMsg(String msg);
        void hideErrorMsg();
        RecyclerView getMoviesList();
        Context getContext();
        void startDetailsActivity(Bundle bundle);
        void changeTitle(String title);
        void updateItemAtIndex(int position);
        void highLightFav();
        boolean isTabMode();
        RelativeLayout getContainerLayout();
    }

    interface PresenterInterface{
        void getMovies(boolean userSelection);
        int getItemCount();
        MoviesViewHolder createViewHolder(ViewGroup parent, int viewType);
        void bindViewHolder(MoviesViewHolder holder, int position);
        void OnSuccess(List<Movie> images);
        void onFail(AppException e);
        void onSortingChange(int sortOption);
        void onSaveInstanceCalled(Bundle bundle);
        void onRestoreInstance(Bundle bundle);
        int getHighlightMenuIndex();
        void updateItemAtIndex(int position);
        void scrollingUp();
        void onlyFavIsAvailable();
    }

    interface ModelInterface {
        void getMovies(PresenterInterface presenter, boolean userSelection);

        void changeSort(int sortOption);
        int getType();
    }
}
