package com.ark.movieapp.presenters.presenterImp;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.RelativeLayout;

import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.data.listeners.UpdateHomeItem;
import com.ark.movieapp.data.model.Movie;
import com.ark.movieapp.R;
import com.ark.movieapp.managers.FavManager;
import com.ark.movieapp.presenters.presenterInterfaces.DetailsScreenPresenterInterface;
import com.ark.movieapp.utils.DisplayUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by ahmedb on 11/28/16.
 */

public class DetailsScreenPresenterImp implements DetailsScreenPresenterInterface.DetailsPresenter{

    private final DetailsScreenPresenterInterface.DetailsView mView;

    public DetailsScreenPresenterImp(DetailsScreenPresenterInterface.DetailsView view){
        this.mView = view;
    }

    @Override
    public void getInfoAndSetupUI(final Bundle bundle) {

        final Movie movieModel = bundle.getParcelable("Obj");

        MovieAppApplicationClass.getInstance().getImageLoader().displayImage(movieModel.getBannerURL(),mView.getBanner(), MovieAppApplicationClass.getInstance().getDisplayOptions(),new SimpleImageLoadingListener(){

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
                mView.getProgressBar().setVisibility(View.VISIBLE);
                mView.getBanner().setImageResource(R.drawable.placeholder);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                super.onLoadingComplete(imageUri, view, bitmap);

                mView.getProgressBar().setVisibility(View.GONE);
                mView.getBanner().setImageBitmap(bitmap);

                Palette palette = Palette.from(bitmap).generate();

                List<Palette.Swatch> swatches = palette.getSwatches();

                if(swatches != null && !swatches.isEmpty()) {

                    Palette.Swatch swatch = swatches.get(0);

                    mView.setContentColor(swatch.getRgb());
                    mView.setCollapseTitleColor(swatch.getTitleTextColor());

                    if (Build.VERSION.SDK_INT >= 21 && !bundle.getBoolean("tablet")) {

                        mView.setStatusBarColor(swatch.getRgb());
                    }
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                super.onLoadingCancelled(imageUri, view);
            }
        });

        mView.setTitle(movieModel.getTitle());

        MovieAppApplicationClass.getInstance().getImageLoader().displayImage(movieModel.getPosterURL() , mView.getPoster() , MovieAppApplicationClass.getInstance().getDisplayOptions());

        mView.setReleaseDate(movieModel.getReleaseDate());
        mView.setRating(movieModel.getRate() / 2);
        mView.setOverView(movieModel.getOverView());
        mView.setRatingNumber(movieModel.getVoteNumber());

        DisplayUtils displayUtils = new DisplayUtils();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mView.getPoster().getLayoutParams();

        boolean isPortrait = mView.getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;


        params.width = displayUtils.getDisplayWidth() / (bundle.getBoolean("tablet") ? 4 : 2);
        params.height = displayUtils.getDisplayHeight() / (bundle.getBoolean("tablet") ? 4 : 2);

        mView.getPoster().setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) mView.getDetailsContainer().getLayoutParams();

        params.width = displayUtils.getDisplayWidth() / (bundle.getBoolean("tablet") ? 4 : 2);
        params.height = displayUtils.getDisplayHeight() / (bundle.getBoolean("tablet") ? 4 : 2);

        mView.getDetailsContainer().setLayoutParams(params);

        if(movieModel.isFav()){

            mView.getFloatngButon().setImageResource(R.drawable.rating_star_sc);
        }else{

            mView.getFloatngButon().setImageResource(R.drawable.rating_star);
        }

        mView.getFloatngButon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (movieModel.isFav()) {
                    mView.getFloatngButon().setImageResource(R.drawable.rating_star);
                    movieModel.setFav(false);
                    FavManager.getInstance().changeFav(movieModel);

                } else {

                    mView.getFloatngButon().setImageResource(R.drawable.rating_star_sc);
                    movieModel.setFav(true);
                    FavManager.getInstance().changeFav(movieModel);
                }

                mView.getListener().updateItemAtIndex(bundle.getInt("position"));
            }
        });
    }

    @Override
    public void viewWillDetach() {
        if(Build.VERSION.SDK_INT >= 21)
            mView.setStatusBarColor(ContextCompat.getColor(MovieAppApplicationClass.getInstance().getApplicationContext() , R.color.colorPrimaryDark));
    }
}
