package com.foodie.foodvisit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodie.foodvisit.R;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by kprabhu on 11/14/17.
 */

public class AboutusFragment extends Fragment {

    public static final String TAG = "AboutusFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aboutus, container, false);
        return rootView;
    }

}
