package com.ark.movieapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmedb on 12/11/16.
 */

public class ResultModel extends BaseEntity {

    @Expose
    private int page;

    @Expose
    @SerializedName("results")
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }
}
