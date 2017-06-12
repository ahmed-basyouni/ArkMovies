package com.ark.movieapp.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.data.cache.MovieContentProvider;
import com.ark.movieapp.data.cache.MovieDataBaseHelper;
import com.ark.movieapp.data.model.Movie;
import com.ark.movieapp.presenters.presenterInterfaces.MVPInterface;
import com.ark.movieapp.utils.InjectorHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *
 * Created by ahmedb on 12/13/16.
 */

public class FavManager implements Loader.OnLoadCompleteListener<Cursor>{

    private static FavManager instance;

    @Inject
    MovieAppApplicationClass context;

    private static final int LOADER_ID = 1;
    private CursorLoader cursorLoader;
    private MVPInterface.PresenterInterface mPresenter;

    public static FavManager getInstance(){
        if(instance == null) {
            instance = new FavManager();
            InjectorHelper.getInstance().getDeps().inject(instance);
        }
        return instance;
    }

    private ContentValues getContentFromMovieModel(Movie movie) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieDataBaseHelper.MOVIE_ID , movie.getId());
        contentValues.put(MovieDataBaseHelper.MOVIE_NAME, movie.getTitle());
        contentValues.put(MovieDataBaseHelper.MOVIE_POSTER_URL, movie.posterURL);
        contentValues.put(MovieDataBaseHelper.OVERVIEW, movie.getOverView());
        contentValues.put(MovieDataBaseHelper.RATE, movie.getRate());
        contentValues.put(MovieDataBaseHelper.RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieDataBaseHelper.MOVIE_BANNER_URL, movie.bannerURL);
        contentValues.put(MovieDataBaseHelper.MOVIE_VOTE_NUMBER, movie.getVoteNumber());
        contentValues.put(MovieDataBaseHelper.IS_FAV, movie.isFav() ? 1 : 0);

        return contentValues;
    }

    private Movie getMovieModelFromCursor(Cursor cursor) {

        Movie movieModel = new Movie();

        movieModel.setId(cursor.getInt(cursor.getColumnIndex(MovieDataBaseHelper.MOVIE_ID)));
        movieModel.setTitle(cursor.getString(cursor.getColumnIndex(MovieDataBaseHelper.MOVIE_NAME)));
        movieModel.setPosterURL(cursor.getString(cursor.getColumnIndex(MovieDataBaseHelper.MOVIE_POSTER_URL)));
        movieModel.setOverView(cursor.getString(cursor.getColumnIndex(MovieDataBaseHelper.OVERVIEW)));
        movieModel.setRate(cursor.getFloat(cursor.getColumnIndex(MovieDataBaseHelper.RATE)));
        movieModel.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieDataBaseHelper.RELEASE_DATE)));
        movieModel.setBannerURL(cursor.getString(cursor.getColumnIndex(MovieDataBaseHelper.MOVIE_BANNER_URL)));
        movieModel.setVoteNumber(cursor.getString(cursor.getColumnIndex(MovieDataBaseHelper.MOVIE_VOTE_NUMBER)));
        movieModel.setFav(cursor.getInt(cursor.getColumnIndex(MovieDataBaseHelper.IS_FAV)) == 1);

        return movieModel;
    }

    public void changeFav(final Movie movieModel) {

        Observable.create(subscriber -> {
            if(movieModel.isFav()){

                ContentValues contentValues = getContentFromMovieModel(movieModel);

                context.getContentResolver().insert(
                        MovieContentProvider.CONTENT_URI, contentValues);

            }else{

                Uri uri = Uri.parse(MovieContentProvider.CONTENT_URI + "/"
                        + movieModel.getId());

                context.getContentResolver().delete(uri, null, null);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public void getFavList(MVPInterface.PresenterInterface presenter){

        this.mPresenter = presenter;
        cursorLoader = new CursorLoader(context, MovieContentProvider.CONTENT_URI, MovieContentProvider.AVAILABLE_COLUMNS, null, null, null);
        cursorLoader.registerListener(LOADER_ID , FavManager.this);
        cursorLoader.startLoading();
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {

        Observable.create((Observable.OnSubscribe<List<Movie>>) subscriber -> {
            List<Movie> favMovies = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Movie movieModel = getMovieModelFromCursor(cursor);
                favMovies.add(movieModel);
                cursor.moveToNext();
            }
            subscriber.onNext(favMovies);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Movie>>() {
            @Override
            public void onCompleted() {
                // make sure to close the cursor
                cursor.close();

                cursorLoader.unregisterListener(FavManager.this);
                cursorLoader.cancelLoad();
                cursorLoader.stopLoading();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Movie> movies) {
                FavManager.this.mPresenter.OnSuccess(movies);
            }
        });
    }

    public boolean isFav(int id){

        Uri uri = Uri.parse(MovieContentProvider.CONTENT_URI + "/"
                + id);

        Cursor cursor = context.getContentResolver().query(uri , null ,null, null, null);

        if(cursor != null) {

            boolean exists = (cursor.getCount() > 0);
            cursor.close();
            return exists;
        }

        return false;
    }

    public boolean isFavNotEmpty(){
        Cursor cursor = context.getContentResolver().query(MovieContentProvider.CONTENT_URI, null ,null, null, null);

        if(cursor != null) {

            boolean notEmpty = (cursor.getCount() > 0);
            cursor.close();
            return notEmpty;
        }

        return false;

    }
}
