package com.ark.movieapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by ahmedb on 12/12/16.
 */

public class ReviewResultModel extends BaseEntity {

    @Expose
    private int id;
    @Expose
    @SerializedName("results")
    private List<Review> reviews;

    public int getId() {
        return id;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
