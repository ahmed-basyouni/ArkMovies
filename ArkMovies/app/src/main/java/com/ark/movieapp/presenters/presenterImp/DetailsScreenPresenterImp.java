package com.ark.movieapp.presenters.presenterImp;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.RelativeLayout;

import com.ark.android.rxbinding.rxpalettle.PaletteConsumer;
import com.ark.android.rxbinding.rxpalettle.RxPalette;
import com.ark.android.rxbinding.rxuil.RxUILObservableBuilder;
import com.ark.android.rxbinding.rxuil.UILConsumer;
import com.ark.movieapp.R;
import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.data.model.Movie;
import com.ark.movieapp.managers.FavManager;
import com.ark.movieapp.presenters.presenterInterfaces.DetailsScreenPresenterInterface;
import com.ark.movieapp.utils.DisplayUtils;
import com.ark.movieapp.utils.InjectorHelper;
import com.jakewharton.rxbinding2.view.RxView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * Created by ahmedb on 11/28/16.
 */

public class DetailsScreenPresenterImp implements DetailsScreenPresenterInterface.DetailsPresenter {

    @Inject
    ImageLoader imageLoader;
    @Inject
    DisplayImageOptions displayImageOptions;
    @Inject
    MovieAppApplicationClass applicationClass;

    private final DetailsScreenPresenterInterface.DetailsView mView;

    public DetailsScreenPresenterImp(DetailsScreenPresenterInterface.DetailsView view) {
        this.mView = view;
        InjectorHelper.getInstance().getDeps().inject(this);
    }

    @Override
    public void getInfoAndSetupUI(final Bundle bundle) {

        final Movie movieModel = bundle.getParcelable("Obj");

        RxUILObservableBuilder.builder()
                .imageLoader(imageLoader)
                .displayOptions(displayImageOptions)
                .preview(mView.getBanner())
                .url(movieModel.getBannerURL())
                .subscribe(new UILConsumer() {
                    @Override
                    public void onNext(RxUILObservableBuilder.RXUILObject value) {
                        mView.getProgressBar().setVisibility(View.GONE);
                        mView.getBanner().setImageBitmap(value.getLoadedImage());
                        getPalette(value.getLoadedImage(), bundle);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.getProgressBar().setVisibility(View.GONE);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mView.getProgressBar().setVisibility(View.VISIBLE);
                        mView.getBanner().setImageResource(R.drawable.placeholder);
                    }
                });

        mView.setTitle(movieModel.getTitle());

        RxUILObservableBuilder.builder()
                .imageLoader(imageLoader)
                .displayOptions(displayImageOptions)
                .preview(mView.getPoster())
                .url(movieModel.getPosterURL())
                .subscribe();

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

        if (movieModel.isFav()) {

            mView.getFloatngButon().setImageResource(R.drawable.rating_star_sc);
        } else {

            mView.getFloatngButon().setImageResource(R.drawable.rating_star);
        }

        RxView.clicks(mView.getFloatngButon()).subscribe(o -> {
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
        });

    }

    private void getPalette(Bitmap loadedImage, Bundle bundle) {
        RxPalette.analyze(loadedImage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PaletteConsumer() {
                    @Override
                    public void onNext(List<Palette.Swatch> swatches) {

                        Palette.Swatch swatch = swatches.get(0);

                        mView.setContentColor(swatch.getRgb());
                        mView.setCollapseTitleColor(swatch.getTitleTextColor());

                        if (Build.VERSION.SDK_INT >= 21 && !bundle.getBoolean("tablet")) {
                            mView.setStatusBarColor(swatch.getRgb());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void viewWillDetach() {
        if (Build.VERSION.SDK_INT >= 21)
            mView.setStatusBarColor(ContextCompat.getColor(applicationClass.getApplicationContext(), R.color.colorPrimaryDark));
    }
}
