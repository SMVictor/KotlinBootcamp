package com.practice.project.androidbootcamp

import android.Manifest
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.practice.project.androidbootcamp.data.VenueDatabase
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager
    private lateinit var mAdapter: ViewPagerAdapter
    private lateinit var mLocManager: LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationStart()

        mTabLayout = findViewById(R.id.principal_tab)
        mViewPager = findViewById(R.id.principal_view_pager)
        mAdapter = ViewPagerAdapter(supportFragmentManager)

        sVenuesAppDatabase = Room.databaseBuilder(applicationContext, VenueDatabase::class.java, "venuesdb").allowMainThreadQueries().build()

        //val actionBar = supportActionBar
       // actionBar!!.elevation = 0f

        createFragments()
    }

    private fun createFragments() {
        mAdapter.addFragment(ListFragment(), getString(R.string.venues_list_tap_title))
        mAdapter.addFragment(MapFragment(), getString(R.string.venues_map_tap_title))
        mViewPager.adapter = mAdapter
        mTabLayout.setupWithViewPager(mViewPager)

        mTabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_map)
        mTabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_local_activity)
    }

    /*
     * To get current location, a resource must be created outside the view model to obtain the mVenues
     * */
    private fun locationStart() {
        //It is verified if the user grants the permissions necessary to use her/his current position.
        mLocManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsEnabled) {
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            Toast.makeText(this, "Please proceed to make your request again", Toast.LENGTH_LONG).show()
            return
        }
        // It is initialized the LocationListener Manager
        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000f, object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val geoLocation = ArrayList<Double>()
                geoLocation.add(location.latitude)
                geoLocation.add(location.longitude)
                sGeoLocation.value = geoLocation
            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

            }

            override fun onProviderEnabled(s: String) {

            }

            override fun onProviderDisabled(s: String) {

            }
        })
    }

    companion object {
        lateinit var sVenuesAppDatabase: VenueDatabase
        var sGeoLocation = MutableLiveData<List<Double>>()
    }
}
