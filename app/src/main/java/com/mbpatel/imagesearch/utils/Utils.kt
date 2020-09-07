package com.mbpatel.imagesearch.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope

/**
 * convert dp value to pixel
 */
fun dip2px(context: Context, dpValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun isValidWebURL(url: String): Boolean {
    return !TextUtils.isEmpty(url) && Patterns.WEB_URL.matcher(url).matches();
}

fun getAspectWidth(oldW: Int, oldH: Int, newH: Int): Int {
    return (newH * calculateAspectRatio(oldW, oldH)).toInt()
}

fun getAspectHeight(oldW: Int, oldH: Int, newW: Int): Int {
    return (newW / calculateAspectRatio(oldW, oldH)).toInt()
}

fun calculateAspectRatio(oldW: Int, oldH: Int): Float {
    return if (oldW == 0 || oldH == 0) 0f else (oldW / oldH).toFloat()
}

fun showToast(context: Context, message: String) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()
}

fun showSnackBar(rootView: View, message: String) {
    Snackbar.make(
        rootView,
        message,
        Snackbar.LENGTH_LONG
    ).show()
}

/**
 * To check internet connection available or not
 * @param context
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
 * Configure CoroutineScope injection for production and testing.
 *
 * @receiver ViewModel provides viewModelScope for production
 * @param coroutineScope null for production, injects TestCoroutineScope for unit tests
 * @return CoroutineScope to launch coroutines on
 */
fun ViewModel.getViewModelScope(coroutineScope: CoroutineScope?) =
    coroutineScope ?: this.viewModelScope