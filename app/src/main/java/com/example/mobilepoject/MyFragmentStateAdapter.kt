package com.example.mobilepoject

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mobilepoject.messenger.ChatFragment

class MyFragmentStateAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> ProfileFragment()
            1-> QueryFragment()
            2-> ChatFragment()
            else -> ProfileFragment()
        }
    }

}