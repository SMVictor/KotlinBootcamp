package com.practice.project.androidbootcamp

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practice.project.androidbootcamp.databinding.FragmentListBinding
import com.practice.project.androidbootcamp.viewmodel.RecyclerViewViewModel
import com.practice.project.androidbootcamp.viewmodel.VenuesViewModel
import android.arch.lifecycle.ViewModelProviders


class ListFragment : Fragment() {
    private var mVenuesViewModel: VenuesViewModel? = null
    private var recyclerViewViewModel: RecyclerViewViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mVenuesViewModel = ViewModelProviders.of(activity!!).get(VenuesViewModel::class.java)
        mVenuesViewModel!!.activity = activity
        mVenuesViewModel!!.context = context
        mVenuesViewModel!!.venues!!.observe(this, Observer { venues ->
            recyclerViewViewModel!!.setVenues(venues!!)
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_list, container, false)
        var binding = FragmentListBinding.bind(view)
        recyclerViewViewModel = RecyclerViewViewModel(activity!!)
        binding.setViewModel(recyclerViewViewModel)
        return view
    }
}
