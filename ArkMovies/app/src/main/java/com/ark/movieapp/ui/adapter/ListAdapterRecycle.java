package com.ark.movieapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ark.movieapp.ui.views.MoviesViewHolder;
import com.ark.movieapp.presenters.presenterInterfaces.MVPInterface;


/**
 * Created by ahmedb on 10/18/16.
 */

public class ListAdapterRecycle extends RecyclerView.Adapter<MoviesViewHolder>{

    private final MVPInterface.PresenterInterface mPresenter;

    public ListAdapterRecycle(MVPInterface.PresenterInterface presenterInterface){

        this.mPresenter = presenterInterface;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return mPresenter.createViewHolder(parent , viewType);
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder holder, final int position) {

        mPresenter.bindViewHolder(holder, position);

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getItemCount();
    }

}