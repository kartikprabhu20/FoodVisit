package com.foodie.foodvisit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.foodie.foodvisit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutusFragment : Fragment() {

    companion object {
        const val TAG = "AboutusFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_aboutus, container, false)
}
