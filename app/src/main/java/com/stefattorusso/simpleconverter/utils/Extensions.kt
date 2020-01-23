package com.stefattorusso.simpleconverter.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mynameismidori.currencypicker.ExtendedCurrency
import com.stefattorusso.domain.RateDomain
import com.stefattorusso.simpleconverter.model.RateModel
import java.io.IOException
import java.io.InputStream
import java.math.BigDecimal
import java.util.*


// Mapper

fun RateDomain.toModel(): RateModel {
    return RateModel(
        this.code,
        Currency.getInstance(this.code).getDisplayName(Locale.US),
        this.value.toStringValue(),
        this.base,
        ExtendedCurrency.getCurrencyByISO(this.code).flag
    )
}

// Fragment

fun <TFragment : Fragment> TFragment.newInstance(bundle: Bundle?): TFragment {
    return this.apply { arguments = bundle ?: Bundle() }
}

// Activity

fun <TFragment : Fragment> AppCompatActivity.loadFragment(
    containerId: Int,
    fragment: TFragment
) {
    supportFragmentManager
        .beginTransaction()
        .replace(containerId, fragment, fragment.javaClass.simpleName)
        .commit()
}

fun AppCompatActivity.getFragment(containerId: Int) =
    supportFragmentManager.findFragmentById(containerId)


// Context

fun Activity.hideSoftKeyboard() {
    val v = currentFocus
    v?.let {
        val imm: InputMethodManager? =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

fun Context.getStringFromAssets(name: String): String {
    var value = ""
    var inputStream: InputStream? = null
    try {
        inputStream = assets.open(name)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        value = String(buffer, Charsets.UTF_8)
    } catch (e: IOException) {
        Log.e("TAG", e.localizedMessage)
    } finally {
        inputStream?.close()
    }
    return value
}


// View

fun ImageView.loadUrl(url: String?) {
    if (url == null) return
    Glide.with(context)
        .load(url)
        .into(this)
}

fun ImageView.loadResource(id: Int) {
    if (id < 0) return
    Glide.with(context)
        .load(id)
        .circleCrop()
        .into(this)
}


fun BigDecimal.toStringValue(): String {
    val result = this.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
    return result.toPlainString()
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int): List<T> {
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
    return this.toList()
}