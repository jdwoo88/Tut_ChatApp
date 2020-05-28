package com.jwoo.astridchatapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jwoo.astridchatapp.fragments.ChatsFragment
import com.jwoo.astridchatapp.fragments.UsersFragment

class SectionpagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> { return UsersFragment() }
            1 -> { return ChatsFragment() }
        }

        return null!!
    }

    override fun getCount(): Int {
        return 2 // fragment count
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> { return "Users" }
            1 -> { return "Chats" }
        }

        return null!!
    }
}