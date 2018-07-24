package com.practice.project.androidbootcamp.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.AsyncTask
import com.practice.project.androidbootcamp.MainActivity
import com.practice.project.androidbootcamp.model.Venue
import com.practice.project.androidbootcamp.utilities.FourSquareAPIController
import com.practice.project.androidbootcamp.utilities.NetworkUtilities
import java.util.*

class VenuesViewModel : ViewModel() {

    var mVenues: MutableLiveData<List<Venue>>? = null
    private val mNetworkUtilities = NetworkUtilities()
    var mContext: Context? = null

    fun getVenues(): LiveData<List<Venue>> {
        if (mVenues == null) {
            mVenues = MutableLiveData()

            if (mNetworkUtilities.isConnectedToNetwork(mContext!!)) {
                MainActivity.sGeoLocation.observeForever { geoLocation -> loadVenuesFromFourSquareAPI(geoLocation!![0].toString() + "," + geoLocation[1]) }
            } else {
                LoadVenuesFromDatabaseTask().execute()
            }
        }
        return mVenues as MutableLiveData<List<Venue>>
    }

    private fun loadVenuesFromFourSquareAPI(geoLocation: String) {
        val fourSquareAPIController = FourSquareAPIController()
        fourSquareAPIController.mGeoLocation = geoLocation
        fourSquareAPIController.mVenues = mVenues
        fourSquareAPIController.start()
    }

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
                                venue.location = location
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
            mVenues?.value = venues
        }
    }
}