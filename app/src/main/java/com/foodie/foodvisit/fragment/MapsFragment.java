package com.foodie.foodvisit.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodie.foodvisit.FoodVisitApp;
import com.foodie.foodvisit.R;
import com.foodie.foodvisit.Utils;
import com.foodie.foodvisit.model.Location;
import com.foodie.foodvisit.model.RestaurantInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


/**
 * Created by kprabhu on 11/11/17.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final String TAG = "MapsFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<RestaurantInfo> restaurantInfoList = Utils.getRestrauntInfoListFromDB(FoodVisitApp.getAppContext());



        final LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if(restaurantInfoList.size() > 0) {

            for (RestaurantInfo info : restaurantInfoList) {
                Location location = info.getLocation();
                LatLng place = new LatLng(Double.parseDouble(location.getLongitude()), Double.parseDouble(location.getLatitude()));
                MarkerOptions options = new MarkerOptions().position(place);
                mMap.addMarker(options).setTitle(info.getName());
                builder.include(place);
            }

        }else {
            LatLng bangalore = new LatLng(12.971606, 77.594376);
            MarkerOptions options = new MarkerOptions().position(bangalore);
            mMap.addMarker(new MarkerOptions().position(bangalore).title("Marker in Bangalore"));
            builder.include(bangalore);
        }

        LatLngBounds bounds = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
    }

}
