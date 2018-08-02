package com.practice.project.androidbootcamp.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.practice.project.androidbootcamp.MainActivity
import com.practice.project.androidbootcamp.adapter.VenuesAdapter
import com.practice.project.androidbootcamp.model.Venue
import com.practice.project.androidbootcamp.utilities.FourSquareAPIController
import com.practice.project.androidbootcamp.utilities.NetworkUtilities
import java.util.ArrayList

class RecyclerViewViewModel(@SuppressLint("StaticFieldLeak") private val mActivity: Activity, @SuppressLint("StaticFieldLeak") private val mContext: Context) : ViewModel() {

    private val mVenuesAdapter = VenuesAdapter()
    private val mVenues = MutableLiveData<List<Venue>>()
    private val mNetworkUtilities = NetworkUtilities()

    init {
        mVenues.observeForever { venues -> mVenuesAdapter.setVenueData(venues!!) }
    }

    fun setupRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = mVenuesAdapter
        if (mNetworkUtilities.isConnectedToNetwork(mContext)) {
            MainActivity.sGeoLocation.observeForever({ geoLocation -> loadVenues(geoLocation!![0].toString() + "," + geoLocation[1]) }
            )
        } else {
            LoadVenuesFromDatabaseTask().execute()
        }
    }

    private fun loadVenues(geoLocation: String) {
        val fourSquareAPIController = FourSquareAPIController(geoLocation, mVenues, mContext)
        fourSquareAPIController.start()
    }

    @SuppressLint("StaticFieldLeak")
    inner class LoadVenuesFromDatabaseTask : AsyncTask<Void, Void, List<Venue>>() {
        override fun doInBackground(vararg voids: Void): List<Venue> {
            var venues: List<Venue> = ArrayList()
            try {
                venues = MainActivity.sVenuesAppDatabase.venueDao().all
                val categoriesList = MainActivity.sVenuesAppDatabase.categoryDao().all
                val locationList = MainActivity.sVenuesAppDatabase.locationDao().all
                if (venues.isNotEmpty()) {
                    for (venue in venues) {
                        for (category in categoriesList) {
                            if (venue.categoryId == category.categoryId) {
                                venue.categories.add(category)
                                break
                            }
                        }
                        for (location in locationList) {
                            if (venue.locationId == location.locationId) {
                                venue.location=location
                                break
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return venues
        }

        override fun onPostExecute(venues: List<Venue>) {
            super.onPostExecute(venues)
            mVenues.value = venues
        }
    }
}