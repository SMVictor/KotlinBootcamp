package com.practice.project.androidbootcamp

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.practice.project.androidbootcamp.data.VenueDatabase

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mTabLayout: TabLayout? = null
    private var mViewPager: ViewPager? = null
    private var mAdapter: ViewPagerAdapter? = null
    companion object {
        var mVenuesAppDatabase: VenueDatabase? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mVenuesAppDatabase = Room.databaseBuilder(applicationContext, VenueDatabase::class.java, "venuesdb").allowMainThreadQueries().build()

        mTabLayout = findViewById(R.id.principal_tab) as TabLayout
        mViewPager = findViewById(R.id.principal_view_pager) as ViewPager
        mTabLayout!!.setupWithViewPager(mViewPager)
        setUpViewPager()
    }

    private fun setUpViewPager() {
        mAdapter = ViewPagerAdapter(supportFragmentManager)
        mAdapter!!.addFragment(ListFragment(), getString(R.string.venues_list_tap_title))
        mAdapter!!.addFragment(MapFragment(), getString(R.string.venues_map_tap_title))
        mViewPager!!.setAdapter(mAdapter)

        mTabLayout!!.getTabAt(1)!!.setIcon(R.drawable.ic_map)
        mTabLayout!!.getTabAt(0)!!.setIcon(R.drawable.ic_local_activity)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
