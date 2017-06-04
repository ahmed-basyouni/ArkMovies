package com.ark.movieapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ahmedb on 12/1/16.
 */

public class Movie extends BaseEntity implements Parcelable{

    @Expose
    private int id;
    @Expose
    @SerializedName("original_title")
    private String title;
    @Expose
    @SerializedName("poster_path")
    public String posterURL;
    @Expose
    @SerializedName("overview")
    private String overView;
    @Expose
    @SerializedName("vote_average")
    private float rate;
    @Expose
    @SerializedName("release_date")
    private String releaseDate;
    private boolean fav;
    @Expose
    @SerializedName("backdrop_path")
    public String bannerURL;
    @Expose
    @SerializedName("vote_count")
    private String voteNumber;


    public Movie(){

    }

    public Movie(Parcel in) {
        title = in.readString();
        posterURL = in.readString();
        overView = in.readString();
        releaseDate = in.readString();
        bannerURL = in.readString();
        rate = in.readFloat();
        fav  = (Boolean) in.readValue( null );
        id = in.readInt();
        voteNumber = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(posterURL);
        parcel.writeString(overView);
        parcel.writeString(releaseDate);
        parcel.writeString(bannerURL);
        parcel.writeFloat(rate);
        parcel.writeValue(fav);
        parcel.writeInt(id);
        parcel.writeString(voteNumber);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getVoteNumber() {
        return voteNumber;
    }

    public void setVoteNumber(String voteNumber) {
        this.voteNumber = voteNumber;
    }

    public String getBannerURL() {
        return MovieAppApplicationClass.getInstance().getString(R.string.banner_url) + bannerURL;
    }

    public void setBannerURL(String bannerURL) {
        this.bannerURL = bannerURL;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterURL() {
        return MovieAppApplicationClass.getInstance().getString(R.string.banner_url) + posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
