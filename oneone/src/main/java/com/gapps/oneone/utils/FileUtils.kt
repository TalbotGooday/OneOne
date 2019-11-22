package com.gapps.oneone.utils

import android.content.Context
import android.util.Base64
import com.gapps.oneone.models.log.LogModel
import com.gapps.oneone.models.shared_prefs.SharedPrefsFileModel
import com.google.gson.Gson
import java.io.File
import java.nio.charset.Charset


const val FOLDER_LOG = "one_one"
const val FILE_LOG = "one-one.log"
const val FILE_LOG_D = "one-one-d.log"
const val FILE_LOG_W = "one-one-w.log"
const val FILE_LOG_E = "one-one-e.log"
const val FILE_LOG_I = "one-one-i.log"
const val FILE_LOG_V = "one-one-v.log"

const val DEBUG = "OO_LOG_D"
const val WARNING = "OO_LOG_W"
const val ERROR = "OO_LOG_E"
const val INFO = "OO_LOG_I"
const val VERBOSE = "OO_LOG_V"

fun getLogFilePath(fileName: String) = "$FOLDER_LOG/$fileName"

fun addMessageToLog(context: Context, type: String, message: Any, tag: String) {

	val fileName = when (type) {
		DEBUG -> FILE_LOG_D
		WARNING -> FILE_LOG_W
		ERROR -> FILE_LOG_E
		INFO -> FILE_LOG_I
		VERBOSE -> FILE_LOG_V
		else -> FILE_LOG_D
	}

	val file = File(context.cacheDir, getLogFilePath(fileName))

	try {
		val gson = Gson()

		val toJson = if (message is String) {
			message
		} else {
			gson.toJson(message)
		}

		val logModel = LogModel().apply {
			this.tag = tag
			this.message = toJson
			this.time = System.currentTimeMillis()
		}

		val messageB64 = Base64.encodeToString(gson.toJson(logModel).toByteArray(), Base64.NO_WRAP)

		file.appendText(messageB64)
		file.appendText("\n")
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

			val data = readLines?.mapNotNull { line -> createLogModel(typeKey, line) }

			data?.run { result[typeKey] = this }
		}
	}

	return result
}

fun getFilesMap(context: Context, type: String? = null): MutableMap<String, File> {
	val files = mutableMapOf<String, File>()

	when (type) {
		null -> {
			files[DEBUG] = File(context.cacheDir, getLogFilePath(FILE_LOG_D))
			files[WARNING] = File(context.cacheDir, getLogFilePath(FILE_LOG_W))
			files[ERROR] = File(context.cacheDir, getLogFilePath(FILE_LOG_E))
			files[INFO] = File(context.cacheDir, getLogFilePath(FILE_LOG_I))
			files[VERBOSE] = File(context.cacheDir, getLogFilePath(FILE_LOG_V))
		}
		DEBUG -> {
			files[DEBUG] = File(context.cacheDir,  getLogFilePath(FILE_LOG_D))
		}
		WARNING -> {
			files[WARNING] = File(context.cacheDir,  getLogFilePath(FILE_LOG_W))
		}
		ERROR -> {
			files[ERROR] = File(context.cacheDir,  getLogFilePath(FILE_LOG_E))
		}
		INFO -> {
			files[INFO] = File(context.cacheDir,  getLogFilePath(FILE_LOG_I))
		}
		VERBOSE -> {
			files[VERBOSE] = File(context.cacheDir,  getLogFilePath(FILE_LOG_V))
		}
	}
	return files
}

fun clear(context: Context) {
	getFilesMap(context).values.forEach {
		it.writeText("")
	}
}

fun createLogModel(type: String, messageB64: String): LogModel? {
	val messageBytes = Base64.decode(messageB64, Base64.NO_WRAP)

	return try {
		Gson().fromJson<LogModel>(String(messageBytes, Charset.forName("UTF-8")), LogModel::class.java).apply {
			this.type = type
		}
	} catch (e: Exception) {
		null
	}
}

fun Context.getAllSharedPreferences(): List<SharedPrefsFileModel> {
	val prefsdir = File(applicationInfo.dataDir, "shared_prefs")

	return if (prefsdir.exists() && prefsdir.isDirectory) {
		prefsdir.list()?.map {
			SharedPrefsFileModel().apply {
				this.name = it
			}
		} ?: emptyList()
	} else {
		emptyList()
	}
}