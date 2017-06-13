package com.ark.movieapp.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ark.movieapp.R;
import com.ark.movieapp.managers.FavManager;
import com.ark.movieapp.managers.MovieManager;
import com.ark.movieapp.presenters.presenterImp.ListFragmentPresenter;
import com.ark.movieapp.presenters.presenterInterfaces.MVPInterface;
import com.ark.movieapp.ui.activity.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements MVPInterface.ViewInterface {

    @BindView(R.id.movieList) RecyclerView moviesList;
    @BindView(R.id.errorMsg) TextView errorMsg;

    private ListFragmentPresenter mPresenter;
    private ProgressDialog loadingDialog;
    private Menu menu;
    private boolean favIsSelected;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this , rootView);

        getmPresenter();

        if(getActivity().findViewById(R.id.fab) != null)
            getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage(getString(R.string.loadingMsg));

        if(savedInstanceState == null) {
            mPresenter.getMovies(false);
        }else
            mPresenter.onRestoreInstance(savedInstanceState);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        loadingDialog = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.sort_menu, menu);
        int index = mPresenter.getHighlightMenuIndex();

        if(index == 0 && !favIsSelected)
            highLightMenuItem(menu.findItem(R.id.popular));
        else if(index == 1 && !favIsSelected)
            highLightMenuItem(menu.findItem(R.id.top_rated));
        else if(index == 2 || favIsSelected)
            highLightMenuItem(menu.findItem(R.id.fav));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        highLightMenuItem(item);

        switch (item.getItemId()) {

            case R.id.popular:
                unHighLightMenuItem(menu.findItem(R.id.top_rated));
                unHighLightMenuItem(menu.findItem(R.id.fav));
                mPresenter.onSortingChange(MVPInterface.POPULAR_SORT);
                break;

            case R.id.top_rated:
                unHighLightMenuItem(menu.findItem(R.id.popular));
                unHighLightMenuItem(menu.findItem(R.id.fav));
                mPresenter.onSortingChange(MVPInterface.TOP_RATED_SORT);
                break;

            case R.id.fav:
                unHighLightMenuItem(menu.findItem(R.id.popular));
                unHighLightMenuItem(menu.findItem(R.id.top_rated));
                mPresenter.onSortingChange(MVPInterface.FAV);
                break;
        }
        return true;

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        if(menu.findItem(R.id.fav) != null)
        if(!FavManager.getInstance().isFavNotEmpty() && MovieManager.getInstance().getType() != MVPInterface.FAV)
            menu.findItem(R.id.fav).setVisible(false);
        else
            menu.findItem(R.id.fav).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    private void highLightMenuItem(MenuItem item){

        if(item != null) {

            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.colorAccent)), 0, s.length(), 0);
            item.setTitle(s);
        }
    }

    private void unHighLightMenuItem(MenuItem item){

        if(item != null) {

            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), android.R.color.black)), 0, s.length(), 0);
            item.setTitle(s);
        }
    }


    @Override
    public void hideProgress() {
        if(loadingDialog!= null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    @Override
    public void showLoading() {
        if(loadingDialog != null)
            loadingDialog.show();
    }

    @Override
    public void showErrorMsg(String msg) {
        if(loadingDialog != null)
            loadingDialog.dismiss();

        errorMsg.setVisibility(View.VISIBLE);
        errorMsg.setText(msg);
        moviesList.setVisibility(View.GONE);
    }

    @Override
    public void hideErrorMsg() {

        if(loadingDialog != null)
            loadingDialog.dismiss();

        errorMsg.setVisibility(View.GONE);
        moviesList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.onSaveInstanceCalled(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public RecyclerView getMoviesList() {
        return moviesList;
    }

    @Override
    public void startDetailsActivity(Bundle bundle) {
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(bundle);
        if(isTabMode())
            getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_details ,detailsFragment).commit();
        else
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_home ,detailsFragment).addToBackStack(null).commit();

        ((HomeActivity)getActivity()).expandToolBar();
    }

    @Override
    public void changeTitle(String title) {

    }

    @Override
    public void updateItemAtIndex(int position) {
        mPresenter.updateItemAtIndex(position);
    }

    @Override
    public void highLightFav() {
        if(menu != null) {

            unHighLightMenuItem(menu.findItem(R.id.popular));
            unHighLightMenuItem(menu.findItem(R.id.top_rated));
            highLightMenuItem(menu.findItem(R.id.fav));

        }else{

            favIsSelected = true;
        }
    }

    @Override
    public boolean isTabMode() {
        return getActivity().findViewById(R.id.activity_details) != null;
    }

    @Override
    public RelativeLayout getContainerLayout() {
        return ((RelativeLayout) getActivity().findViewById(R.id.activity_home));
    }

    public void scrollToTop(){

        mPresenter.scrollingUp();
        moviesList.smoothScrollToPosition(0);
    }

    public void setmPresenter(ListFragmentPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    public ListFragmentPresenter getmPresenter() {
        if(mPresenter == null)
            mPresenter = new ListFragmentPresenter(this);
        return mPresenter;
    }
}
