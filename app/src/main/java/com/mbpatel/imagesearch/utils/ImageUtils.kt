package com.mbpatel.imagesearch.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.util.Log
import android.widget.ImageView
import com.mbpatel.imagesearch.R
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.RequestTransformer
import com.squareup.picasso.RequestCreator

/**
 * Create Singleton picasso custom object
 * @param mContext context
 */
fun getCustomPicasso(mContext: Context): Picasso {
    val builder = Picasso.Builder(mContext)
    //set 12% of available app memory for image cache
    builder.memoryCache(LruCache(getBytesForMemCache(12, mContext)))
    //set request transformer
    val requestTransformer =
        RequestTransformer { request ->
            Log.d("image request", request.toString())
            request
        }
    builder.requestTransformer(requestTransformer)
    return builder.build()
}

/**
 * Returns the given percentage of available memory in bytes
 * @param percentage occupy percentage memory
 * @param mContext context
 */
private fun getBytesForMemCache(percentage: Int, mContext: Context): Int {
    val mi = ActivityManager.MemoryInfo()
    val activityManager = mContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager?
    activityManager!!.getMemoryInfo(mi)
    val availableMemory = mi.availMem.toDouble()
    return (percentage * availableMemory / 100).toInt()
}

/**
 * Load the image using picasso
 * @param imageURL set image url
 * @param image view of image
 * @param size square image size
 */
fun loadImage(imageURL: String, image: ImageView, size: Int) {
    //print picasso snap stats
    val ss = Picasso.get().snapshot
    Log.d("download image stats", "" + ss.cacheHits)
    Log.d("download image stats", "" + ss.cacheMisses)
    Log.d("download image stats", "" + ss.downloadCount)
    Log.d("download image stats", "" + ss.totalDownloadSize)

    //clear cache and cancel pending requests
    Picasso.get().invalidate(imageURL)
    Picasso.get().cancelRequest(image)

    //set image placeholder image
    var rc: RequestCreator = Picasso.get().load(imageURL)
    rc = rc.placeholder(R.color.gray)

    //set error image, memory policy
    rc = rc.error(R.color.gray)

    //resize and crop
    if (size > 0)
        rc = rc.resize(size, size).centerCrop()
    //load image to imageview
    rc.into(image)
}

/**
 * Use for error image loading
 * @param image image view for the load image
 */
fun loadErrorImage(image: ImageView) {
    //set image placeholder image
    var rc: RequestCreator = Picasso.get().load(R.color.gray)
    rc = rc.placeholder(R.color.gray)

    //set error image, memory policy
    rc = rc.error(R.color.gray)

    //load image to imageview
    rc.into(image)
}