package com.example.consumerapp

import android.net.Uri
import android.provider.BaseColumns

object DatabaseGithub {

    const val AUTH = "com.example.githubuserapi"
    const val SCHEME = "content"

    internal class FavColums : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val AVATAR = "avatar_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTH)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}
