package com.ark.movieapp.ui.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ark.movieapp.R;
import com.ark.movieapp.data.listeners.UpdateHomeItem;
import com.ark.movieapp.data.model.Movie;
import com.ark.movieapp.presenters.presenterImp.DetailsScreenPresenterImp;
import com.ark.movieapp.presenters.presenterInterfaces.DetailsScreenPresenterInterface;
import com.ark.movieapp.ui.activity.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements DetailsScreenPresenterInterface.DetailsView{


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.movieBanner) ImageView imageView;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.progressBar) ProgressBar progressView;
    @BindView(R.id.moveRating) RatingBar ratingBar;
    @BindView(R.id.poster) ImageView posterImage;
    @BindView(R.id.detailsContainer) LinearLayout detailsContaner;
    @BindView(R.id.voteNumber) TextView voteNumber;
    @BindView(R.id.overView) TextView overView;
    @BindView(R.id.releaseDate) TextView releaseDate;
    @BindView(R.id.fab) FloatingActionButton floatingActionButton;

    private DetailsScreenPresenterImp mPresenter;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(!getArguments().getBoolean("tablet"))
             menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        ButterKnife.bind(this , rootView);

        if(!getArguments().getBoolean("tablet")) {

            getActivity().findViewById(R.id.fab).setVisibility(View.GONE);

            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });

        }else{

            toolbar.setVisibility(View.GONE);
        }

        mPresenter = new DetailsScreenPresenterImp(this);
        mPresenter.getInfoAndSetupUI(getArguments());

        if(savedInstanceState == null){

            Movie movieModel = getArguments().getParcelable("Obj");
            TrailerFragment trailerFragment = new TrailerFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(TrailerFragment.MOVIE_ID , movieModel.getId());
            trailerFragment.setArguments(bundle);
            getChildFragmentManager().beginTransaction().add(R.id.trailersContainer,trailerFragment,"trailerFragment").commit();

            ReviewFragment reviewFragment = new ReviewFragment();
            Bundle reviewBundle = new Bundle();
            reviewBundle.putInt(ReviewFragment.MOVIE_ID , movieModel.getId());
            reviewFragment.setArguments(reviewBundle);
            getChildFragmentManager().beginTransaction().add(R.id.reviewsContainer,reviewFragment,"reviewFragment").commit();

        }

        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.viewWillDetach();

        if(!getArguments().getBoolean("tablet")) {

            ((HomeActivity)getListener()).showToolBar();
            ((HomeActivity)getListener()).showFloatingButton();
        }
    }

    @Override
    public void setContentColor(int color) {
        collapsingToolbarLayout.setContentScrimColor(color);
    }

    @Override
    public void setCollapseTitleColor(int color) {
        collapsingToolbarLayout.setCollapsedTitleTextColor(color);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setStatusBarColor(int color) {
        if(getActivity() != null && !getActivity().isFinishing())
            getActivity().getWindow().setStatusBarColor(color);
    }

    @Override
    public void setTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
    }

    @Override
    public ImageView getBanner() {
        return imageView;
    }

    @Override
    public ImageView getPoster() {
        return posterImage;
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressView;
    }

    @Override
    public LinearLayout getDetailsContainer() {
        return detailsContaner;
    }

    @Override
    public void setRating(float rating) {
        ratingBar.setRating(rating);
    }

    @Override
    public void setReleaseDate(String date) {
        releaseDate.setText(date);
    }

    @Override
    public void setOverView(String overViewString) {
        overView.setText(overViewString);
    }

    @Override
    public void setRatingNumber(String vote) {
        voteNumber.setText(vote);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public FloatingActionButton getFloatngButon() {
        return floatingActionButton;
    }

    public UpdateHomeItem getListener(){
        if(getActivity() instanceof UpdateHomeItem)
            return ((UpdateHomeItem)getActivity());
        return null;
    }

    @Override
    public void hideToolBar() {

    }
}
