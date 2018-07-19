package com.practice.project.androidbootcamp.viewmodel

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView

class ViewModelBindings {

   companion object {
       @JvmStatic
       @BindingAdapter("recyclerViewViewModel")
       fun setRecyclerViewViewModel(recyclerView: RecyclerView,
                                    viewModel: RecyclerViewViewModel) {
           viewModel.setupRecyclerView(recyclerView)
       }
   }
}
