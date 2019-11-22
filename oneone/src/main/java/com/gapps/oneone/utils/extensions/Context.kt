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


inline fun Context.debug(code: () -> Unit) {
	if (BuildConfig.DEBUG) {
		code()
	}
}

@JvmOverloads
fun Context.toast(text: String, duration: Int = Toast.LENGTH_LONG) {
	Toast.makeText(this, text, duration).show()
}

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_LONG) {
	Toast.makeText(this, resId, duration).show()
}

fun Context.convertDpToPixel(dp: Int): Int {
	val metrics = resources.displayMetrics
	return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.intentSafe(intent: Intent): Boolean {
	val activities = packageManager
			.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
	return activities.isNotEmpty()
}

fun Context.color(@ColorRes colorRes: Int): Int {
	return ContextCompat.getColor(this, colorRes)
}

fun Context.quantity(@PluralsRes plural: Int, value: Int?): String {
	return this.resources.getQuantityString(plural, value ?: 0, value ?: 0)
}

fun Context.setSystemBarColor(@ColorRes color: Int) {
	val colorInt = ContextCompat.getColor(this, color)

	setSystemBarColorInt(colorInt)
}

fun Context.setSystemBarColorInt(colorInt: Int) {
	val activity = this as? Activity ?: if (this is Fragment) this.activity else null
	activity ?: return
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		val window = activity.window
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
		window.statusBarColor = colorInt
	}
}

fun Context.setSystemBarLight() {
	val activity = this as? AppCompatActivity ?: if (this is Fragment) this.activity else null
	activity ?: return

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		val view = activity.findViewById<View>(android.R.id.content)
		var flags = view.systemUiVisibility
		flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
		view.systemUiVisibility = flags
	}
}

fun Context.clearSystemBarLight() {
	val activity = this as? AppCompatActivity ?: if (this is Fragment) this.activity else null
	activity ?: return

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		val view = activity.findViewById<View>(android.R.id.content)
		var flags = view.systemUiVisibility
		flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
		view.systemUiVisibility = flags
	}
}

fun Context.hideKeyboard(textView: TextView? = null) {
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

fun Context.showKeyboard(textView: TextView? = null) {
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


fun View?.subscribeKeyboardEvent(action: (Boolean) -> Unit) {
	val fragmentView = this ?: return

	fragmentView.viewTreeObserver?.addOnGlobalLayoutListener {
		val r = Rect()
		fragmentView.getWindowVisibleDisplayFrame(r)
		val screenHeight = fragmentView.rootView.height
		val keypadHeight = screenHeight - r.bottom

		val keyBoardIsOpened = keypadHeight > screenHeight * 0.15
		action.invoke(keyBoardIsOpened)
	}
}


fun Context.copyToClipboard(text: String, action: (() -> Unit)? = null) {
	val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
	val clip = ClipData.newPlainText("HCMessageText", text)
	if (clipboard != null) {
		clipboard.setPrimaryClip(clip)
		action?.invoke()
	}
}

fun Context.openAppInMarket() {
	val i = Intent(Intent.ACTION_VIEW)
	i.data = Uri.parse("http://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
	startActivity(i.apply {
		addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
	}, null)
}

fun Context.openLink(link: String) {
	val i = Intent(Intent.ACTION_VIEW)
	i.data = Uri.parse(link)
	startActivity(i.apply {
		addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
	}, null)
}

fun Context.getClipboardText(): String {
	val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

	val primaryClip = clipboard.primaryClip

	val clipboardTmpText = if (primaryClip != null && primaryClip.itemCount > 0) {
		primaryClip.getItemAt(0)?.text?.toString()
	} else {
		null
	}

	return clipboardTmpText ?: "(▰˘◡˘▰)"
}

fun Context.getAppInfoMap(): Map<String, String> {
	val packageInfo: PackageInfo = this.packageManager.getPackageInfo(this.applicationContext.packageName, 0)
	val sVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
		packageInfo.longVersionCode.toString()
	} else {
		packageInfo.versionCode.toString()
	}

	val sVersionName = packageInfo.versionName

	val sPackName: String = this.applicationContext.packageName

	val nSdkVersion = Build.VERSION.SDK_INT.toString()

	return mapOf(
			VERSION_CODE to sVersionCode,
			VERSION_NAME to sVersionName,
			PACKAGE_NAME to sPackName,
			VERSION_ANDROID to "${Build.VERSION.RELEASE} (SDK: $nSdkVersion)",
			PHONE to "${Build.MANUFACTURER} ${Build.MODEL}"
	)
}

fun Context.getAppInfoMapLocalized(): Map<String, String> {
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

fun Context.getAppInfoString(): String {
	val builder = StringBuilder()

	getAppInfoMapLocalized().entries.forEach {
		builder.append("${it.key}: ${it.value}\n")
	}

	return builder.toString()
}