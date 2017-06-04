package com.ark.movieapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ark.movieapp.R;
import com.ark.movieapp.app.MovieAppApplicationClass;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmedb on 12/12/16.
 */

public class Review extends BaseEntity implements Parcelable {

    @Expose
    private String id;
    @Expose
    @SerializedName("author")
    private String author;
    @Expose
    @SerializedName("content")
    private String review;

    public Review(){

    }

    protected Review(Parcel in) {
        id = in.readString();
        author = in.readString();
        review = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(review);
    }

    public String getAuthor() {
        return author;
    }

    public String getReview() {
        return review;
    }
}
