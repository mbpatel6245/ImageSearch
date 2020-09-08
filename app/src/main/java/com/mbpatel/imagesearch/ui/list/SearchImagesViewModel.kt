package com.mbpatel.imagesearch.ui.list

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mbpatel.imagesearch.data.ImageRepository
import com.mbpatel.imagesearch.model.SearchItemData
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel for the [ImageListActivity] screen.
 * The ViewModel works with the [ImageRepository] to get the data.
 */
class SearchImagesViewModel(private val repository: ImageRepository) : ViewModel() {

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<SearchItemData>>? = null

    /**
     * perform the search images and update to the view Model
     */
    fun searchImages(queryString: String): Flow<PagingData<SearchItemData>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<SearchItemData>> = repository.getSearchResultStream(queryString)
                .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}