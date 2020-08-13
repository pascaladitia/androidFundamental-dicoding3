package com.example.githubuserapi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.githubuserapi.BuildConfig
import com.pascal.githubuserapi.data.DataUser
import org.json.JSONObject


class UserViewModel : ViewModel() {

    private val listUsers = MutableLiveData<ArrayList<DataUser>>()

    fun setUser(username: String) {
        val listItems = ArrayList<DataUser>()

        val url = BuildConfig.BASE_URL+"search/users?q=$username"
        val token = BuildConfig.TOKEN

        AndroidNetworking.get(url)
            .addPathParameter("username", username)
            .addHeaders("Authorization", "token $token")
            .setTag(this)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        //parsing json
                        val list = response.getJSONArray("items")

                        for (i in 0 until list.length()) {
                            val user = list.getJSONObject(i)
                            val userItems = DataUser()
                            userItems.apply {
                                id = user.getInt("id")
                                login = user.getString("login")
                                avatar_url = user.getString("avatar_url")
                            }
                            listItems.add(userItems)
                        }

                        listUsers.postValue(listItems)
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                    }
                }

                override fun onError(error: ANError) {
                    Log.d("onFailure", error.message.toString())
                }
            })
    }

    fun getUsers(): LiveData<ArrayList<DataUser>> {
        return listUsers
    }
}