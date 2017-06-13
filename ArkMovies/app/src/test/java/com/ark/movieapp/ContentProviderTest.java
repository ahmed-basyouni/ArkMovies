package com.ark.movieapp;

import android.content.ContentResolver;
import android.content.pm.ProviderInfo;
import android.database.Cursor;

import com.ark.movieapp.data.cache.MovieContentProvider;
import com.ark.movieapp.data.cache.MovieDataBaseHelper;
import com.ark.movieapp.data.model.Movie;
import com.ark.movieapp.managers.FavManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentResolver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 *
 * Created by Ark on 6/12/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)
public class ContentProviderTest {
    private ContentResolver contentResolver;
    private FavManager favManager;

    @Before
    public void setup() {
        MovieContentProvider provider = new MovieContentProvider();
        provider.onCreate();
        favManager = new FavManager();
        favManager.context = RuntimeEnvironment.application;
        ProviderInfo info = new ProviderInfo();
        info.authority = MovieContentProvider.AUTHORITY;
        Robolectric.buildContentProvider(MovieContentProvider.class).create(info);
        contentResolver = RuntimeEnvironment.application.getContentResolver();
    }

    @Test
    public void getSomeData() {
        Movie movie = new Movie();
        movie.setId(10);
        movie.setFav(true);
        movie.setTitle("pirates");
        movie.setReleaseDate("12-10-2107");
        movie.setOverView("pirates movie");
        movie.setRate(5);
        movie.setVoteNumber("24535");
        movie.setPosterURL("https://blah.com");
        movie.setBannerURL("https://blah.com");

        favManager.changeFav(movie);

        Cursor cursorCheck = contentResolver.query(MovieContentProvider.CONTENT_URI,
                null, null, null, null);

        assertNotNull(cursorCheck);

        cursorCheck.moveToFirst();

        assertEquals(cursorCheck.getString(cursorCheck.getColumnIndex(MovieDataBaseHelper.MOVIE_NAME)), movie.getTitle());
        assertTrue(cursorCheck.getCount() > 0);


        assertTrue(favManager.isFav(movie.getId()));

        movie.setFav(false);

        favManager.changeFav(movie);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cursorCheck = contentResolver.query(MovieContentProvider.CONTENT_URI,
                null, null, null, null);

        assertNotNull(cursorCheck);

        assertTrue(cursorCheck.getCount() == 0);
    }
}
