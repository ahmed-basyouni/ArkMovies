package com.ark.movieapp.presenters.presenterInterfaces;

import android.content.Context;
import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.ark.movieapp.data.exception.AppException;
import com.ark.movieapp.data.model.Trailer;
import com.ark.movieapp.data.model.TrailerResultModel;

import java.util.List;

/**
 *
 * Created by ahmedb on 12/12/16.
 */

public interface TrailerPresenterInterface {

    interface ViewInterface{
        LinearLayout getTrailersContainer();
        void showLoading();
        void hideLoading();
        void showErrorMsg(String msg);
        void hideErrorMsg();
        Context getContext();
        void openYouTube(String url);
        void shareTrailer(Bundle bundle);
    }

    interface PresenterInterface{
        void getMovieTrailer(int id);
        void onSaveInstanceCalled(Bundle bundle);
        void onRestoreInstance(Bundle bundle);
        void onListDownloaded(List<Trailer> trailers);
        void onFail(AppException e);
    }

    interface ModelInterface{
        void getMovieTrailer(int id , PresenterInterface  presenterInterface);
    }
}
