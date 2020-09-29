package com.gapps.oneone.utils.extensions

import java.text.SimpleDateFormat
import java.util.*

internal fun Long?.toStrDate(pattern: String = "yyyy-MM-dd hh:mm:ss", localeCode: String = "en"): String {
	val value = this ?: 0

	return SimpleDateFormat(pattern, Locale(localeCode)).format(value)
}

internal fun Long.toUTC(): String {
	return Date(this).toUTC()
}


internal fun Date.toUTC(): String {
	val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)

	return df.format(this)
}