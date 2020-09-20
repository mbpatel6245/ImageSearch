package com.mbpatel.imagesearch.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SearchItemData() : Parcelable {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("account_id")
    var accountId: Long = 0

    @SerializedName("title")
    var title: String? = null

    @SerializedName("images")
    var searchImages: List<SearchImage>? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        accountId = parcel.readLong()
        title = parcel.readString()
        searchImages = parcel.createTypedArrayList(SearchImage)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeLong(accountId)
        parcel.writeString(title)
        parcel.writeTypedList(searchImages)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchItemData> {
        override fun createFromParcel(parcel: Parcel): SearchItemData {
            return SearchItemData(parcel)
        }

        override fun newArray(size: Int): Array<SearchItemData?> {
            return arrayOfNulls(size)
        }
    }


}