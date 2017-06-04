package com.ark.movieapp.presenters.presenterImp;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ark.movieapp.R;
import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.data.exception.AppException;
import com.ark.movieapp.data.model.Review;
import com.ark.movieapp.managers.ReviewManager;
import com.ark.movieapp.presenters.presenterInterfaces.ReviewPresenterInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmedb on 12/13/16.
 */

public class ReviewFragmentPresenter implements ReviewPresenterInterface.PresenterInterface {

    private ReviewPresenterInterface.ViewInterface mView;
    private final String LIST_KEY = "reviewList";
    private List<Review> reviews;
    private int movieId;

    public ReviewFragmentPresenter(ReviewPresenterInterface.ViewInterface viewInterface){
        this.mView = viewInterface;
    }

    public void setView(ReviewPresenterInterface.ViewInterface mView) {
        this.mView = mView;
    }

    @Override
    public void getMovieReview(int id) {
        this.movieId = id;
        mView.showLoading();
        ReviewManager.getInstance().getMovieReview(id, this);
    }

    @Override
    public void onSaveInstanceCalled(Bundle bundle) {
        bundle.putParcelableArrayList(LIST_KEY , (ArrayList<? extends Parcelable>) reviews);
    }

    @Override
    public void onRestoreInstance(Bundle bundle) {
        reviews = bundle.getParcelableArrayList(LIST_KEY);
        if(reviews != null && !reviews.isEmpty())
            if(mView != null && mView.getContext() != null)
                setAdapter();
        else
            getMovieReview(movieId);
    }

    private void setAdapter() {

        for(int x = 0 ; x < reviews.size() ; x++){

            View rootView = LayoutInflater.from(mView.getContext()).inflate(R.layout.review_item,mView.getReviewContainer(),false);

            final ReviewItem reviewItem = new ReviewItem(rootView);

            final Review review = reviews.get(x);

            reviewItem.author.setText(review.getAuthor());

            reviewItem.showMoreButton.setTag(false);


            if(review.getReview().length() > 200){

                reviewItem.reviewContent.setText(review.getReview().substring(0 , 200));

                reviewItem.showMoreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        changeTextContent(reviewItem, review);
                    }
                });

            }else{
                reviewItem.reviewContent.setText(review.getReview());
                reviewItem.showMoreButton.setVisibility(View.GONE);
            }

            mView.getReviewContainer().addView(rootView);

            if(x != reviews.size() - 1){
                View view = new View(mView.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , 1);
                view.setBackgroundColor(ContextCompat.getColor(mView.getContext() , R.color.black));
                view.setLayoutParams(params);
                mView.getReviewContainer().addView(view);
            }

        }
    }

    private void changeTextContent(ReviewItem reviewItem, Review review) {

        if((boolean)reviewItem.showMoreButton.getTag()){

            reviewItem.reviewContent.setText(review.getReview().substring(0 , 200));
            reviewItem.showMoreButton.setText(MovieAppApplicationClass.getInstance().getString(R.string.show_more));
            reviewItem.showMoreButton.setTag(false);

        }else{

            reviewItem.reviewContent.setText(review.getReview());
            reviewItem.showMoreButton.setText(MovieAppApplicationClass.getInstance().getString(R.string.show_less));
            reviewItem.showMoreButton.setTag(true);
        }
    }

    @Override
    public void onListDownloaded(final List<Review> reviews) {
        this.reviews = reviews;
        MovieAppApplicationClass.getInstance().runOnUI(new Runnable() {
            @Override
            public void run() {

                mView.hideLoading();
                if (reviews == null || reviews.isEmpty())
                    onFail(new AppException(AppException.NO_DATA_EXCEPTION));
                else{
                    mView.hideErrorMsg();
                    if(mView != null && mView.getContext() != null)
                        setAdapter();
                }
            }
        });
    }

    @Override
    public void onFail(final AppException e) {

        MovieAppApplicationClass.getInstance().runOnUI(new Runnable() {
            @Override
            public void run() {
                mView.hideLoading();
                mView.showErrorMsg(e.getMessage());
            }
        });
    }

    class ReviewItem{

        @BindView(R.id.author) public TextView author;
        @BindView(R.id.reviewContent) public TextView reviewContent;
        @BindView(R.id.show_more_button) public TextView showMoreButton;

        public ReviewItem(View rootView){

            ButterKnife.bind(this,rootView);
        }
    }
}
