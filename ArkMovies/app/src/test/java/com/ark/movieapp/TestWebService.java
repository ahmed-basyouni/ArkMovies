package com.ark.movieapp;

import com.ark.movieapp.data.exception.AppException;
import com.ark.movieapp.data.model.Movie;
import com.ark.movieapp.data.model.ResultModel;
import com.ark.movieapp.managers.MovieManager;
import com.ark.movieapp.presenters.presenterImp.ListFragmentPresenter;
import com.ark.movieapp.ui.activity.HomeActivity;
import com.ark.movieapp.ui.fragment.HomeFragment;
import com.nostra13.universalimageloader.utils.L;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * Created by Ark on 6/13/2017.
 */
@RunWith(RobolectricTestRunnerWithResources.class)
public class TestWebService {
    private final CountDownLatch latch = new CountDownLatch(1);
    private ResultModel resultModel;
    private HomeActivity mainActivity;

    @Test
    public void testRecycleViewAdapter() throws InterruptedException {
        HomeFragment homeFragment = spy(HomeFragment.class);
        ListFragmentPresenter listFragmentPresenter = spy(ListFragmentPresenter.class);
        listFragmentPresenter.setView(homeFragment);
        homeFragment.setmPresenter(listFragmentPresenter);
        SupportFragmentTestUtil.startVisibleFragment(homeFragment);
        Thread.sleep(10000);
        ShadowLooper.runUiThreadTasks();
        ArgumentCaptor<List<Movie>> moviesCaptor = ArgumentCaptor.forClass(List.class);
        verify(homeFragment.getmPresenter(),  times(1)).OnSuccess(moviesCaptor.capture());
        assertTrue(homeFragment.getMoviesList().getAdapter().getItemCount() == moviesCaptor.getValue().size());
    }

    @Test
    public void testOnFailCall(){
        ListFragmentPresenter listFragmentPresenter = Mockito.spy(ListFragmentPresenter.class);
        MovieManager movieManager = spy(MovieManager.class);
        when(movieManager.isNetworkConnected()).thenReturn(false);
        movieManager.getMovies(listFragmentPresenter, false);
        ShadowLooper.runUiThreadTasks();
        verify(listFragmentPresenter,  times(1)).onFail(isA(AppException.class));
        ArgumentCaptor<AppException> argument = ArgumentCaptor.forClass(AppException.class);
        verify(listFragmentPresenter,  times(1)).onFail(argument.capture());
        assertNotNull(argument);
        assertEquals(AppException.NETWORK_EXCEPTION, argument.getValue().getErrorCode());
    }

    @Test
    public void testOnSuccessCall() throws InterruptedException {
        ListFragmentPresenter listFragmentPresenter = Mockito.spy(ListFragmentPresenter.class);
        MovieManager movieManager = spy(MovieManager.class);
        movieManager.getMovies(listFragmentPresenter, false);
        Thread.sleep(10000);
        ShadowLooper.runUiThreadTasks();
        ArgumentCaptor<List<Movie>> moviesCaptor = ArgumentCaptor.forClass(List.class);
        verify(listFragmentPresenter,  times(1)).OnSuccess(moviesCaptor.capture());
        assertTrue(moviesCaptor.getValue().size() > 0);
    }
}
