package com.example.githubuserapi.main

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapi.R
import com.example.githubuserapi.adapter.SectionPagerAdapter
import com.example.githubuserapi.alarm.AlarmActivity
import com.example.githubuserapi.db.DatabaseGithub
import com.example.githubuserapi.db.DatabaseGithub.FavColums.Companion.CONTENT_URI
import com.example.githubuserapi.db.FavoriteHelper
import com.example.githubuserapi.helper.MappingHelper
import com.example.githubuserapi.model.Favorite
import com.example.githubuserapi.viewModel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.tab_layout.*


class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var favHelper: FavoriteHelper
    private lateinit var username: String
    private lateinit var avatarUrl: String
    private var menuItem: Menu? = null
    private var isFav: Boolean = false
    private lateinit var uriWithId: Uri
    private var fav: Favorite? = null

    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + fav?.id)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        if (cursor != null) {
            fav = MappingHelper.mapCursorToObject(cursor)
            cursor.close()
        }

        setData()
        favoriteOptions()
        viewPagerOptions()
        initToolbar()
    }

    private fun setData() {
        favHelper = FavoriteHelper.getInstance(applicationContext)
        favHelper.open()

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)

        username = intent?.getStringExtra(EXTRA_LOGIN).toString()

        detailViewModel.setDetailUser(username)

        detailViewModel.getDetailUser().observe(this, Observer { detailUserItems ->
            if (detailUserItems != null) {
                Glide.with(this)
                    .load(detailUserItems[0].avatar_url)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_baseline_account_circle_24)
                            .error(R.drawable.ic_baseline_account_circle_24)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                    )
                    .into(detail_avatar)
                detail_name.text = detailUserItems[0].login
                detail_username.text = detailUserItems[0].name
                detail_repository.text = detailUserItems[0].public_repos
                detail_followers.text = detailUserItems[0].followers
                detail_following.text = detailUserItems[0].following
            }
        })
    }

    private fun viewPagerOptions() {
        val sectionsPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tab_layout.setupWithViewPager(viewPager)
    }

    private fun favoriteOptions() {
        username = intent?.getStringExtra(EXTRA_LOGIN).toString()
        val result = favHelper.queryByLogin(username)
        val favorite = (1..result.count).map {
            result.apply {
                moveToNext()
                getInt(result.getColumnIndexOrThrow(DatabaseGithub.FavColums.LOGIN))
            }
        }
        if (favorite.isNotEmpty()) isFav = true
    }

    private fun addFavorite() {
        try {
            username = intent?.getStringExtra(EXTRA_LOGIN).toString()
            avatarUrl = intent?.getStringExtra(EXTRA_AVATAR_URL).toString()

            val values = ContentValues().apply {
                put(DatabaseGithub.FavColums.LOGIN, username)
                put(DatabaseGithub.FavColums.AVATAR, avatarUrl)
            }
            contentResolver.insert(CONTENT_URI, values)

            showSnackbarMessage(getString(R.string.add))
            Log.d("INSERT VALUES ::::: ", values.toString())
        } catch (e: SQLiteConstraintException) {
            showSnackbarMessage("" + e.localizedMessage)
            finish()
        }
    }

    private fun removeFavorite() {
        try {
            username = intent?.getStringExtra(EXTRA_LOGIN).toString()
            val result = contentResolver.delete(uriWithId, null, null)

            showSnackbarMessage(getString(R.string.delete))
            Log.d("REMOVE VALUES ::::: ", result.toString())
        } catch (e: SQLiteConstraintException) {
            showSnackbarMessage("" + e.localizedMessage)
            finish()
        }
    }

    private fun favoriteIcon() {
        if (isFav)
            menuItem?.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_red)
        else
            menuItem?.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border_24)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menuItem = menu
        favoriteIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.changeLanguageSetting -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }
            R.id.favoriteMenu -> {
                if (isFav) removeFavorite() else addFavorite()

                isFav = !isFav
                favoriteIcon()
                true
            }
            R.id.alarmMenu -> {
                val intent = Intent(this, AlarmActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun initToolbar() {
        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.title_detail)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(viewPager, message, Snackbar.LENGTH_SHORT).show()
    }
}

