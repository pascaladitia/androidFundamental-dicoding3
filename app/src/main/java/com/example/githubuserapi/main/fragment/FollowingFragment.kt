package com.example.githubuserapi.main.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.R
import com.example.githubuserapi.adapter.FollowingUserAdapter
import com.example.githubuserapi.viewModel.FollowingViewModel
import kotlinx.android.synthetic.main.activity_fragment.*


class FollowingFragment : Fragment() {

    companion object {
        const val EXTRA_LOGIN = "extra_login"
    }

    private lateinit var adapter: FollowingUserAdapter
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var username: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)

        username = activity?.intent?.getStringExtra(EXTRA_LOGIN).toString()

        showLoading(true)
        followingViewModel.setFollowing(username)

        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer { followingItems ->
            if (followingItems != null) {
                adapter.setData(followingItems)
                showLoading(false)
            }
        })

        recyclerViewConfig()
    }

    private fun recyclerViewConfig() {
        adapter = FollowingUserAdapter()

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