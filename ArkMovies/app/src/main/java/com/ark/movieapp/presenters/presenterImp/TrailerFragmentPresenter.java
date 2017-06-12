package com.ark.movieapp.presenters.presenterImp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ark.android.rxbinding.rxuil.RxUILObservableBuilder;
import com.ark.android.rxbinding.rxuil.UILConsumer;
import com.ark.movieapp.R;
import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.data.exception.AppException;
import com.ark.movieapp.data.model.Trailer;
import com.ark.movieapp.managers.TrailerManager;
import com.ark.movieapp.presenters.presenterInterfaces.TrailerPresenterInterface;
import com.ark.movieapp.utils.InjectorHelper;
import com.jakewharton.rxbinding2.view.RxView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by ahmedb on 12/12/16.
 */

public class TrailerFragmentPresenter implements TrailerPresenterInterface.PresenterInterface {

    private TrailerPresenterInterface.ViewInterface mView;
    private final String LIST_KEY = "trailerList";
    private List<Trailer> trailers;
    private int movieId;

    @Inject
    ImageLoader imageLoader;
    @Inject
    DisplayImageOptions displayImageOptions;
    @Inject
    MovieAppApplicationClass applicationClass;

    public TrailerFragmentPresenter(TrailerPresenterInterface.ViewInterface viewInterface) {
        this.mView = viewInterface;
        InjectorHelper.getInstance().getDeps().inject(this);
    }

    @Override
    public void getMovieTrailer(int id) {
        this.movieId = id;
        mView.showLoading();
        TrailerManager.getInstance().getMovieTrailer(id, this);
    }

    public void setView(TrailerPresenterInterface.ViewInterface mView) {
        this.mView = mView;
    }

    private void shareTrailerAtIndex(int index) {

        String url = trailers.get(index).getTrailerUrl();
        String title = trailers.get(index).getTitle();

        Bundle bundle = new Bundle();
        bundle.putString(Intent.EXTRA_SUBJECT, title);
        bundle.putString(Intent.EXTRA_TEXT, url);

        mView.shareTrailer(bundle);
    }

    private void playTrailerAtIndex(int index) {

        String url = trailers.get(index).getTrailerUrl();
        mView.openYouTube(url);
    }


    @Override
    public void onSaveInstanceCalled(Bundle bundle) {
        bundle.putParcelableArrayList(LIST_KEY, (ArrayList<? extends Parcelable>) trailers);
    }

    @Override
    public void onRestoreInstance(Bundle bundle) {
        trailers = bundle.getParcelableArrayList(LIST_KEY);
        if (trailers != null && !trailers.isEmpty())
            if (mView != null && mView.getContext() != null)
                setAdapter();
            else
                getMovieTrailer(movieId);
    }

    @Override
    public void onListDownloaded(final List<Trailer> trailers) {
        this.trailers = trailers;
        mView.hideLoading();
        if (trailers == null || trailers.isEmpty())
            onFail(new AppException(AppException.NO_DATA_EXCEPTION));
        else {
            mView.hideErrorMsg();
            if (mView != null && mView.getContext() != null)
                setAdapter();
        }
    }

    private void setAdapter() {

        for (int x = 0; x < trailers.size(); x++) {
            View rootView = LayoutInflater.from(mView.getContext()).inflate(R.layout.trailer_item, mView.getTrailersContainer(), false);

            final TrailerItem trailerItem = new TrailerItem(rootView);

            final int finalX = x;
            RxView.clicks(rootView).subscribe(o -> {
                playTrailerAtIndex(finalX);
            });

            RxView.clicks(trailerItem.shareButton).subscribe(o -> {
                shareTrailerAtIndex(finalX);
            });

            RxUILObservableBuilder.builder().preview(trailerItem.trailerImage)
                    .url(trailers.get(x).getTrailerImageUrl())
                    .imageLoader(imageLoader)
                    .displayOptions(displayImageOptions)
                    .subscribe(new UILConsumer() {
                        @Override
                        public void onNext(RxUILObservableBuilder.RXUILObject value) {
                            trailerItem.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            trailerItem.progressBar.setVisibility(View.GONE);
                        }
                    });

            mView.getTrailersContainer().addView(rootView);
        }
    }

    @Override
    public void onFail(final AppException e) {
        mView.hideLoading();
        mView.showErrorMsg(e.getMessage());
    }


    class TrailerItem {

        @BindView(R.id.trailerImage)
        public ImageView trailerImage;
        @BindView(R.id.progressBar)
        public ProgressBar progressBar;
        @BindView(R.id.shareButton)
        public ImageButton shareButton;

        public TrailerItem(View rootView) {

            ButterKnife.bind(this, rootView);
        }
    }
}
