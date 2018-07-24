package com.practice.project.androidbootcamp.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.practice.project.androidbootcamp.DetailActivity
import com.practice.project.androidbootcamp.databinding.VenueBinding
import com.practice.project.androidbootcamp.model.Venue


class VenuesAdapter : RecyclerView.Adapter<VenuesAdapter.VenueAdapterViewHolder>() {

    private var mVenuesData: List<Venue>? = ArrayList()
    private var layoutInflater: LayoutInflater? = null

    /**
     * Cache of the children views for a venue list item.
     */
    inner class VenueAdapterViewHolder(private val venueBinding: VenueBinding) : RecyclerView.ViewHolder(venueBinding.root) {
        fun bind(venue: Venue) {
            this.venueBinding.venueModel = venue
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VenueAdapterViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.context)
        }
        var venueBinding = VenueBinding.inflate(layoutInflater!!, viewGroup, false)

        var venueAdapterViewHolder = VenueAdapterViewHolder(venueBinding)

        venueAdapterViewHolder.itemView.setOnClickListener {
            val destinationClass = DetailActivity::class.java
            val intentToStartDetailActivity = Intent(viewGroup.context, destinationClass)
            intentToStartDetailActivity.putExtra("Venue", mVenuesData!![venueAdapterViewHolder.adapterPosition])
            viewGroup.context.startActivity(intentToStartDetailActivity)
        }

        return venueAdapterViewHolder
    }

    override fun onBindViewHolder(venueAdapterViewHolder: VenueAdapterViewHolder, position: Int) {
        var venue = mVenuesData!![position]
        venueAdapterViewHolder.bind(venue)
    }

    override fun getItemCount(): Int {
        return if (null == mVenuesData) 0 else mVenuesData!!.size
    }

    fun setVenueData(venueData: List<Venue>) {
        mVenuesData = venueData
        notifyDataSetChanged()
    }
}