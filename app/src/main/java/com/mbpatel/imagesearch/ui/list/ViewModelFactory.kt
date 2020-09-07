package com.mbpatel.imagesearch.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mbpatel.imagesearch.data.ImageRepository

/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val repository: ImageRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchImagesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchImagesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
