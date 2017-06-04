package com.ark.movieapp.managers;

import com.ark.movieapp.data.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ahmedb on 12/1/16.
 */

public class MovieParsingManager {

    public static List<Movie> parseMovies(InputStream inputStream) throws JSONException, IOException {

        StringBuilder result = new StringBuilder();
        if (inputStream != null) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }

        String json = result.toString();

        List<Movie> movies = new ArrayList<>();

        String RESULT_ARRAY = "results";
        String TITLE = "original_title";
        String POSTER_PATH = "poster_path";
        String OVERVIEW = "overview";
        String RELEASE_DATE = "release_date";
        String RATE = "vote_average";
        String BANNER = "backdrop_path";
        String VOTE_NUM = "vote_count";

        JSONArray jsonArray = new JSONObject(json).getJSONArray(RESULT_ARRAY);

        for(int movieIndex = 0 ; movieIndex < jsonArray.length() ; movieIndex++){

            JSONObject movieJson = jsonArray.getJSONObject(movieIndex);

            Movie movie = new Movie();

            if(movieJson.has(TITLE))
                movie.setTitle(movieJson.getString(TITLE));

            if(movieJson.has(POSTER_PATH))
                movie.setPosterURL(movieJson.getString(POSTER_PATH));

            if(movieJson.has(OVERVIEW))
                movie.setOverView(movieJson.getString(OVERVIEW));

            if(movieJson.has(RELEASE_DATE))
                movie.setReleaseDate(movieJson.getString(RELEASE_DATE));

            if(movieJson.has(RATE))
                movie.setRate((float) movieJson.getDouble(RATE));

            if(movieJson.has(BANNER))
                movie.setBannerURL(movieJson.getString(BANNER));

            if(movieJson.has(VOTE_NUM))
                movie.setVoteNumber(movieJson.getString(VOTE_NUM));

            movies.add(movie);
        }

        return movies;
    }

}
