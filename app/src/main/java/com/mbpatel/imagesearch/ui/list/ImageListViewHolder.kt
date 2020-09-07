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
import com.mbpatel.imagesearch.utils.Constants.KEY_IMAGE_ID
import com.mbpatel.imagesearch.utils.Constants.KEY_IMAGE_LINK
import com.mbpatel.imagesearch.utils.Constants.KEY_IMAGE_TITLE
import com.mbpatel.imagesearch.utils.dip2px
import com.mbpatel.imagesearch.utils.isValidWebURL
import com.squareup.picasso.Picasso

/**
 * View Holder for a [SearchItemData] RecyclerView list item.
 */
class ImageListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val searchImage: ImageView = view.findViewById(R.id.ivSearchImage)

    private var itemData: SearchItemData? = null

    init {
        view.setOnClickListener {
            itemData?.let { it1 ->
                it1.searchImages?.let { it2 ->
                    isImageData(it2)?.let {
                        val mIntent = Intent(view.context, ImageDetailActivity::class.java)
                        mIntent.putExtra(KEY_IMAGE_TITLE, it1.title.toString())
                        mIntent.putExtra(KEY_IMAGE_LINK, it.link)
                        mIntent.putExtra(KEY_IMAGE_ID, (it1.id+it1.accountId))
                        view.context.startActivity(mIntent)
                    }
                }
            }


//            Toast.makeText(
//                view.context,
//                "\uD83D\uDE28 Wooops ${itemData!!.searchImages!![0].link}",
//                Toast.LENGTH_LONG
//            ).show()
//            repo?.url?.let { url ->
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                view.context.startActivity(intent)
//            }
        }
    }

    fun bind(data: SearchItemData?) {
        if (data == null)
            Picasso.get().load(R.color.gray).into(searchImage);
        else {
            itemData = data
            data.searchImages?.let {
                itemData = data
                val imageData = isImageData(it)
                if (imageData != null && isValidWebURL(imageData.link)) {
                    val size = dip2px(
                        itemView.context,
                        itemView.context.resources.getDimension(R.dimen.image_load_size)
                    )
                    Picasso.get().load(imageData.link).placeholder(R.color.gray)
                        .resize(size, size).centerCrop()
                        .into(searchImage)
                } else
                    Picasso.get().load(R.color.gray).placeholder(R.color.gray).into(searchImage)
            }
        }
    }

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
