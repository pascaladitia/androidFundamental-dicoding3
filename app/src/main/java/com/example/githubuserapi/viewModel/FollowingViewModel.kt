package com.example.githubuserapi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.example.githubuserapi.BuildConfig
import com.example.githubuserapi.model.Following
import org.json.JSONArray


class FollowingViewModel : ViewModel() {

    private val listFollowing = MutableLiveData<ArrayList<Following>>()

    fun setFollowing(username: String) {
        val listItems = ArrayList<Following>()

        val url = BuildConfig.BASE_URL+"users/$username/following"
        val token = BuildConfig.TOKEN

        AndroidNetworking.get(url)
            .addPathParameter("username", username)
            .addHeaders("Authorization", "token $token")
            .setTag(this)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    try {
                        for (i in 0 until response.length()) {
                            val following = response.getJSONObject(i)
                            val followingItems = Following()
                            followingItems.apply {
                                id = following.getInt("id")
                                login = following.getString("login")
                                avatar_url = following.getString("avatar_url")
                            }
                            listItems.add(followingItems)
                        }

                        listFollowing.postValue(listItems)
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                    }
                }

                override fun onError(error: ANError) {
                    Log.d("onFailure", error.message.toString())
                }
            })
    }

    fun getFollowing(): LiveData<ArrayList<Following>> {
        return listFollowing
    }
}