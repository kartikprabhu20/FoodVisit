package com.foodie.foodvisit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.foodie.foodvisit.R
import com.foodie.foodvisit.Utils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    companion object {
        const val TAG = "MapsFragment"
    }

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_maps, container, false)
        setHasOptionsMenu(true)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val restaurantInfoList = Utils.getRestaurantInfoListFromDB(requireContext())
        val builder = LatLngBounds.Builder()

        if (restaurantInfoList.isNotEmpty()) {
            for (info in restaurantInfoList) {
                val location = info.location ?: continue
                val lat = location.longitude?.toDoubleOrNull() ?: continue
                val lng = location.latitude?.toDoubleOrNull() ?: continue
                val place = LatLng(lat, lng)
                mMap.addMarker(MarkerOptions().position(place))?.title = info.name
                builder.include(place)
            }
        } else {
            val bangalore = LatLng(12.971606, 77.594376)
            mMap.addMarker(MarkerOptions().position(bangalore).title("Marker in Bangalore"))
            builder.include(bangalore)
        }

        val bounds = builder.build()
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200))
    }
}
