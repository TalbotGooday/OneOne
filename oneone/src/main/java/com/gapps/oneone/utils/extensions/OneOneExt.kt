package com.gapps.oneone.utils.extensions

import com.gapps.oneone.OneOne
import java.io.PrintWriter
import java.io.StringWriter

fun Throwable.printOneOne(tag: String? = null) {
	val sw = StringWriter()
	val pw = PrintWriter(sw)
	this.printStackTrace(pw)

	OneOne.e(tag ?: OneOne.DEFAULT_TAG, sw.toString())
}