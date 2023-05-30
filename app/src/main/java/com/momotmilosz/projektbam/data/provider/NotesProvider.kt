package com.momotmilosz.projektbam.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.momotmilosz.projektbam.SecretApplication

class NotesProvider : ContentProvider() {

    private val noteDao by lazy { (SecretApplication.appContext as SecretApplication).database.noteDao() }
    private val PROVIDER_NAME = "com.momotmilosz.projektbam"
    private val URI_CODE = 101

    companion object {
        var uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }

    override fun onCreate(): Boolean {
        uriMatcher.addURI(PROVIDER_NAME, "notes", URI_CODE)
        uriMatcher.addURI(PROVIDER_NAME, "notes/*", URI_CODE)
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        require(uriMatcher.match(uri) == URI_CODE)
        { "Unknown URI $uri" }

        val cursor = noteDao.getAllCursor()
        cursor.setNotificationUri(SecretApplication.appContext.contentResolver,uri)

        return cursor
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}