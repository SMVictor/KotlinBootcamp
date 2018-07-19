package com.practice.project.androidbootcamp.utilities

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.gson.GsonBuilder
import com.practice.project.androidbootcamp.MainActivity
import com.practice.project.androidbootcamp.model.Category
import com.practice.project.androidbootcamp.model.JsonResponse
import com.practice.project.androidbootcamp.model.Venue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FourSquareAPIController : Callback<JsonResponse> {
    private var mGeoLocation: String? = null
    var venues: MutableLiveData<List<Venue>>? = null

    fun start() {

        val gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        val fourSquareAPI = retrofit.create(FourSquareAPI::class.java)

        val call = fourSquareAPI.requestSearch(CLIENT_ID, CLIENT_SECRET, API_VERSION, mGeoLocation!!, RADIUS)
        call.enqueue(this)
    }

    override fun onResponse(call: Call<JsonResponse>, response: Response<JsonResponse>) {
        try {
            MainActivity.mVenuesAppDatabase!!.clearAllTables()
        } catch (e: Exception) {
        }

        val venues = response.body()!!.response!!.venues
        for (venue in venues) {
            try {
                if (venue.categories.size === 0) {
                    val category = Category()
                    category.id = "Undefined"
                    category.name = "Undefined"
                    venue.categories.add(category)
                }
                val categoryId = MainActivity.mVenuesAppDatabase!!.categoryDao()
                        .insert(venue.categories[0])
                venue.location.formattedAddressString = venue.location.formattedAddress.toString()
                val locationId = MainActivity.mVenuesAppDatabase!!.locationDao()
                        .insert(venue.location)

                venue.categoryId = categoryId
                venue.locationId = locationId

                val venueId = MainActivity.mVenuesAppDatabase!!.venueDao().insert(venue)
                venue.venueId = venueId
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        this.venues!!.value = venues
    }

    override fun onFailure(call: Call<JsonResponse>, t: Throwable) {
        t.printStackTrace()
    }

    fun getmGeoLocation(): String? {
        return mGeoLocation
    }

    fun setmGeoLocation(mGeoLocation: String) {
        this.mGeoLocation = mGeoLocation
    }

    companion object {

        private val BASE_URL = "https://api.foursquare.com/v2/"
        private val CLIENT_ID = "DJS3YIF02PXN1VHITVGRS3Q43X0XOUZ1R1QDLPCLF4ZYWYBI"
        private val CLIENT_SECRET = "XQUKOG2D00ZIQZ0OB4XNB3TEYGTVDRGDHS343IHVATP5GBEA"
        private val API_VERSION = "20130815"
        private val RADIUS = "1000"
    }
}
