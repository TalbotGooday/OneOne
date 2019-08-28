package com.gapps.oneone.utils

import android.content.Context
import android.util.Base64
import com.gapps.oneone.models.LogModel
import com.google.gson.Gson
import java.io.File
import java.nio.charset.Charset

const val FILE_LOG = "one-one.log"

const val DEBUG = "OO_LOG_D"
const val WARNING = "OO_LOG_W"
const val ERROR = "OO_LOG_E"

private const val DELIMITER = "&"

fun addMessageToLog(context: Context, type: String, message: Any, tag: String) {

	val file = File(context.cacheDir, FILE_LOG)

	try {
		val toJson = Gson().toJson(message)

		val messageB64 = Base64.encodeToString(toJson.toByteArray(), Base64.DEFAULT)

		val messageStr = "$type$DELIMITER($tag)$DELIMITER$messageB64"

		file.appendText(messageStr)
	} catch (e: Exception) {
		e.printStackTrace()
	}
}

fun getMessagesFromLog(context: Context): Map<String, List<LogModel>>? {
	val file = File(context.cacheDir, FILE_LOG)

	val readLines = try {
		file.readLines()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	} ?: return null

	val debug = mutableListOf<LogModel>()
	val warnings = mutableListOf<LogModel>()
	val errors = mutableListOf<LogModel>()

	readLines.forEach {
		when (it.substringBefore(DELIMITER)) {
			DEBUG -> {
				debug.add(createLogModel(it.substringBefore(DELIMITER), it.substringAfter(DELIMITER)))
			}
			WARNING -> {
				warnings.add(createLogModel(it.substringBefore(DELIMITER), it.substringAfter(DELIMITER)))
			}
			ERROR -> {
				errors.add(createLogModel(it.substringBefore(DELIMITER), it.substringAfter(DELIMITER)))
			}
		}
	}

	return mapOf(
			DEBUG to debug,
			WARNING to warnings,
			ERROR to errors
	)
}

fun createLogModel(type: String, messageWithType: String): LogModel {
	val tag = messageWithType.substringBefore(DELIMITER)
	val messageBytes = Base64.decode(messageWithType.substringAfter(DELIMITER), Base64.DEFAULT)

	return LogModel().apply {
		this.type = type
		this.tag = tag
		this.message = String(messageBytes, Charset.forName("UTF-8"))
	}
}