package com.mbpatel.imagesearch.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mbpatel.imagesearch.data.ImageRepository
import com.mbpatel.imagesearch.db.CommentRepository

/**
 * Factory for creating a [ImageDetailViewModel] with a constructor that takes a [ImageRepository]
 * and an ID for the current [Comment].
 */
class ImageDetailViewModelFactory(
    private val commentRepository: CommentRepository,
    private val imageId:String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val vModel:ViewModel= ImageDetailViewModel(commentRepository,imageId) as T
        return vModel as T
    }
}
