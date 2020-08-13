package com.example.githubuserapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapi.R
import com.example.githubuserapi.model.Followers
import kotlinx.android.synthetic.main.item_row_user.view.*

class FollowersUserAdapter : RecyclerView.Adapter<FollowersUserAdapter.FollowersViewHolder>(){

    private val mData = ArrayList<Followers>()

    fun setData(items: ArrayList<Followers>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): FollowersViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false)
        return FollowersViewHolder(view)
    }

    override fun onBindViewHolder(followersViewHolder: FollowersViewHolder, position: Int) {
        followersViewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class FollowersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(followers: Followers) {
            with(itemView) {
                item_row_name.text = followers.login
                Glide.with(context)
                    .load(followers.avatar_url)
                    .apply(RequestOptions()
                        .override(70,70)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH))
                    .into(item_row_avatar)
            }
        }
    }

}