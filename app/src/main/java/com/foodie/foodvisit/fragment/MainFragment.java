package com.foodie.foodvisit.fragment;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.foodie.foodvisit.Activity.DetailActivity;
import com.foodie.foodvisit.R;
import com.foodie.foodvisit.RestaurantViewModel;
import com.foodie.foodvisit.adapter.RestaurantAdapter;
import com.foodie.foodvisit.model.Restaurant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.foodie.foodvisit.Utils.isOnline;

/**
 * Created by kprabhu on 11/11/17.
 */

public class MainFragment extends LifecycleFragment implements RestaurantAdapter.ClickListener {

    public static final String TAG = "MainFragment";
    private static final String RESTAURANT = "restaurant" ;
    Unbinder unbinder;
    @BindView(R.id.restaurantListRecyclerView)
    RecyclerView recyclerView;
    ProgressBar progressBar;

    RestaurantViewModel viewModel;
    private RestaurantAdapter mRestaurantAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        progressBar = rootView.findViewById(R.id.progress_bar);
        unbinder = ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getContext().registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        int gridColumn = getResources().getInteger(R.integer.grid_column_number);
        viewModel = ViewModelProviders.of(getActivity()).get(RestaurantViewModel.class);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumn));

        mRestaurantAdapter = new RestaurantAdapter(viewModel.restaurants, this);
        recyclerView.setAdapter(mRestaurantAdapter);
        if (!isOnline()) {
            Snackbar.make(getView(), R.string.no_connection, Snackbar.LENGTH_LONG).show();
            return;
        }
        observe();
    }

    @Override
    public void accept(int position) {
        List<Restaurant> value = viewModel.getRestaurants().getValue();
        if (value == null) {
            Log.i(TAG, "onRestaurantClicked: No restaurant");
            return;
        }
        Restaurant restaurant = value.get(position);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(RESTAURANT, restaurant);
        startActivity(intent);

        Log.d(TAG, "onRestaurantClicked() called with: restaurant = [" + position + "]");
    }

    private BroadcastReceiver connectivityChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isOnline()) {
                observe();
            } else if (viewModel == null || viewModel.restaurants == null) {
                offlineViewChanges();
            }
        }
    };

    private void offlineViewChanges() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
    private void onlineViewChanges() {
        if (viewModel != null && viewModel.restaurants != null) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    private void observe() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}


