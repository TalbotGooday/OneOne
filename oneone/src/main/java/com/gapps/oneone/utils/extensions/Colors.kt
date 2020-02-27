package com.gapps.oneone.utils.extensions

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.graphics.ColorUtils
import com.gapps.oneone.R
import com.gapps.oneone.utils.*


/** Google definition: alpha = 1.0f  */
const val TEXT_ALPHA_PRIMARY_LIGHT = (255 * 1.0f).toInt()
/** Google definition: alpha = 0.87f  */
const val TEXT_ALPHA_PRIMARY_DARK = (255 * 0.87f).toInt()
const val THRESHOLD_LIGHT = 0.54


@ColorInt
fun Int.getPrimaryTextColor(): Int {
	return getPrimaryTextColor(ColorUtils.calculateLuminance(this) > THRESHOLD_LIGHT)
}

@ColorInt
fun Int.getPrimaryIconColor(): Int {
	val isLight = isColorLight()

	val color = if (isLight) Color.BLACK else Color.WHITE
	val alpha = if (isLight) TEXT_ALPHA_PRIMARY_DARK else TEXT_ALPHA_PRIMARY_LIGHT
	return ColorUtils.setAlphaComponent(color, alpha)
}

fun Int.isColorLight() = ColorUtils.calculateLuminance(this) > THRESHOLD_LIGHT

private fun getPrimaryTextColor(brightBackground: Boolean): Int {
	val color = if (brightBackground) Color.BLACK else Color.WHITE
	val alpha = if (brightBackground) TEXT_ALPHA_PRIMARY_DARK else TEXT_ALPHA_PRIMARY_LIGHT
	return ColorUtils.setAlphaComponent(color, alpha)
}

@ColorInt
fun Drawable.getColor(): Int {
	var color = Color.TRANSPARENT
	if (this is ColorDrawable)
		color = this.color

	return color
}

fun View.backgroundTint(@ColorInt primaryTextColor: Int) {
	background.colorFilter = PorterDuffColorFilter(primaryTextColor, PorterDuff.Mode.SRC_IN)
}

fun Float.toColorAlpha(): Int{
	return (this * 255).toInt()
}

fun Int.toColorDrawable(@Px size: Int): ColorDrawable{
	return ColorDrawable(this).apply {
		setBounds(0, 0, size, size)
	}
}

fun getLogIndicatorByType(type: String): Int {
	return when (type) {
		DEBUG -> {
			R.color.colorOODebug
		}
		WARNING -> {
			R.color.colorOOWarnings
		}
		ERROR -> {
			R.color.colorOOErrors
		}
		INFO -> {
			R.color.colorOOInfo
		}
		VERBOSE -> {
			android.R.color.white
		}
		else -> {
			Color.BLACK
		}
	}
}