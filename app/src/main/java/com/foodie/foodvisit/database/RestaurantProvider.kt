package com.foodie.foodvisit.database

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

class RestaurantProvider : ContentProvider() {

    private lateinit var restaurantDbHelper: RestaurantDBHelper

    companion object {
        private const val RESTAURANTS = 100
        private const val RESTAURANT_ID = 101

        private val sUriMatcher: UriMatcher = buildUriMatcher()

        private fun buildUriMatcher(): UriMatcher {
            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            val authority = RestaurantContract.AUTHORITY
            matcher.addURI(authority, RestaurantContract.PATH_RESTAURANTS, RESTAURANTS)
            matcher.addURI(authority, "${RestaurantContract.PATH_RESTAURANTS}/#", RESTAURANT_ID)
            return matcher
        }
    }

    override fun onCreate(): Boolean {
        restaurantDbHelper = RestaurantDBHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        val cursor = when (sUriMatcher.match(uri)) {
            RESTAURANTS -> getRestaurants(projection, selection, selectionArgs, sortOrder)
            RESTAURANT_ID -> getRestaurantsWithID(uri, projection, selection, selectionArgs, sortOrder)
            else -> throw UnsupportedOperationException("Unknown uri for query: $uri")
        }
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    private fun getRestaurantsWithID(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        val database = restaurantDbHelper.readableDatabase
        val builder = SQLiteQueryBuilder().apply {
            tables = RestaurantContract.RestaurantEntry.TABLE_NAME
            appendWhere("${RestaurantContract.RestaurantEntry.RESTAURANT_ID}=${uri.lastPathSegment}")
        }
        return builder.query(database, projection, selection, selectionArgs, null, null, sortOrder)
    }

    private fun getRestaurants(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        val database = restaurantDbHelper.readableDatabase
        val builder = SQLiteQueryBuilder().apply {
            tables = RestaurantContract.RestaurantEntry.TABLE_NAME
        }
        return builder.query(database, projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri {
        val db: SQLiteDatabase = restaurantDbHelper.writableDatabase
        return when (sUriMatcher.match(uri)) {
            RESTAURANTS -> {
                val id = db.insertOrThrow(RestaurantContract.RestaurantEntry.TABLE_NAME, null, contentValues)
                if (id > 0) {
                    context!!.contentResolver.notifyChange(uri, null)
                    ContentUris.withAppendedId(RestaurantContract.RestaurantEntry.CONTENT_URI, id)
                } else {
                    throw android.database.SQLException("Failed to insert row into $uri")
                }
            }
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db: SQLiteDatabase = restaurantDbHelper.writableDatabase
        val effectiveSelection = selection ?: "1"
        val rowsDeleted = when (sUriMatcher.match(uri)) {
            RESTAURANTS -> db.delete(
                RestaurantContract.RestaurantEntry.TABLE_NAME,
                effectiveSelection,
                selectionArgs
            )
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }
        if (rowsDeleted != 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return rowsDeleted
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val db: SQLiteDatabase = restaurantDbHelper.writableDatabase
        val rowsUpdated = when (sUriMatcher.match(uri)) {
            RESTAURANTS -> db.update(
                RestaurantContract.RestaurantEntry.TABLE_NAME,
                contentValues,
                selection,
                selectionArgs
            )
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }
        if (rowsUpdated != 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return rowsUpdated
    }
}
