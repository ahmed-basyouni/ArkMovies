package com.ark.movieapp.presenters.presenterInterfaces;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.ark.movieapp.data.exception.AppException;
import com.ark.movieapp.data.model.Review;

import java.util.List;

/**
 *
 * Created by ahmedb on 12/12/16.
 */

public interface ReviewPresenterInterface {

    interface ViewInterface{
        LinearLayout getReviewContainer();
        void showLoading();
        void hideLoading();
        void showErrorMsg(String msg);
        void hideErrorMsg();
        Context getContext();
    }

    interface PresenterInterface{
        void getMovieReview(int id);
        void onSaveInstanceCalled(Bundle bundle);
        void onRestoreInstance(Bundle bundle);
        void onListDownloaded(List<Review> reviews);
        void onFail(AppException e);
    }

    interface ModelInterface{
        void getMovieReview(int id , ReviewPresenterInterface.PresenterInterface presenterInterface);
    }
}
