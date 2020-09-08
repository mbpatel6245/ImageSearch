package com.mbpatel.imagesearch.api

import com.google.gson.annotations.SerializedName
import com.mbpatel.imagesearch.model.SearchItemData
/** Used for Initial search response */
data class SearchResponse (
    @SerializedName("data")
    var data: List<SearchItemData> = emptyList(),

    @SerializedName("success")
    var success: Boolean? = null,

    @SerializedName("status")
    var status: Int? = null
)