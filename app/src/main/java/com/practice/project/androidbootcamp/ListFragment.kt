package com.practice.project.androidbootcamp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practice.project.androidbootcamp.databinding.FragmentListBinding
import com.practice.project.androidbootcamp.viewmodel.RecyclerViewViewModel


class ListFragment : Fragment() {
    private var recyclerViewViewModel: RecyclerViewViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val binding = FragmentListBinding.bind(view)
        recyclerViewViewModel = RecyclerViewViewModel(activity!!, context!!)
        binding.viewModel = recyclerViewViewModel
        return view
    }
}
