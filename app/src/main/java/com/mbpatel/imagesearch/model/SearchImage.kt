package com.mbpatel.imagesearch.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SearchImage() : Parcelable {
    @SerializedName("id")
    var id: String = ""

    @SerializedName("type")
    var type: String = ""

    @SerializedName("link")
    var link: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        type = parcel.readString().toString()
        link = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(type)
        parcel.writeString(link)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchImage> {
        override fun createFromParcel(parcel: Parcel): SearchImage {
            return SearchImage(parcel)
        }

        override fun newArray(size: Int): Array<SearchImage?> {
            return arrayOfNulls(size)
        }
    }


}