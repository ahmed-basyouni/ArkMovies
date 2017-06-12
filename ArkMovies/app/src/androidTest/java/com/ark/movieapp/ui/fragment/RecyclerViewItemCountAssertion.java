package com.ark.movieapp.ui.fragment;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * Created by ahmed-basyouni on 5/19/17.
 */

public class RecyclerViewItemCountAssertion implements ViewAssertion {

    private final int greaterThanCount;

    public RecyclerViewItemCountAssertion(int greaterThanCount) {
        this.greaterThanCount = greaterThanCount;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter.getItemCount(), greaterThan(greaterThanCount));
    }
}
