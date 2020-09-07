package com.mbpatel.imagesearch.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CommentDao {

    @Query("SELECT * FROM comment WHERE image_id = :imageId")
    fun getComment(imageId: String): LiveData<Comment>

    @Insert
    suspend fun insertComment(mComment: Comment): Long

    @Query("SELECT EXISTS(SELECT 1 FROM comment WHERE image_id = :imageId LIMIT 1)")
    fun isAddedComment(imageId: String): LiveData<Boolean>

    @Query("UPDATE comment SET image_comment = :imageComment WHERE image_id = :imageId")
    suspend fun updateComment(imageId: String, imageComment: String): Int
}