package com.practice.project.androidbootcamp.viewmodel

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.practice.project.androidbootcamp.MainActivity
import com.practice.project.androidbootcamp.model.Venue
import com.practice.project.androidbootcamp.utilities.FourSquareAPIController
import com.practice.project.androidbootcamp.utilities.NetworkUtilities

class VenuesViewModel : ViewModel() {

    private var mVenues: MutableLiveData<List<Venue>>? = null
    var context: Context? = null
    ////////////////////////////////// Getters and Setters//////////////////////////////////////////
    var activity: Activity? = null
    var longitude: Double? = null
    var latitude: Double? = null
    private var mLocManager: LocationManager? = null
    private var mNetworkUtilities = NetworkUtilities()
    ////////////////////////////////////////////////////////////////////////////////////////////////

    var venues: LiveData<List<Venue>>? = null
        get() {
            if (mVenues == null) {
                mVenues = MutableLiveData<List<Venue>>()
                if (mNetworkUtilities.isConnectedToNetwork(context!!)) {
                    locationStart()
                } else {
                    loadVenuesFromDatabase()
                }
            }
            return mVenues as MutableLiveData<List<Venue>>
        }

    /*
    * To get current location, a resource must be created outside the view model to obtain the venues
    * */
    private fun locationStart() {
        //It is verified if the user grants the permissions necessary to use her/his current position.
        mLocManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = mLocManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsEnabled) {
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context!!.startActivity(settingsIntent)
        }

        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            Toast.makeText(context, "Please proceed to make your request again", Toast.LENGTH_LONG).show()
            return
        }
        // It is initialized the LocationListener Manager
        mLocManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000f, object : LocationListener {
            override fun onLocationChanged(location: Location) {
                latitude = location.latitude
                longitude = location.longitude
                loadVenuesFromFourSquareAPI()
            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

            }

            override fun onProviderEnabled(s: String) {

            }

            override fun onProviderDisabled(s: String) {

            }
        })
    }

    private fun loadVenuesFromFourSquareAPI() {
        var geoLocation = latitude.toString() + "," + longitude
        var fourSquareAPIController = FourSquareAPIController()
        fourSquareAPIController.setmGeoLocation(geoLocation)
        fourSquareAPIController.venues=mVenues
        fourSquareAPIController.start()
    }

    private fun loadVenuesFromDatabase() {
        try {
            val venuesList = MainActivity.mVenuesAppDatabase!!.venueDao().all
            val categoriesList = MainActivity.mVenuesAppDatabase!!.categoryDao().all
            val locationList = MainActivity.mVenuesAppDatabase!!.locationDao().all
            if (venuesList.size != 0) {
                for (venue in venuesList) {
                    for (category in categoriesList) {
                        if (venue.categoryId === category.categoryId) {
                            venue.categories.add(category)
                            break
                        }
                    }
                    for (location in locationList) {
                        if (venue.locationId === location.locationId) {
                            venue.location = location
                            break
                        }
                    }
                }
                //The list of venues of the view model is loaded
                mVenues!!.value = venuesList
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}