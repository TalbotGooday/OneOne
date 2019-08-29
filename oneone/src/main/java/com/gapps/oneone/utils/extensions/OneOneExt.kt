package com.gapps.oneone.utils.extensions

import com.gapps.oneone.OneOne
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception

fun Throwable.printOneOne(){
	val sw = StringWriter()
	val pw = PrintWriter(sw)
	this.printStackTrace(pw)

	OneOne.e(OneOne.DEFAULT_TAG, sw.toString())
}