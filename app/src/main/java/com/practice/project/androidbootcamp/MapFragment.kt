package com.practice.project.androidbootcamp

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
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


class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var venues: List<Venue>? = null
    private var mVenuesViewModel: VenuesViewModel? = null
    private var mCurrentLatitude: Double? = null
    private var mCurrentLongitude: Double? = null
    private var mLocManager: LocationManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = super.onCreateView(inflater, container, savedInstanceState)
        venues = ArrayList<Venue>()
        getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap!!.setOnMarkerClickListener { marker ->
            for (i in venues!!.indices) {
                if (venues!![i].venueId == marker.tag) {
                    val destinationClass = DetailActivity::class.java
                    val intentToStartDetailActivity = Intent(context, destinationClass)
                    intentToStartDetailActivity.putExtra("Venue", venues!![i])
                    context!!.startActivity(intentToStartDetailActivity)
                }
            }
            false
        }

        locationStart()

        mVenuesViewModel = ViewModelProviders.of(activity!!).get(VenuesViewModel::class.java)
        mVenuesViewModel!!.activity = activity
        mVenuesViewModel!!.context = context
        mVenuesViewModel!!.venues!!.observe(this, Observer { venues ->
            this.venues = venues
            setPointsInMap()
        })
    }

    fun setInitialLocationMap() {
        val initialPosition = LatLng(mCurrentLatitude!!, mCurrentLongitude!!)
        mMap!!.addMarker(MarkerOptions()
                .position(initialPosition)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).tag = -1
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 16f))
    }

    fun setPointsInMap() {
        if (venues!!.size > 1) {

            for (i in venues!!.indices) {
                this.mMap!!.addMarker(MarkerOptions()
                        .title(venues!![i].name)
                        .position(LatLng(venues!![i].location.lat, venues!![i].location.lng))).tag = venues!![i].venueId
                if (i != venues!!.size - 1) {
                }
            }
        } else {
            this.mMap!!.addMarker(MarkerOptions()
                    .title(venues!![0].name)
                    .position(LatLng(venues!![0].location.lat, venues!![0].location.lng))).tag = venues!![0].venueId
        }
    }

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
            return
        }
        // It is initialized the LocationListener Manager
        mLocManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000f, object : LocationListener {
            override fun onLocationChanged(location: Location) {
                mCurrentLatitude = location.latitude
                mCurrentLongitude = location.longitude
                setInitialLocationMap()
            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

            }

            override fun onProviderEnabled(s: String) {

            }

            override fun onProviderDisabled(s: String) {

            }
        })
    }
}
