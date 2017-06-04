package com.ark.movieapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ark.movieapp.R;
import com.ark.movieapp.presenters.presenterImp.ReviewFragmentPresenter;
import com.ark.movieapp.presenters.presenterImp.TrailerFragmentPresenter;
import com.ark.movieapp.presenters.presenterInterfaces.ReviewPresenterInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by ahmedb on 12/12/16.
 */

public class ReviewFragment extends Fragment implements ReviewPresenterInterface.ViewInterface{

    public static final java.lang.String MOVIE_ID = "reviewMovieID";
    @BindView(R.id.reviewContainer) LinearLayout reviewContainer;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.errorMsg)
    TextView errorMsg;
    private ReviewFragmentPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.review_fragment,container,false);

        ButterKnife.bind(this,rootView);

        int movieId = getArguments().getInt(MOVIE_ID , -1);

        StateMaintainer mStateMaintainer =
                new StateMaintainer( getFragmentManager() , ReviewFragment.class.getName()+movieId);

        if (mStateMaintainer.firstTimeIn()) {

            mPresenter = new ReviewFragmentPresenter(this);
            mStateMaintainer.put(mPresenter);

        }else{

            mPresenter = mStateMaintainer.get(ReviewFragmentPresenter.class.getName());
            mPresenter.setView(this);
        }

        if(movieId != -1 && savedInstanceState == null)
            mPresenter.getMovieReview(movieId);
        else if(savedInstanceState != null)
            mPresenter.onRestoreInstance(savedInstanceState);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.onSaveInstanceCalled(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public LinearLayout getReviewContainer() {
        return reviewContainer;
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
}
