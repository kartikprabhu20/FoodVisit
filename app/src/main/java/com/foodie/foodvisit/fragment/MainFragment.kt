package com.foodie.foodvisit.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foodie.foodvisit.Activity.DetailActivity
import com.foodie.foodvisit.R
import com.foodie.foodvisit.RestaurantViewModel
import com.foodie.foodvisit.Utils
import com.foodie.foodvisit.adapter.RestaurantAdapter
import com.foodie.foodvisit.model.Restaurant
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), RestaurantAdapter.ClickListener {

    companion object {
        const val TAG = "MainFragment"
        private const val RESTAURANT = "restaurant"
    }

    private val viewModel: RestaurantViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var mRestaurantAdapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true)
        progressBar = rootView.findViewById(R.id.progress_bar)
        recyclerView = rootView.findViewById(R.id.restaurantListRecyclerView)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireContext().registerReceiver(
            connectivityChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        val gridColumn = resources.getInteger(R.integer.grid_column_number)
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), gridColumn)

        mRestaurantAdapter = RestaurantAdapter(
            viewModel.restaurants.value ?: emptyList(),
            this
        )
        recyclerView.adapter = mRestaurantAdapter

        if (!Utils.isOnline(requireContext())) {
            Snackbar.make(requireView(), R.string.no_connection, Snackbar.LENGTH_LONG).show()
            return
        }
        observe()
    }

    override fun accept(position: Int) {
        val value: List<Restaurant> = viewModel.getRestaurants().value ?: run {
            Log.i(TAG, "onRestaurantClicked: No restaurants")
            return
        }
        val restaurant = value[position]
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(RESTAURANT, restaurant)
        startActivity(intent)
    }

    private val connectivityChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Utils.isOnline(context)) {
                observe()
            } else if (viewModel.restaurants.value == null) {
                offlineViewChanges()
            }
        }
    }

    private fun offlineViewChanges() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun onlineViewChanges() {
        if (viewModel.restaurants.value != null) {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun observe() {
        viewModel.getRestaurants().observe(viewLifecycleOwner) { result ->
            if (result == null) return@observe
            onlineViewChanges()
            mRestaurantAdapter.updateRestaurants(result)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            requireContext().unregisterReceiver(connectivityChangeReceiver)
        } catch (_: IllegalArgumentException) {
            // Receiver not registered
        }
    }
}
