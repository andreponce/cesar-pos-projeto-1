package com.example.projeto1.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fruit(
    val name :String,
    val description : String,
    val imageURI :Uri
) : Parcelable