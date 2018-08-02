package com.practice.project.androidbootcamp.utilities

import android.arch.lifecycle.MutableLiveData
import android.content.Context
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

class FourSquareAPIController(private val mGeoLocation : String, private val mVenues: MutableLiveData<List<Venue>>,
                              private val mContext : Context) : Callback<JsonResponse> {

    private val BASE_URL = "https://api.foursquare.com/v2/"
    private val API_VERSION = "20130815"
    private val RADIUS = "1000"

    private val mFileUtilities: FileUtilities = FileUtilities(mContext)
    private val mClientID: String
    private val mClientSecret: String

    init {
        mClientID = mFileUtilities.getFourSquareData("CLIENT_ID")
        mClientSecret = mFileUtilities.getFourSquareData("CLIENT_SECRET")
    }

    fun start() {

        val gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        val fourSquareAPI = retrofit.create(FourSquareAPI::class.java)

        val call = fourSquareAPI.requestSearch(mClientID, mClientSecret, API_VERSION, mGeoLocation!!, RADIUS)
        call.enqueue(this)
    }

    override fun onResponse(call: Call<JsonResponse>, response: Response<JsonResponse>) {
        try {
            MainActivity.sVenuesAppDatabase.clearAllTables()
        } catch (e: Exception) {
        }

        val venues = response.body()!!.response.venues
        for (venue in venues) {
            try {
                if (venue.categories.size == 0) {
                    val category = Category()
                    category.id = "Undefined"
                    category.name = "Undefined"
                    venue.categories.add(category)
                }
                val categoryId = MainActivity.sVenuesAppDatabase.categoryDao()
                        .insert(venue.categories[0])
                venue.location.formattedAddressString = venue.location.formattedAddress.toString()
                val locationId = MainActivity.sVenuesAppDatabase.locationDao()
                        .insert(venue.location)

                venue.categoryId = categoryId
                venue.locationId = locationId

                val venueId = MainActivity.sVenuesAppDatabase.venueDao().insert(venue)
                venue.venueId = venueId
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        this.mVenues!!.value = venues
    }

    override fun onFailure(call: Call<JsonResponse>, t: Throwable) {
        t.printStackTrace()
    }
}
