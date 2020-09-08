package com.mbpatel.imagesearch.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.mbpatel.imagesearch.R
import kotlinx.coroutines.CoroutineScope
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * convert dp value to pixel
 * @param context provide the context
 * @param dpValue provide dp
 */
fun dip2px(context: Context, dpValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * Check is valid web url
 * @param url string url
 */
fun isValidWebURL(url: String): Boolean {
    return !TextUtils.isEmpty(url) && Patterns.WEB_URL.matcher(url).matches();
}

/**
 * Calculate the aspect Width
 * @param oldW set Old Width of view
 * @param oldH set Old Height of view
 * @param newH set New Height of the view
 */
fun getAspectWidth(oldW: Int, oldH: Int, newH: Int): Int {
    return (newH * calculateAspectRatio(oldW, oldH)).toInt()
}

/**
 * Calculate the aspect height
 * @param oldW set Old Width of view
 * @param oldH set Old Height of view
 * @param newW set New Width of the view
 */
fun getAspectHeight(oldW: Int, oldH: Int, newW: Int): Int {
    return (newW / calculateAspectRatio(oldW, oldH)).toInt()
}

/**
 * Calculate the aspect ratio
 * @param oldW set Old Width of view
 * @param oldH set Old Height of view
 */
fun calculateAspectRatio(oldW: Int, oldH: Int): Float {
    return if (oldW == 0 || oldH == 0) 0f else (oldW / oldH).toFloat()
}

/**
 * Display the Toast
 * @param context context
 * @param message string message
 */
fun showToast(context: Context, message: String) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()
}

/**
 * Display the snackbar
 * @param rootView root of view
 * @param message string message
 */
fun showSnackBar(rootView: View, message: String) {
    Snackbar.make(
        rootView,
        message,
        Snackbar.LENGTH_LONG
    ).show()
}

/**
 * To check internet connection available or not
 * @param mContext context of the activity
 */
fun isInternetAvailable(mContext: Context): Boolean {
    try {
        val cm =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // test for connection
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            postAndroidMInternetCheck(cm)
        } else {
            preAndroidMInternetCheck(cm)
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * To check internet connection for pre android version 23
 * @param cm
 */
fun preAndroidMInternetCheck(cm: ConnectivityManager): Boolean {
    val activeNetwork = cm.activeNetworkInfo
    return if (activeNetwork != null) {
        activeNetwork.type == ConnectivityManager.TYPE_WIFI || activeNetwork.type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
    } else false
}

/**
 * To check internet connection for post android version 23
 * @param cm
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
fun postAndroidMInternetCheck(cm: ConnectivityManager): Boolean {
    var activeNetwork: Network? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        activeNetwork = cm.activeNetwork
    }
    val connection = cm.getNetworkCapabilities(activeNetwork)
    return connection != null && (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || connection.hasTransport(
        NetworkCapabilities.TRANSPORT_CELLULAR
    ))
}

/**
 * Show error during the load state
 * @param mContext provide context
 * @param it provide load state error
 */
fun setError(mContext: Context, it: LoadState.Error) {
    when (it.error) {
        is UnknownHostException -> showToast(
            mContext,
            mContext.getString(R.string.error_internet_connectivity)
        )
        is SocketTimeoutException -> showToast(
            mContext,
            mContext.getString(R.string.error_internet_connectivity)
        )
        is NoMoreDataException -> showToast(mContext, it.error.message.toString())
        is NoAnyDataException -> showToast(mContext, it.error.message.toString())
        else -> showToast(mContext, it.error.message.toString())
    }
}

/**
 * Hide keyboard automatically once user done with input field
 * @param view current focused view
 */
fun Context.hideKeyboard(view: View) {
    try {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}