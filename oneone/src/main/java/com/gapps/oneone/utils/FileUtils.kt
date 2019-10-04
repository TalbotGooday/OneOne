package com.gapps.oneone.utils

import android.content.Context
import android.util.Base64
import com.gapps.oneone.models.LogModel
import com.google.gson.Gson
import java.io.File
import java.nio.charset.Charset

const val FILE_LOG = "one-one.log"
const val FILE_LOG_D = "one-one-d.log"
const val FILE_LOG_W = "one-one-w.log"
const val FILE_LOG_E = "one-one-e.log"
const val FILE_LOG_I = "one-one-i.log"

const val DEBUG = "OO_LOG_D"
const val WARNING = "OO_LOG_W"
const val ERROR = "OO_LOG_E"
const val INFO = "OO_LOG_I"

private const val DELIMITER = "&"

fun addMessageToLog(context: Context, type: String, message: Any, tag: String) {

	val fileName = when (type) {
		DEBUG -> FILE_LOG_D
		WARNING -> FILE_LOG_W
		ERROR -> FILE_LOG_E
		INFO -> FILE_LOG_I
		else -> FILE_LOG_D
	}

	val file = File(context.cacheDir, fileName)

	try {
		val toJson = if (message is String) {
			message
		} else {
			Gson().toJson(message)
		}

		val messageB64 = Base64.encodeToString(toJson.toByteArray(), Base64.NO_WRAP)

		val messageStr = "$tag$DELIMITER$messageB64\n"

		file.appendText(messageStr)
	} catch (e: Exception) {
		e.printStackTrace()
	}
}

fun getMessagesFromLog(context: Context, type: String? = null): Map<String, List<LogModel>?> {
	val files = getFilesMap(context, type)

	val result = mutableMapOf<String, List<LogModel>?>()

	files.keys.forEach { typeKey: String ->
		val file = files[typeKey]

		if (file != null) {
			val readLines = try {
				file.readLines()
			} catch (e: Exception) {
				e.printStackTrace()
				null
			}

			val data = readLines?.map { line -> createLogModel(typeKey, line.substringBefore(DELIMITER), line.substringAfter(DELIMITER)) }

			data?.run { result[typeKey] = this }
		}
	}

	return result
}

fun getFilesMap(context: Context, type: String? = null): MutableMap<String, File> {
	val files = mutableMapOf<String, File>()

	when (type) {
		null -> {
			files[DEBUG] = File(context.cacheDir, FILE_LOG_D)
			files[WARNING] = File(context.cacheDir, FILE_LOG_W)
			files[ERROR] = File(context.cacheDir, FILE_LOG_E)
			files[INFO] = File(context.cacheDir, FILE_LOG_I)
		}
		DEBUG -> {
			files[DEBUG] = File(context.cacheDir, FILE_LOG_D)
		}
		WARNING -> {
			files[WARNING] = File(context.cacheDir, FILE_LOG_W)
		}
		ERROR -> {
			files[ERROR] = File(context.cacheDir, FILE_LOG_E)
		}
	}
	return files
}

fun clear(context: Context) {
	getFilesMap(context).values.forEach {
		it.writeText("")
	}
}

fun createLogModel(type: String, tag: String, messageB64: String): LogModel {
	val messageBytes = Base64.decode(messageB64, Base64.NO_WRAP)

	return LogModel().apply {
		this.type = type
		this.tag = tag
		this.message = String(messageBytes, Charset.forName("UTF-8"))
	}
}