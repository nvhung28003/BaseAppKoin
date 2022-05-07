package com.example.baseappkoin.extension

import android.annotation.SuppressLint
import android.database.Cursor
import android.provider.MediaStore

@SuppressLint("Range")
fun Cursor.getDate() : Long{
    val dateTaken =
        this.getLong(this.getColumnIndex(MediaStore.Files.FileColumns.DATE_TAKEN)) * 1000
    val dateModifier =
        this.getLong(this.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)) * 1000L
    return  if (dateTaken == 0.toLong()){
        dateModifier
    }else dateTaken
}