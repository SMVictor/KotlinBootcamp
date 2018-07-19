package com.practice.project.androidbootcamp

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.ArrayList

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val mFragmentsList = ArrayList<Fragment>()
    private val mTitlesList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentsList[position]
    }

    override fun getCount(): Int {
        return mTitlesList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitlesList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentsList.add(fragment)
        mTitlesList.add(title)
    }
}