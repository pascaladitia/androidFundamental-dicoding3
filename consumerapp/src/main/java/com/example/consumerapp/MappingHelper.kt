package com.example.consumerapp

import android.database.Cursor
import java.util.*

object MappingHelper {

    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<Favorite> {
        val favList = ArrayList<Favorite>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseGithub.FavColums._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseGithub.FavColums.LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseGithub.FavColums.AVATAR))
                favList.add(Favorite(id, login, avatarUrl))
            }
        }
        return favList
    }

    fun mapCursorToObject(notesCursor: Cursor?): Favorite {
        var fav = Favorite()
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseGithub.FavColums._ID))
            val login = getString(getColumnIndexOrThrow(DatabaseGithub.FavColums.LOGIN))
            val avatar = getString(getColumnIndexOrThrow(DatabaseGithub.FavColums.AVATAR))
            fav = Favorite(id, login, avatar)
        }
        return fav
    }
}