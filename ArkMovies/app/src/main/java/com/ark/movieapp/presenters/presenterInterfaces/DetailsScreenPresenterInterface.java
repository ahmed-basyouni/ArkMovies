package com.ark.movieapp.presenters.presenterInterfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ark.movieapp.data.listeners.UpdateHomeItem;

/**
 * Created by ahmedb on 11/28/16.
 */

public interface DetailsScreenPresenterInterface {

    interface DetailsView{
        void setContentColor(int color);
        void setCollapseTitleColor(int color);
        void setStatusBarColor(int color);
        void setTitle(String title);
        ImageView getBanner();
        ImageView getPoster();
        ProgressBar getProgressBar();
        LinearLayout getDetailsContainer();
        void setRating(float rating);
        void setReleaseDate(String releaseDate);
        void setOverView(String overView);
        void setRatingNumber(String voteNumber);
        Context getContext();
        FloatingActionButton getFloatngButon();
        UpdateHomeItem getListener();
        void hideToolBar();
    }

    interface DetailsPresenter{
        void getInfoAndSetupUI(Bundle bundle);
        void viewWillDetach();
    }
}
