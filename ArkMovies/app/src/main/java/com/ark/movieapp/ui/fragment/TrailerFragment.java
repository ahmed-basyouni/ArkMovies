package com.ark.movieapp.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ark.movieapp.R;
import com.ark.movieapp.presenters.presenterImp.TrailerFragmentPresenter;
import com.ark.movieapp.presenters.presenterInterfaces.TrailerPresenterInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by ahmedb on 12/12/16.
 */

public class TrailerFragment extends Fragment implements TrailerPresenterInterface.ViewInterface{

    public static final String MOVIE_ID = "movie_id";
    @BindView(R.id.trailersContainer) LinearLayout trailerContainer;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.errorMsg) TextView errorMsg;

    private TrailerFragmentPresenter mPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.trailer_fragment,container,false);

        ButterKnife.bind(this,rootView);

        int movieId = getArguments().getInt(MOVIE_ID , -1);

        StateMaintainer mStateMaintainer =
                new StateMaintainer( getFragmentManager() , TrailerFragment.class.getName()+movieId);

        if (mStateMaintainer.firstTimeIn()) {

            mPresenter = new TrailerFragmentPresenter(this);
            mStateMaintainer.put(mPresenter);

        }else{

            mPresenter = mStateMaintainer.get(TrailerFragmentPresenter.class.getName());
            mPresenter.setView(this);
        }

        if(movieId != -1 && savedInstanceState == null) {

            mPresenter.getMovieTrailer(movieId);

        }else if(savedInstanceState != null) {

            mPresenter.onRestoreInstance(savedInstanceState);

        }
        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.onSaveInstanceCalled(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public LinearLayout getTrailersContainer() {
        return trailerContainer;
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMsg(String msg) {
        errorMsg.setVisibility(View.VISIBLE);
        errorMsg.setText(msg);
    }

    @Override
    public void hideErrorMsg() {
        errorMsg.setVisibility(View.GONE);
    }

    @Override
    public void openYouTube(String url) {

        getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

    }

    @Override
    public void shareTrailer(Bundle bundle) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtras(bundle);
        startActivity(Intent.createChooser(i, getString(R.string.shareTrailer)));
    }


}
