package com.example.baseappkoin.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.os.bundleOf
import com.example.baseappkoin.extension.getDate
import java.io.*


object AppUtils {


    const val READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    const val WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    const val NOTIFICATION_PERMISSION = Manifest.permission.ACCESS_NOTIFICATION_POLICY
    const val REQUEST_CODE_REQUEST_PERMISSION = 999

    fun openBrowser(url: String, context: Context) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    fun saveBitmap(
        context: Context, bitmap: Bitmap
    ): Uri {
        val relativeLocation = Environment.DIRECTORY_PICTURES
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, makeFileName())
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
        val resolver = context.contentResolver
        var stream: OutputStream? = null
        var uri: Uri? = null
        return try {
            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            uri = resolver.insert(contentUri, contentValues)
            if (uri == null) {
                throw IOException("Failed to create new MediaStore record.")
            }
            stream = resolver.openOutputStream(uri)
            if (stream == null) {
                throw IOException("Failed to get output stream.")
            }
            if (!bitmap.compress(CompressFormat.JPEG, 95, stream)) {
                throw IOException("Failed to save bitmap.")
            }
            uri
        } catch (e: IOException) {
            if (uri != null) {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null)
            }
            throw e
        } finally {
            stream?.close()
        }
    }

    private fun makeFileName(): String {
        return "image" + System.currentTimeMillis() + ".jpg"
    }

    @SuppressLint("Range")
    fun getContentType(contentResolver: ContentResolver, uri: Uri?): String? {
        var type: String? = null
        val cursor = contentResolver.query(uri!!, null, null, null, null)
        try {
            if (cursor != null) {
                cursor.moveToFirst()
                type = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE))
                cursor.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (TextUtils.isEmpty(type)) "application/octet-stream" else type
    }


    @SuppressLint("Range")
    fun getDateTaken(contentResolver: ContentResolver, uri: Uri): Long {
        var date: Long = 0
        val cursor = getCursor(uri,contentResolver)
        try {
            if (cursor != null) {
                cursor.moveToFirst()
                date = cursor.getDate()
                cursor.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return date
    }

    private fun getCursor(uri: Uri, contentResolver: ContentResolver): Cursor? {
        return queryLocal(uri,contentResolver ,null,null, null)
    }

    private fun queryLocal(
        uri: Uri,
        contentResolver: ContentResolver,
        selection: String?,
        offset: Int?,
        limit: Int?
    ): Cursor? {

        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            var query = "${MediaStore.Files.FileColumns.DATE_MODIFIED} desc"
            if (limit != null) {
                query += " limit $limit offset $offset"
            }
            contentResolver.query(
                uri,
                PROJECTION,
                selection,
                null,
                query
            )
        } else {
            contentResolver.query(
                uri,
                null,
                bundleOf(
                    ContentResolver.QUERY_ARG_SQL_SELECTION to selection,
                    ContentResolver.QUERY_ARG_LIMIT to limit,
                    ContentResolver.QUERY_ARG_OFFSET to offset,
                    ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Files.FileColumns.DATE_MODIFIED),
                    ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                ),
                null,
            )
        }
    }

    private val PROJECTION = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Files.FileColumns.DATE_MODIFIED,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.SIZE,
        MediaStore.Files.FileColumns.DATE_TAKEN
    )

}