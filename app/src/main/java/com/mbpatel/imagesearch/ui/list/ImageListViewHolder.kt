package com.mbpatel.imagesearch.ui.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mbpatel.imagesearch.R
import com.mbpatel.imagesearch.model.SearchImage
import com.mbpatel.imagesearch.model.SearchItemData
import com.mbpatel.imagesearch.ui.detail.ImageDetailActivity
import com.mbpatel.imagesearch.utils.*
import com.mbpatel.imagesearch.utils.Constants.KEY_IMAGE_ID
import com.mbpatel.imagesearch.utils.Constants.KEY_IMAGE_LINK
import com.mbpatel.imagesearch.utils.Constants.KEY_IMAGE_TITLE
import com.squareup.picasso.Picasso

/**
 * View Holder for a [SearchItemData] RecyclerView list item.
 */
class ImageListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val searchImage: ImageView = view.findViewById(R.id.ivSearchImage)

    private var itemData: SearchItemData? = null

    init {
        view.setOnClickListener {
            var isNotImage = true
            itemData?.let { it1 ->
                it1.searchImages?.let { it2 ->
                    isImageData(it2)?.let {
                        isNotImage = false
                        val mIntent = Intent(view.context, ImageDetailActivity::class.java)
                        mIntent.putExtra(KEY_IMAGE_TITLE, it1.title.toString())
                        mIntent.putExtra(KEY_IMAGE_LINK, it.link)
                        mIntent.putExtra(KEY_IMAGE_ID, (it1.id + it1.accountId))
                        view.context.startActivity(mIntent)
                    }
                }
            }
            if (isNotImage)
                showToast(
                    view.context,
                    view.context.getString(R.string.error_invalid_image)
                )
        }
    }

    /**
     * Bind the searchable item data to viewholder
     * @param data SearchItemData
     */
    fun bind(data: SearchItemData?) {
        var isNotImage = true
        itemData = data
        data?.searchImages?.let {
            itemData = data
            val imageData = isImageData(it)
            if (imageData != null && isValidWebURL(imageData.link)) {
                isNotImage = false
                val size = dip2px(
                    itemView.context,
                    itemView.context.resources.getDimension(R.dimen.image_load_size)
                )
                loadImage(imageData.link, searchImage, size)
            }
        }
        if (isNotImage)
            loadErrorImage(searchImage)
    }


    /**
     * verify to check the image data
     *
     * @param list required list of search images
     */
    private fun isImageData(list: List<SearchImage>): SearchImage? {
        if (list.isEmpty())
            return null
        for (item in list) {
            if (item.type.contains("image/"))
                return item
        }
        return null
    }

    companion object {
        fun create(parent: ViewGroup): ImageListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_list_item, parent, false)
            return ImageListViewHolder(view)
        }
    }
}
