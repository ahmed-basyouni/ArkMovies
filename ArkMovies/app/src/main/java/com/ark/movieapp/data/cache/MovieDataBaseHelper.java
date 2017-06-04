package com.ark.movieapp.data.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * Created by ahmedb on 11/4/16.
 */

public class MovieDataBaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    public static final String MOVIES_TABLE = "MoviesImaggeTable";

    public static final String MOVIE_ID = "_id";
    public static final String MOVIE_NAME = "movieName";
    public static final String MOVIE_POSTER_URL = "posterURL";
    public static final String OVERVIEW = "movieOverView";
    public static final String RATE = "rate";
    public static final String RELEASE_DATE = "releaseDate";
    public static final String MOVIE_BANNER_URL = "movieBanner";
    public static final String MOVIE_VOTE_NUMBER = "voteNumber";
    public static final String IS_FAV = "isFav";

    private static final String DATABASE_CREATE = "create table "
            + MOVIES_TABLE + "( " + MOVIE_ID
            + " integer primary key , " + MOVIE_NAME
            + " text not null, " + MOVIE_POSTER_URL + " text not null, "
            + OVERVIEW + " text not null, "
            + RATE + " integer , "
            + RELEASE_DATE + " text not null, "
            + MOVIE_BANNER_URL + " text not null, "
            + MOVIE_VOTE_NUMBER + " text not null, "
            + IS_FAV + " integer"
            + ");";

    public MovieDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MOVIES_TABLE);
        onCreate(sqLiteDatabase);
    }
}
