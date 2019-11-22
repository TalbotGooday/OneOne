package com.gapps.oneone.utils.extensions

import com.gapps.oneone.utils.time.FastDateFormat
import java.util.*

fun Long?.toStrDate(pattern: String = "yyyy-MM-dd hh:mm:ss", localeCode: String = "en"): String {
	val value = this ?: 0

	return FastDateFormat.getInstance(pattern, Locale(localeCode)).format(value)
}
