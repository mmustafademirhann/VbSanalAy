package com.example.socialmediavbsanalay.domain.model

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField


import java.io.Serializable

data class Post(
    val imageResId: String, // Resource ID of the image for the story
    val username: String     // Username of the user associated with the story
     // URL of the user's profile photo
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageResId)
        parcel.writeString(username)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}