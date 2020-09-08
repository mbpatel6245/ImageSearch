package com.mbpatel.imagesearch.ui.list

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

/**
 * Created for the update loading state adapter
 */
class PagingLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PagingLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoadStateViewHolder {
        return PagingLoadStateViewHolder.create(parent, retry)
    }
}