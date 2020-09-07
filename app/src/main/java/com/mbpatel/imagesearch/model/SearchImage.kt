package com.mbpatel.imagesearch.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SearchImage : Serializable {
    @SerializedName("id")
    var id: String = ""

    @SerializedName("type")
    var type: String = ""

    @SerializedName("link")
    var link: String = ""

    companion object {
        private const val serialVersionUID = -6252212433091453359L
    }
}