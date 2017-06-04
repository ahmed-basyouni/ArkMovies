package com.ark.movieapp.ui.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ark.movieapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ahmedb on 11/26/16.
 */

public class MoviesViewHolder extends RecyclerView.ViewHolder{

    public @BindView(R.id.flickerImageView) ImageView flickerImageView;
    public @BindView(R.id.ImageContainer) RelativeLayout flickerImageContainer;
    public @BindView(R.id.progressBar) ProgressBar progressBar;
    public @BindView(R.id.favIcon) ImageButton favBtn;

    public MoviesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
