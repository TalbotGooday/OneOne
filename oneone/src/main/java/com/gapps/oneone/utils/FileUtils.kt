package com.gapps.oneone.utils

import android.content.Context
import android.util.Base64
import com.gapps.oneone.models.log.FileModel
import com.gapps.oneone.models.log.LogModel
import com.gapps.oneone.models.shared_prefs.SharedPrefsFileModel
import com.gapps.oneone.utils.extensions.getAppInfoString
import com.gapps.oneone.utils.time.FastDateFormat
import com.google.gson.Gson
import java.io.File
import java.nio.charset.Charset
import java.util.*


const val FOLDER_ONE_ONE = "one_one"
const val FOLDER_LOG = "log"
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

fun initLogFilesAndDirs(context: Context) {
	val oneOneFolder = File(context.cacheDir, "$FOLDER_ONE_ONE/$FOLDER_LOG")

	if (oneOneFolder.exists().not()) {
		oneOneFolder.mkdirs()
	}
}

fun getLogFilePath(fileName: String) = "$FOLDER_ONE_ONE/$fileName"

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

		if (file.exists().not()) {
			file.createNewFile()
		}

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
			files[DEBUG] = File(context.cacheDir, getLogFilePath(FILE_LOG_D))
		}
		WARNING -> {
			files[WARNING] = File(context.cacheDir, getLogFilePath(FILE_LOG_W))
		}
		ERROR -> {
			files[ERROR] = File(context.cacheDir, getLogFilePath(FILE_LOG_E))
		}
		INFO -> {
			files[INFO] = File(context.cacheDir, getLogFilePath(FILE_LOG_I))
		}
		VERBOSE -> {
			files[VERBOSE] = File(context.cacheDir, getLogFilePath(FILE_LOG_V))
		}
	}
	return files
}

fun getLogFilesList(context: Context): List<FileModel> {
	val logDir = File(context.cacheDir, "$FOLDER_ONE_ONE/$FOLDER_LOG")
	if (logDir.exists().not() || logDir.isDirectory.not()) {
		return emptyList()
	}

	return logDir.list()?.mapNotNull {
		FileModel().apply {
			this.name = it
			this.path = "${logDir.path}/$it"
		}
	} ?: emptyList()
}

fun clearLogMessages(context: Context, type: String? = null) {
	getFilesMap(context, type).values.forEach {
		it.writeText("")
	}
}

fun clearLogFiles(context: Context) {
	val logDir = File(context.cacheDir, "$FOLDER_ONE_ONE/$FOLDER_LOG")
	val listFiles = logDir.listFiles()

	if (logDir.exists().not() || logDir.isDirectory.not() || listFiles.isNullOrEmpty()) {
		return
	}

	for (f: File in listFiles) {
		if (!f.isDirectory) {
			f.delete();
		}
	}

	logDir.delete();
}

fun removeLogFile(context: Context, name: String) {
	val logDir = File(context.cacheDir, "$FOLDER_ONE_ONE/$FOLDER_LOG")
	val logFile = File(logDir, name)
	if (logFile.exists()) {
		logFile.delete()
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

fun writeTextToLogFile(context: Context, text: String, withAdditionalPhoneInfo: Boolean = false) {
	val logDir = File(context.cacheDir, "$FOLDER_ONE_ONE/$FOLDER_LOG")

	if (logDir.exists().not()) {
		logDir.mkdir()
	}

	val fileName = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH).format(System.currentTimeMillis())
	val logFile = File(logDir, "$fileName.log")

	if (logFile.exists().not()) {
		logFile.createNewFile()
	}

	if (withAdditionalPhoneInfo) {
		val phoneInfo = context.getAppInfoString()

		logFile.writeText("$phoneInfo\n ---------------------- \n")
	}

	if (logFile.exists()) {
		logFile.appendText(text)
	} else {
		logFile.writeText(text)
	}
}