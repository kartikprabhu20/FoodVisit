package com.foodie.foodvisit.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foodie.foodvisit.Activity.DetailActivity
import com.foodie.foodvisit.R
import com.foodie.foodvisit.RestaurantViewModel
import com.foodie.foodvisit.Utils
import com.foodie.foodvisit.adapter.RestaurantAdapter
import com.foodie.foodvisit.model.Restaurant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ToVisitFragment : Fragment(), RestaurantAdapter.ClickListener {

    companion object {
        const val TAG = "ToVisitFragment"
        private const val RESTAURANT = "restaurant"
    }

    private val viewModel: RestaurantViewModel by activityViewModels()
    private lateinit var tvNoToVisitList: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var mRestaurantAdapter: RestaurantAdapter
    private var restaurants: List<Restaurant> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        tvNoToVisitList = rootView.findViewById(R.id.no_tovist_list)
        progressBar = rootView.findViewById(R.id.progress_bar)
        recyclerView = rootView.findViewById(R.id.restaurantListRecyclerView)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!Utils.hasToVisitList(requireContext())) {
            tvNoToVisitList.text = requireActivity().resources.getString(R.string.no_tovisit_list)
            progressBar.visibility = View.GONE
            return
        }

        val gridColumn = resources.getInteger(R.integer.grid_column_number)
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), gridColumn)

        mRestaurantAdapter = RestaurantAdapter(emptyList(), this)
        recyclerView.adapter = mRestaurantAdapter

        loadFromDatabase()
    }

    override fun onResume() {
        super.onResume()
        if (!Utils.hasToVisitList(requireContext())) {
            tvNoToVisitList.text = requireActivity().resources.getString(R.string.no_tovisit_list)
            progressBar.visibility = View.GONE
        }
    }

    private fun loadFromDatabase() {
        lifecycleScope.launch {
            val list = withContext(Dispatchers.IO) {
                Utils.getRestaurantsFromDB(requireContext())
            }
            restaurants = list
            if (list.isNotEmpty()) {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                mRestaurantAdapter.updateRestaurants(list)
            }
        }
    }

    override fun accept(position: Int) {
        val restaurant = restaurants.getOrNull(position) ?: return
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(RESTAURANT, restaurant)
        startActivity(intent)
        Log.d(TAG, "onRestaurantClicked: position=$position")
    }
}
