package com.mbpatel.imagesearch.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.mbpatel.imagesearch.R
import com.mbpatel.imagesearch.databinding.ReposLoadStateFooterViewItemBinding

/**
 * Used for the update load state item view holder
 */
class PagingLoadStateViewHolder(
    private val binding: ReposLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.ibRefresh.setOnClickListener { retry.invoke() }
    }

    /**
     * Bind the loadstate data
     *
     * @param loadState pass the load state
     */
    fun bind(loadState: LoadState) {
        binding.pbLoader.isVisible = loadState is LoadState.Loading
        binding.ibRefresh.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): PagingLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.repos_load_state_footer_view_item, parent, false)
            val binding = ReposLoadStateFooterViewItemBinding.bind(view)
            return PagingLoadStateViewHolder(binding, retry)
        }
    }
}