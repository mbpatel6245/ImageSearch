package com.mbpatel.imagesearch.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="comment")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Long =0,
    val image_id:String,
    val image_comment:String
){}