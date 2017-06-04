package com.ark.movieapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ark.movieapp.R;
import com.ark.movieapp.app.MovieAppApplicationClass;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by ahmedb on 12/12/16.
 */

public class Trailer extends BaseEntity implements Parcelable {

    @Expose
    private String id;
    @Expose
    @SerializedName("name")
    private String title;
    @Expose
    @SerializedName("key")
    private String trailerUrl;

    public Trailer(){

    }

    protected Trailer(Parcel in) {
        id = in.readString();
        title = in.readString();
        trailerUrl = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(trailerUrl);
    }

    public String getTrailerUrl() {
        return MovieAppApplicationClass.getInstance().getString(R.string.youTube_base_url) + trailerUrl;
    }

    public String getTrailerImageUrl() {
        return MovieAppApplicationClass.getInstance().getString(R.string.youTube_image_url)
                + trailerUrl
                + MovieAppApplicationClass.getInstance().getString(R.string.youTube_image_quality);
    }

    public String getTitle() {
        return title;
    }
}
