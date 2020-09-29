package com.gapps.oneone.utils.extensions

import com.gapps.oneone.utils.time.FastDateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Long?.toStrDate(pattern: String = "yyyy-MM-dd hh:mm:ss", localeCode: String = "en"): String {
	val value = this ?: 0

	return FastDateFormat.getInstance(pattern, Locale(localeCode)).format(value)
}

fun Long.toUTC(): String {
	return Date(this).toUTC()
}


fun Date.toUTC(): String {
	val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)

	return df.format(this)
}