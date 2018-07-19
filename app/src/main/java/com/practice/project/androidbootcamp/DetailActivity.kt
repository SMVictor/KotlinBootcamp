package com.practice.project.androidbootcamp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.practice.project.androidbootcamp.model.Venue

class DetailActivity : AppCompatActivity() {

    private var mTitleVenueDetail: TextView? = null
    private var mAddressVenueDetail: TextView? = null
    private var mCategoryVenueDetail: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        mTitleVenueDetail = findViewById(R.id.tvTitleVenueDetail)
        mAddressVenueDetail = findViewById(R.id.tvAddressVenueDetail)
        mCategoryVenueDetail = findViewById(R.id.tvCategoryVenueDetail)

        val venue = intent.getSerializableExtra("Venue") as Venue

        mTitleVenueDetail!!.setText(venue.name)
        mAddressVenueDetail!!.setText(venue.location.formattedAddressString)
        var categoriesNames = ""
        for (category in venue.categories) {

            categoriesNames += category.name + " "
        }
        mCategoryVenueDetail!!.text = categoriesNames
    }
}