package com.practice.project.androidbootcamp.viewmodel

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.practice.project.androidbootcamp.adapter.VenuesAdapter
import com.practice.project.androidbootcamp.model.Venue
import java.util.ArrayList

class RecyclerViewViewModel(private val mActivity: Activity) : ViewModel() {

    private val mVenuesAdapter: VenuesAdapter
    private var mVenues: List<Venue>? = null

    init {
        mVenues = ArrayList<Venue>()
        mVenuesAdapter = VenuesAdapter()
    }

    fun setupRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = mVenuesAdapter
        recyclerView.layoutManager = layoutManager
        mVenuesAdapter.setVenueData(mVenues!!)
        loadVenues()
    }

    /**
     * Cargar Venues
     */
    private fun loadVenues() {}

    fun setVenues(venues: List<Venue>) {
        this.mVenues = venues
        mVenuesAdapter.setVenueData(mVenues!!)
    }
}