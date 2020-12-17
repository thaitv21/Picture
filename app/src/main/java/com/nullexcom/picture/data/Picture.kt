package com.nullexcom.picture.data

import com.google.firebase.database.Exclude
import java.io.Serializable

data class Picture(
        val url: String,
        val userId: String,
        val username: String,
        val userPhoto: String,
        val numOfLikes: Int,
        val isLiked: Boolean = false,
        val template: String
) : Serializable {
    constructor() : this("", "", "", "", 0, false, "")
}