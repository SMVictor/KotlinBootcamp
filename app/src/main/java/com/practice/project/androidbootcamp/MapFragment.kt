package com.practice.project.androidbootcamp

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.practice.project.androidbootcamp.model.Venue
import com.practice.project.androidbootcamp.viewmodel.VenuesViewModel
import java.util.*


class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var mVenues: List<Venue> = ArrayList()
    private var mVenuesViewModel: VenuesViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = super.onCreateView(inflater, container, savedInstanceState)
        getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap!!.setOnMarkerClickListener { marker ->
            for (i in mVenues.indices) {
                if (mVenues[i].venueId == marker.tag) {
                    val destinationClass = DetailActivity::class.java
                    val intentToStartDetailActivity = Intent(context, destinationClass)
                    intentToStartDetailActivity.putExtra("Venue", mVenues[i])
                    context!!.startActivity(intentToStartDetailActivity)
                }
            }
            false
        }

        MainActivity.sGeoLocation.observeForever { geoLocation -> setInitialLocationMap(geoLocation!![0], geoLocation[1]) }

        mVenuesViewModel = ViewModelProviders.of(activity!!).get(VenuesViewModel::class.java)
        mVenuesViewModel!!.mContext = context
        mVenuesViewModel!!.getVenues().observe(this, android.arch.lifecycle.Observer { venues ->
            mVenues = venues!!
            setPointsInMap()
        })
    }

    private fun setInitialLocationMap(latitude: Double?, longitude: Double?) {
        val initialPosition = LatLng(latitude!!, longitude!!)
        mMap!!.addMarker(MarkerOptions()
                .position(initialPosition)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).tag = -1
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 16f))
    }

    private fun setPointsInMap() {
        for (i in mVenues.indices) {
            mMap!!.addMarker(MarkerOptions()
                    .title(mVenues[i].name)
                    .position(LatLng(mVenues[i].location.lat, mVenues[i].location.lng))).tag = mVenues[i].venueId
        }
    }
}
