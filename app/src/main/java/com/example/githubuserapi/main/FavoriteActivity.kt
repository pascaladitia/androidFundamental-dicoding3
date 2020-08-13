package com.example.githubuserapi.main

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapi.R
import com.example.githubuserapi.adapter.FavoriteUserAdapter
import com.example.githubuserapi.db.DatabaseGithub.FavColums.Companion.CONTENT_URI
import com.example.githubuserapi.db.FavoriteHelper
import com.example.githubuserapi.helper.MappingHelper
import com.example.githubuserapi.model.Favorite
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteUserAdapter
    private lateinit var favHelper: FavoriteHelper

    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_AVATAR = "extra_avatar_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoriteUserAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        favHelper = FavoriteHelper.getInstance(applicationContext)
        favHelper.open()

        if (savedInstanceState == null) {
            loadFavoriteUserAsync()
        } else {
            val listFav = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_LOGIN)
            if (listFav != null) {
                adapter.mData = listFav
            }
        }

        recyclerViewOptions()
        initToolbar()
    }

    private fun recyclerViewOptions() {
        recycleViewFav.layoutManager = LinearLayoutManager(this)
        recycleViewFav.setHasFixedSize(true)

        adapter = FavoriteUserAdapter{
            val intent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
            intent.apply {
                putExtra(EXTRA_LOGIN, it.login)
                putExtra(EXTRA_AVATAR, it.avatar_url)
            }
            startActivity(intent)
        }

        recycleViewFav.adapter = adapter
    }

    private fun loadFavoriteUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBarFav.visibility = View.VISIBLE
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBarFav.visibility = View.INVISIBLE
            val favorites = deferredFavorites.await()
            if (favorites.size > 0) {
                adapter.mData = favorites
            } else {
                adapter.mData = ArrayList()
                Snackbar.make(recycleViewFav, getString(R.string.empty), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putParcelableArrayList(EXTRA_LOGIN, adapter.mData)
            putParcelableArrayList(EXTRA_AVATAR, adapter.mData)
        }
    }

    override fun onResume() {
        super.onResume()
        recyclerViewOptions()
        loadFavoriteUserAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    private fun initToolbar() {
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.title_favorite)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }
}