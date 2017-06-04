package com.ark.movieapp.presenters.presenterImp;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.data.exception.AppException;
import com.ark.movieapp.data.model.Movie;
import com.ark.movieapp.R;
import com.ark.movieapp.managers.FavManager;
import com.ark.movieapp.ui.adapter.ListAdapterRecycle;
import com.ark.movieapp.ui.views.MoviesViewHolder;
import com.ark.movieapp.managers.MovieManager;
import com.ark.movieapp.presenters.presenterInterfaces.MVPInterface;
import com.ark.movieapp.utils.DisplayUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ahmedb on 11/26/16.
 */

public class ListFragmentPresenter implements MVPInterface.PresenterInterface {

    private final String LIST_KEY = "moviesList";
    private final String SCROLLED_ITEMS = "scrolledItems";

    private MVPInterface.ViewInterface mView;

    private List<Movie> movies;
    private boolean scrollingDown;

    HashMap<Integer, Boolean> itemsScrolled = new HashMap<>();
    private ListAdapterRecycle adapterRecycle;

    public ListFragmentPresenter(MVPInterface.ViewInterface view) {
        this.mView = view;
    }

    private void initAnimationsItems() {
        for (int x = 0; x < movies.size(); x++) {

            itemsScrolled.put(x, false);
        }
    }

    @Override
    public void getMovies(boolean userSelection) {
        mView.showLoading();
        MovieManager.getInstance().getMovies(this, userSelection);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public MoviesViewHolder createViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mView.getContext());

        View rootView = inflater.inflate(R.layout.movie_list_item, parent, false);

        return new MoviesViewHolder(rootView);
    }

    @Override
    public void bindViewHolder(final MoviesViewHolder holder, final int position) {

        if (scrollingDown && !itemsScrolled.get(position)) {

            Animation animation = AnimationUtils.loadAnimation(mView.getContext(), R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
            itemsScrolled.put(position, true);

        }

        CardView.LayoutParams params = (CardView.LayoutParams) holder.flickerImageContainer.getLayoutParams();

        DisplayUtils displayUtils = new DisplayUtils();

        boolean isPortrait = mView.getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;

        params.width = displayUtils.getDisplayWidth() / (mView.isTabMode() ? 4 : 2);
        params.height = displayUtils.getDisplayHeight() / (isPortrait && mView.isTabMode() ? 4 : 2);

        holder.flickerImageContainer.setLayoutParams(params);


        ((CardView) holder.flickerImageContainer.getParent()).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                openDetailsAtIndex(position);
            }
        });

        if (FavManager.getInstance().isFav(movies.get(position).getId())) {
            movies.get(position).setFav(true);
            holder.favBtn.setImageResource(R.drawable.rating_star_sc);
        } else {
            movies.get(position).setFav(false);
            holder.favBtn.setImageResource(R.drawable.rating_star);
        }
        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (movies.get(position).isFav()) {
                    holder.favBtn.setImageResource(R.drawable.rating_star);
                    movies.get(position).setFav(false);
                    FavManager.getInstance().changeFav(movies.get(position));

                } else {

                    holder.favBtn.setImageResource(R.drawable.rating_star_sc);
                    movies.get(position).setFav(true);
                    FavManager.getInstance().changeFav(movies.get(position));
                }
            }
        });

        MovieAppApplicationClass.getInstance().getImageLoader().displayImage(movies.get(position).getPosterURL() , holder.flickerImageView , MovieAppApplicationClass.getInstance().getDisplayOptions(),new SimpleImageLoadingListener(){

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                holder.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void openDetailsAtIndex(int index) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("Obj", movies.get(index));
        bundle.putInt("position", index);
        bundle.putBoolean("tablet", mView.isTabMode());
        mView.startDetailsActivity(bundle);
    }

    @Override
    public void OnSuccess(final List<Movie> movies) {

        this.movies = movies;
        MovieAppApplicationClass.getInstance().runOnUI(new Runnable() {
            @Override
            public void run() {

                mView.hideProgress();
                if (movies == null || movies.isEmpty())
                    onFail(new AppException(AppException.NO_DATA_EXCEPTION));
                else {
                    mView.hideErrorMsg();
                    initAnimationsItems();
                    setListAdapter();
                }
            }
        });

        if (mView.isTabMode()) {
            if(FavManager.getInstance().isFav(movies.get(0).getId())){
                movies.get(0).setFav(true);
            }
            openDetailsAtIndex(0);
        }
    }

    private void setListAdapter() {
        adapterRecycle = new ListAdapterRecycle(this);
        mView.getMoviesList().setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollingDown = dy > 0;
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mView.getMoviesList().setLayoutManager(new GridLayoutManager(MovieAppApplicationClass.getInstance().getApplicationContext(), (int) mView.getContext().getResources().getInteger(R.integer.numberOfColumns)));
        mView.getMoviesList().setAdapter(adapterRecycle);
    }

    @Override
    public void onFail(final AppException e) {

        MovieAppApplicationClass.getInstance().runOnUI(new Runnable() {
            @Override
            public void run() {
                mView.hideProgress();
                mView.showErrorMsg(e.getMessage());
            }
        });
    }

    @Override
    public void onSortingChange(int sortOption) {

        if (sortOption != MovieManager.getInstance().getType()) {

            MovieManager.getInstance().changeSort(sortOption);
            getMovies(true);
        }
    }

    @Override
    public void onSaveInstanceCalled(Bundle bundle) {
        bundle.putParcelableArrayList(LIST_KEY, (ArrayList<? extends Parcelable>) movies);
        bundle.putSerializable(SCROLLED_ITEMS, itemsScrolled);
    }

    @Override
    public void onRestoreInstance(Bundle bundle) {
        movies = bundle.getParcelableArrayList(LIST_KEY);
        itemsScrolled = (HashMap<Integer, Boolean>) bundle.getSerializable(SCROLLED_ITEMS);
        if (movies != null && !movies.isEmpty())
            setListAdapter();
        else
            getMovies(false);
    }

    @Override
    public int getHighlightMenuIndex() {
        return MovieManager.getInstance().getType();
    }

    @Override
    public void updateItemAtIndex(int position) {

        adapterRecycle.notifyItemChanged(position);

    }

    @Override
    public void onlyFavIsAvailable() {
        mView.highLightFav();
    }

    public void setView(MVPInterface.ViewInterface view) {
        this.mView = view;
    }

    @Override
    public void scrollingUp() {
        this.scrollingDown = false;
    }
}
