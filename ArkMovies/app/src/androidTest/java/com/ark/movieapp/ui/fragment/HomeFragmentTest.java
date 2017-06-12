package com.ark.movieapp.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.ark.movieapp.R;
import com.ark.movieapp.app.MovieAppApplicationClass;
import com.ark.movieapp.ui.activity.HomeActivity;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.ark.movieapp.utils.EsspressoUtils.childAtIndex;
import static com.ark.movieapp.utils.EsspressoUtils.waitFor;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 *
 * Created by Ark on 6/11/2017.
 */
@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    private IdlingResource mIdlingResource;

    @Rule
    public IntentsTestRule<HomeActivity> mActivityRule = new IntentsTestRule<HomeActivity>(
            HomeActivity.class){
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            mIdlingResource = MovieAppApplicationClass.getInstance().getIdlingResource();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Test
    public void checkApplicationFlow() {
        onView(withId(R.id.movieList))
                .check(new RecyclerViewItemCountAssertion(0));
        onView(withId(R.id.movieList))
                .perform(RecyclerViewActions.scrollToPosition(
                        ((RecyclerView)mActivityRule.getActivity().findViewById(R.id.movieList))
                                .getAdapter().getItemCount() - 1));
        onView(withId(R.id.movieList))
                .check(new RecyclerViewScrollOffsetAssertion(0));

        AppBarLayout.Behavior behavior = getBehavior();

        assertNotNull(behavior);

        assertNotEquals(behavior.getTopAndBottomOffset(), Matchers.comparesEqualTo(0));

        onView(withId(R.id.fab)).perform(click());

        onView(isRoot()).perform(waitFor(500));

        assertThat(((RecyclerView)mActivityRule.getActivity().findViewById(R.id.movieList))
                .computeVerticalScrollOffset(), Matchers.comparesEqualTo(0));

        assertThat(behavior.getTopAndBottomOffset(), Matchers.comparesEqualTo(0));

        Espresso.onView(ViewMatchers.withId(R.id.movieList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(isRoot()).perform(waitFor(500));

        assertNotEquals(mActivityRule.getActivity().getWindow().getStatusBarColor(), R.color.colorPrimaryDark);

        onView(isRoot()).perform(waitFor(5000));

        Espresso.onView(childAtIndex(ViewMatchers.withId(R.id.trailersContainer), 0)).perform(click());
        Intents.intended(hasAction(Intent.ACTION_VIEW));

        onView(isRoot()).perform(waitFor(5000));

        Espresso.pressBack();

//        Espresso.openContextualActionModeOverflowMenu();
//
//        onView(withText(R.string.menu_popular)).check(matches(ViewMatchers.isDisplayed()));
//
//        Espresso.pressBack();
//
//        onView(withId(R.id.movieList)).perform(
//                RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.favIcon)));

//        Espresso.openContextualActionModeOverflowMenu();



//        onView(withText(R.string.favMovies)).check(matches(ViewMatchers.isDisplayed()));
//
//        onView(withText(R.string.favMovies)).perform(ViewActions.click());
//
//        onView(isRoot()).perform(waitFor(500));
//
//        assertThat(((RecyclerView)mActivityRule.getActivity().findViewById(R.id.movieList))
//                .getAdapter().getItemCount(), Matchers.comparesEqualTo(1));
    }

    public AppBarLayout.Behavior getBehavior(){
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                mActivityRule.getActivity()
                .findViewById(R.id.appBar)
                .getLayoutParams();
        return (AppBarLayout.Behavior) params
                .getBehavior();
    }

    @Before
    public void registerIdlingResource() {
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}