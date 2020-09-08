package com.mbpatel.imagesearch.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbpatel.imagesearch.db.CommentRepository
import kotlinx.coroutines.launch

class ImageDetailViewModel(var commentRepository: CommentRepository, var imageId: String) :
    ViewModel() {

    /** Fetch the existing comment data */
    var fetchComment = commentRepository.getComment(imageId)

    /**
     * Use for the save/edit comment data
     * @param iComment string comment
     * @param isEdit boolean
      */
    fun addComment(
        iComment: String,
        isEdit: Boolean
    ) {
        viewModelScope.launch {
            commentRepository.saveComment(imageId, iComment, isEdit)
        }
    }
}