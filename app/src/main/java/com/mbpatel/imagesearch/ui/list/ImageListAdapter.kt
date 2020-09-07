package com.mbpatel.imagesearch.ui.list

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mbpatel.imagesearch.model.SearchItemData

/**
 * Adapter for the list of repositories.
 */
class ImageListAdapter : PagingDataAdapter<SearchItemData, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as ImageListViewHolder).bind(repoItem)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<SearchItemData>() {
            override fun areItemsTheSame(oldItem: SearchItemData, newItem: SearchItemData): Boolean =
                    oldItem.id == newItem.id

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: SearchItemData, newItem: SearchItemData): Boolean =
                    oldItem == newItem
        }
    }

}
