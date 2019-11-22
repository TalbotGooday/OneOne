package com.gapps.oneone

import android.content.Context
import com.gapps.oneone.utils.*

class OneOneHelper {
	fun getNameFromType(context: Context?, fileName: String): String{
		context?: return ""

		return when(fileName){
			DEBUG -> context.getString(R.string.debug)
			WARNING -> context.getString(R.string.warning)
			ERROR -> context.getString(R.string.error)
			INFO -> context.getString(R.string.info)
			VERBOSE -> context.getString(R.string.verbose)
			else -> context.getString(R.string.log)
		}
	}
}
