package com.gapps.oneone.utils.extensions

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gapps.oneone.BuildConfig
import com.gapps.oneone.R
import java.lang.StringBuilder


internal inline fun Context.debug(code: () -> Unit) {
	if (BuildConfig.DEBUG) {
		code()
	}
}

internal fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_LONG) {
	Toast.makeText(this, resId, duration).show()
}

internal fun Context.color(@ColorRes colorRes: Int): Int {
	return ContextCompat.getColor(this, colorRes)
}

internal fun Context.setSystemBarColorInt(colorInt: Int) {
	val activity = this as? Activity ?: if (this is Fragment) this.activity else null
	activity ?: return
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		val window = activity.window
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
		window.statusBarColor = colorInt
	}
}

internal fun Context.hideKeyboard(textView: TextView? = null) {
	val view = if (textView == null) {
		val activity = this as? Activity ?: if (this is Fragment) this.activity else null
		activity ?: return
		activity.currentFocus
	} else {
		textView
	}

	if (view != null) {
		(view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
	}
}

internal fun Context.showKeyboard(textView: TextView? = null) {
	val view = if (textView == null) {
		val activity = this as? Activity ?: if (this is Fragment) this.activity else null
		activity ?: return
		activity.currentFocus
	} else {
		textView
	}

	if (view != null) {
		(view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN)
	}
}

internal fun Context.copyToClipboard(text: String, action: (() -> Unit)? = null) {
	val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
	val clip = ClipData.newPlainText("HCMessageText", text)
	if (clipboard != null) {
		clipboard.setPrimaryClip(clip)
		action?.invoke()
	}
}

internal fun Context.getAppInfoMap(): Map<String, String> {
	val packageInfo: PackageInfo = this.packageManager.getPackageInfo(this.applicationContext.packageName, 0)
	val sVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
		packageInfo.longVersionCode.toString()
	} else {
		packageInfo.versionCode.toString()
	}

	val sVersionName = packageInfo.versionName

	val sPackName: String = this.applicationContext.packageName


	return mapOf(
			VERSION_CODE to sVersionCode,
			VERSION_NAME to sVersionName,
			PACKAGE_NAME to sPackName,
			VERSION_ANDROID to "${Build.VERSION.RELEASE} (SDK: ${Build.VERSION.SDK_INT})",
			PHONE to "${Build.MANUFACTURER} ${Build.MODEL}"
	)
}

internal fun Context.getAppInfoMapLocalized(): Map<String, String> {
	fun getTitleByInfoKey(key: String): String {
		return when (key) {
			VERSION_CODE -> getString(R.string.version_code)
			VERSION_NAME -> getString(R.string.version_name)
			PACKAGE_NAME -> getString(R.string.package_name)
			VERSION_ANDROID -> getString(R.string.version_android)
			PHONE -> getString(R.string.phone)

			else -> ""
		}
	}

	return this.getAppInfoMap().entries.map {
		getTitleByInfoKey(it.key) to it.value
	}.toMap()
}

internal fun Context.getAppInfoString(): String {
	val builder = StringBuilder()

	getAppInfoMapLocalized().entries.forEach {
		builder.append("${it.key}: ${it.value}\n")
	}

	return builder.toString()
}