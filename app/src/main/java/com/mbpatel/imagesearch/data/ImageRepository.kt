package com.mbpatel.imagesearch.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mbpatel.imagesearch.api.ServiceGenerator
import com.mbpatel.imagesearch.model.SearchItemData
import kotlinx.coroutines.flow.Flow

const val STARTING_PAGE_INDEX = 1
const val SEARCH_TYPE="jpg|png|gif"

/**
 * ImageRepository class that works with remote data sources.
 */
class ImageRepository(private val service: ServiceGenerator) {

    fun getSearchResultStream(query: String): Flow<PagingData<SearchItemData>> {
        return Pager(
                config = PagingConfig(
                        pageSize = NETWORK_PAGE_SIZE,
                        enablePlaceholders = true
                ),
                pagingSourceFactory = { ImageListPagingSource(service, query) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}
