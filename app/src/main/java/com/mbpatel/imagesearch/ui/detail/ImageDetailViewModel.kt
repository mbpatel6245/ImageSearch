package com.mbpatel.imagesearch.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbpatel.imagesearch.db.CommentRepository
import kotlinx.coroutines.launch

class ImageDetailViewModel(var commentRepository: CommentRepository, var imageId: String) :
    ViewModel() {

    var fetchComment = commentRepository.getComment(imageId)

    fun addComment(
        iComment: String,
        isEdit: Boolean
    ) {
        viewModelScope.launch {
            commentRepository.saveComment(imageId, iComment, isEdit)
        }
    }
}