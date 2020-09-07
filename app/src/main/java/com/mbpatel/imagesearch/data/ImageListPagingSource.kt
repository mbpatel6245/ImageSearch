package com.mbpatel.imagesearch.data

import androidx.paging.PagingSource
import com.mbpatel.imagesearch.api.ServiceGenerator
import com.mbpatel.imagesearch.model.SearchItemData
import com.mbpatel.imagesearch.utils.NoAnyDataException
import com.mbpatel.imagesearch.utils.NoMoreDataException
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class ImageListPagingSource(
    private val service: ServiceGenerator,
    private val query: String
) : PagingSource<Int, SearchItemData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchItemData> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.searchImages(position, query, SEARCH_TYPE)
            val data = response.data
            when {
                response.status == 500 -> throw NoMoreDataException("You have reached end of the data!")
                response.success!! && response.status == 200 && response.data.isEmpty() -> {
                    throw NoAnyDataException("No Any Data Found!")
                }
                else -> LoadResult.Page(
                    data = data,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (data.isEmpty()) null else position + 1
                )
            }

        } catch (exception: UnknownHostException) {
            return LoadResult.Error(exception)
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: NoMoreDataException) {
            return LoadResult.Error(exception)
        } catch (exception: NoAnyDataException) {
            return LoadResult.Error(exception)
        }
    }

}