package com.foodie.foodvisit.fragment;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.foodie.foodvisit.Activity.DetailActivity;
import com.foodie.foodvisit.FoodVisitApp;
import com.foodie.foodvisit.R;
import com.foodie.foodvisit.RestaurantViewModel;
import com.foodie.foodvisit.Utils;
import com.foodie.foodvisit.adapter.RestaurantAdapter;
import com.foodie.foodvisit.database.RestaurantContract;
import com.foodie.foodvisit.model.Location;
import com.foodie.foodvisit.model.Restaurant;
import com.foodie.foodvisit.model.RestaurantInfo;
import com.foodie.foodvisit.model.UserRating;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.foodie.foodvisit.Utils.isOnline;

/**
 * Created by kprabhu on 11/12/17.
 */

public class ToVisitFragment extends LifecycleFragment implements RestaurantAdapter.ClickListener,LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = "ToVistFragment";
    private static final String RESTAURANT = "restaurant" ;
    Unbinder unbinder;
    private static final int LOADER_ID = 12345;
    RestaurantViewModel viewModel;

    @BindView(R.id.no_tovist_list)
    TextView tvNoToVisitList;

    @BindView(R.id.restaurantListRecyclerView)
    RecyclerView recyclerView;
    ProgressBar progressBar;
    private RestaurantAdapter mRestaurantAdapter;
    private List<RestaurantInfo> restaurants = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this,rootView);
        progressBar = rootView.findViewById(R.id.progress_bar);

        setRetainInstance(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(!Utils.hasToVisitList(FoodVisitApp.getAppContext())){
            tvNoToVisitList.setText(getActivity().getResources().getString(R.string.no_tovisit_list));
            progressBar.setVisibility(View.GONE);
            return;
        }
        int gridColumn = getResources().getInteger(R.integer.grid_column_number);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumn));

        viewModel = ViewModelProviders.of(getActivity()).get(RestaurantViewModel.class);
        mRestaurantAdapter = new RestaurantAdapter(viewModel.restaurants, this);
        recyclerView.setAdapter(mRestaurantAdapter);
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

        observe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!Utils.hasToVisitList(FoodVisitApp.getAppContext())){
            tvNoToVisitList.setText(getActivity().getResources().getString(R.string.no_tovisit_list));
            progressBar.setVisibility(View.GONE);

        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                RestaurantContract.RestaurantEntry.CONTENT_URI,
                null,
                null,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<Restaurant> restaurantList = new ArrayList<>();
        restaurants.clear();
        mRestaurantAdapter.updateRestaurants(new ArrayList<Restaurant>(0));
        if (cursor != null && cursor.moveToFirst()) {
            do {
               RestaurantInfo restaurantInfo = new RestaurantInfo(cursor.getString(1),cursor.getString(2),cursor.getString(3),
                        new Location(cursor.getString(4),cursor.getString(6),cursor.getString(7),cursor.getString(5),cursor.getString(8),cursor.getString(9)),
                        new UserRating(cursor.getString(14),cursor.getString(15),cursor.getString(16)),
                        cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13));


                restaurants.add(restaurantInfo);
            } while (cursor.moveToNext());

            for (RestaurantInfo restaurant : restaurants) {
                Restaurant rest = new Restaurant();
                rest.setRestaurant(restaurant);
                restaurantList.add(rest);
            }
            onlineViewChanges();
            mRestaurantAdapter.updateRestaurants(restaurantList);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void accept(int position) {
        Restaurant restaurant = new Restaurant(restaurants.get(position));
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(RESTAURANT, restaurant);
        startActivity(intent);

        Log.d(TAG, "onRestaurantClicked() called with: restaurant = [" + position + "]");

    }

    private void observe() {
        if(Utils.hasToVisitList(FoodVisitApp.getAppContext())){
           return;
        }
        viewModel.getRestaurants().observe(this, new CustomObserver<List<Restaurant>>() {
            @Override
            public void onChanged(@Nullable List<Restaurant> result) {
                if (result == null) {
                    return;
                }
                onlineViewChanges();
                mRestaurantAdapter.updateRestaurants(result);
            }
        });
    }

    private class CustomObserver<T> implements android.arch.lifecycle.Observer<List<Restaurant>> {
        @Override
        public void onChanged(@Nullable List<Restaurant> restaurants) {

        }
    }

    private void onlineViewChanges() {
        if (viewModel != null && viewModel.restaurants != null) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


}
