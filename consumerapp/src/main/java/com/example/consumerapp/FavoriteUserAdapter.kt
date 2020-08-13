package com.example.consumerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_user.view.*

class FavoriteUserAdapter() : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteViewHolder>() {

    var mData = ArrayList<Favorite>()
        set(mData) {
            if (mData.size > 0) {
                this.mData.clear()
            }
            this.mData.addAll(mData)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = this.mData.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favUser: Favorite) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(favUser.avatar_url)
                    .apply(
                        RequestOptions()
                            .override(56, 56)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                    )
                    .into(item_row_avatar)
                item_row_name.text = favUser.login

            }
        }
    }
}