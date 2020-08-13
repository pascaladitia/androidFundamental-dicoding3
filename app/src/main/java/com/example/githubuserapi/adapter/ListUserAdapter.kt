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
import com.pascal.githubuserapi.data.DataUser
import kotlinx.android.synthetic.main.item_row_user.view.*

class ListUserAdapter(private val list: (DataUser) -> Unit) :
    RecyclerView.Adapter<ListUserAdapter.UserViewHolder>() {

    private val mData = ArrayList<DataUser>()

    fun setData(items: ArrayList<DataUser>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position], list)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: DataUser, list: (DataUser) -> Unit) {
            with(itemView) {
                Glide.with(context)
                    .load(user.avatar_url)
                    .apply(RequestOptions()
                        .override(70,70)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH))
                    .into(item_row_avatar)
                item_row_name.text = user.login

                setOnClickListener{ list(user) }
            }
        }
    }
}
