package com.example.githubuserapi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.githubuserapi.BuildConfig
import com.example.githubuserapi.model.Detail
import org.json.JSONObject


class DetailViewModel : ViewModel() {

    private val listDetail = MutableLiveData<ArrayList<Detail>>()

    fun setDetailUser(username: String) {
        val listItems = ArrayList<Detail>()

        val url = BuildConfig.BASE_URL+"users/$username"
        val token = BuildConfig.TOKEN

        AndroidNetworking.get(url)
            .addPathParameter("username", username)
            .addHeaders("Authorization", "token $token")
            .setTag(this)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                       
                        val detailUserItems = Detail()
                        detailUserItems.apply {
                            id = response.getInt("id")
                            login = response.getString("login")
                            avatar_url = response.getString("avatar_url")
                            name = response.getString("name")
                            company = response.getString("company")
                            location = response.getString("location")
                            public_repos = response.getString("public_repos")
                            followers = response.getString("followers")
                            following = response.getString("following")
                        }
                        listItems.add(detailUserItems)

                        listDetail.postValue(listItems)
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                    }
                }

                override fun onError(error: ANError) {
                    Log.d("onFailure", error.message.toString())
                }
            })
    }

    fun getDetailUser(): LiveData<ArrayList<Detail>> {
        return listDetail
    }
}
