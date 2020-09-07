package com.mbpatel.imagesearch.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SearchItemData : Serializable {
    @SerializedName("id")
   var id: String? = null

   @SerializedName("account_id")
   var accountId: Long =0

    @SerializedName("title")
    var title: String? = null

    @SerializedName("images")
    var searchImages: List<SearchImage>? = null

    companion object {
        private const val serialVersionUID = 9163122631598450484L
    }
}