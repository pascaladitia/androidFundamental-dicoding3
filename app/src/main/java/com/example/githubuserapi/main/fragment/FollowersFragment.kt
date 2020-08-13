package com.example.githubuserapi.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.R
import com.example.githubuserapi.adapter.FollowersUserAdapter
import com.example.githubuserapi.viewModel.FollowersViewModel
import kotlinx.android.synthetic.main.activity_fragment.*

class FollowersFragment : Fragment() {

    companion object {
        const val EXTRA_LOGIN = "extra_login"
    }

    private lateinit var adapter: FollowersUserAdapter
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var username: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowersViewModel::class.java)

        username = activity?.intent?.getStringExtra(EXTRA_LOGIN).toString()

        showLoading(true)
        followersViewModel.setFollowers(username)

        followersViewModel.getFollowers().observe(viewLifecycleOwner, Observer { followerItems ->
            if (followerItems != null) {
                adapter.setData(followerItems)
                showLoading(false)
            }
        })

        recyclerViewConfig()
    }

    private fun recyclerViewConfig() {
        adapter = FollowersUserAdapter()

        rvFragment.layoutManager = LinearLayoutManager(context)
        rvFragment.adapter = adapter
        rvFragment.setHasFixedSize(true)
        adapter.notifyDataSetChanged()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarFragment.visibility = View.VISIBLE
        } else {
            progressBarFragment.visibility = View.GONE
        }
    }

}