package com.mbpatel.imagesearch.db

class CommentRepository private constructor(private val commentDao: CommentDao) {

    fun getComment(imageId:String) = commentDao.getComment(imageId)

    suspend fun saveComment(
        iId: String,
        iComment: String,
        isEdit: Boolean
    ) {
        if (isEdit)
            commentDao.updateComment(iId, iComment)
        else {
            val mComment = Comment(
                image_id = iId,
                image_comment = iComment
            )
            commentDao.insertComment(mComment)
        }
    }

    companion object {
        @Volatile
        private var instance: CommentRepository? = null

        fun getInstance(commentDao: CommentDao) =
            instance ?: synchronized(this) {
                instance ?: CommentRepository(commentDao).also { instance = it }
            }
    }
}