package com.ark.movieapp.ui.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ark.movieapp.R;
import com.ark.movieapp.data.listeners.UpdateHomeItem;
import com.ark.movieapp.ui.fragment.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements UpdateHomeItem{

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.appBar) AppBarLayout appBar;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        showToolBar();

        if(getSupportFragmentManager().findFragmentByTag("HomeFragment") == null)
            getSupportFragmentManager().beginTransaction().add(R.id.activity_home , new HomeFragment() , "HomeFragment").commit();
    }

    @Override
    public void updateItemAtIndex(int index) {

        if(getSupportFragmentManager().findFragmentByTag("HomeFragment") != null){
            ((HomeFragment)getSupportFragmentManager().findFragmentByTag("HomeFragment")).updateItemAtIndex(index);
        }
    }

    @OnClick(R.id.fab) void scrollToTop() {

        if(getSupportFragmentManager().findFragmentByTag("HomeFragment") != null){
            ((HomeFragment)getSupportFragmentManager().findFragmentByTag("HomeFragment")).scrollToTop();
            expandToolBar();
        }

    }

    public void expandToolBar() {

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBar
                .getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params
                .getBehavior();
        if (behavior != null)
            behavior.setTopAndBottomOffset(0);
    }

    public void showToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().show();
    }

    public void showFloatingButton(){
        floatingActionButton.setVisibility(View.VISIBLE);
    }
}
