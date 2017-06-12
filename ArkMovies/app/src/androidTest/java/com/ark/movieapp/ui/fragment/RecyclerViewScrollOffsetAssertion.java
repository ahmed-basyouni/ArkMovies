package com.ark.movieapp.ui.fragment;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Created by Ark on 6/11/2017.
 */

public class RecyclerViewScrollOffsetAssertion implements ViewAssertion {

    private final int scrollOffset;

    public RecyclerViewScrollOffsetAssertion(int scrollOffset) {
        this.scrollOffset = scrollOffset;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }
        RecyclerView recyclerView = (RecyclerView) view;
        assertThat(recyclerView.computeVerticalScrollOffset(), greaterThan(scrollOffset));
    }
}
