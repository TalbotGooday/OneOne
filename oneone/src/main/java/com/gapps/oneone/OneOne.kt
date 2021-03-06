package com.gapps.oneone

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.gapps.oneone.models.log.FileModel
import com.gapps.oneone.models.log.LogFileModel
import com.gapps.oneone.models.log.LogModel
import com.gapps.oneone.screens.menu.MenuActivity
import com.gapps.oneone.utils.*
import com.gapps.oneone.utils.extensions.getAppInfoMap
import com.gapps.oneone.utils.extensions.printOneOneLogFile


@SuppressLint("StaticFieldLeak")
object OneOne {
	const val DEFAULT_TAG = "ONE_ONE"

	private const val ONE_ONE_HANDLER_PACKAGE_NAME = "com.gapps.oneone"
	private const val DEFAULT_HANDLER_PACKAGE_NAME = "com.android.internal.os"

	private var context: Context? = null
	private val helper = OneOneHelper()

	var isWebLoggingEnabled = true
		private set

	var appInfo: Map<String, String> = emptyMap()
		private set

	internal fun init(context: Context) {
		this.context = context
		appInfo = context.getAppInfoMap()

		initCrashLogger()

		initLogFilesAndDirs(context)

		helper.checkCrashes(context)
	}

	private fun initCrashLogger() {
		val oldHandler = Thread.getDefaultUncaughtExceptionHandler()

		if (oldHandler != null && oldHandler.javaClass.name.startsWith(ONE_ONE_HANDLER_PACKAGE_NAME)) run {
			Log.e(DEFAULT_TAG, "OneOne was already installed, doing nothing!")
		} else {
			if (oldHandler != null && !oldHandler.javaClass.name.startsWith(DEFAULT_HANDLER_PACKAGE_NAME)) {
				Log.e(DEFAULT_TAG, "IMPORTANT WARNING! You already have an UncaughtExceptionHandler, are you sure this is correct? If you use a custom UncaughtExceptionHandler, you must initialize it AFTER OneOne! Installing anyway, but your original handler will not be called.")
			}

			Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
				throwable.printOneOneLogFile(isCrash = true)
				oldHandler?.uncaughtException(thread, throwable)
			}
		}
	}

	fun setWebLoggingEnabled(enabled: Boolean) {
		isWebLoggingEnabled = enabled
	}

	fun openLog(context: Context) {
		context.startActivity(MenuActivity.newInstance(context))
	}

	@JvmOverloads
	fun d(tag: String? = null, message: Any?) {
		log(DEBUG, tag, message)
	}

	@JvmOverloads
	fun w(tag: String? = null, message: Any?) {
		log(WARNING, tag, message)
	}

	@JvmOverloads
	fun v(tag: String? = null, message: Any?) {
		log(VERBOSE, tag, message)
	}

	@JvmOverloads
	fun e(tag: String? = null, message: Any?) {
		log(ERROR, tag, message)
	}

	@JvmOverloads
	fun i(tag: String? = null, message: Any?) {
		log(INFO, tag, message)
	}

	@JvmOverloads
	fun sendLog(email: String? = null) {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		sendEmail(ctx, email)
	}

	fun clearAllLogMessages() {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		clearLogMessages(ctx)
	}

	fun clearAllLogFiles() {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		clearLogFiles(ctx)
	}

	fun deleteLogFile(name: String) {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		removeLogFile(ctx, name)
	}

	@JvmOverloads
	fun getMessages(type: String? = null): Map<String, List<LogModel>?> {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		return getMessagesFromLog(ctx, type)
	}

	@JvmOverloads
	fun getLogsList(type: String? = null): List<LogFileModel> {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		return getFilesMap(ctx, type).map {
			LogFileModel().apply {
				this.type = it.key
				this.name = helper.getNameFromType(context, it.key)
				this.tag = it.key
			}
		}
	}

	fun getLogFilesList(): List<FileModel> {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		return getLogFilesList(ctx)
	}

	fun writeToLogFile(
			text: String,
			withAdditionalPhoneInfo: Boolean = false,
			isCrash: Boolean = false,
	) {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		writeTextToLogFile(ctx, text, withAdditionalPhoneInfo, isCrash)
	}

	private fun log(type: String, tag: String? = null, message: Any?) {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		message ?: return

		if (isWebLoggingEnabled) {
			helper.logWeb(type, tag, message)
		}

		addMessageToLog(ctx, type, message, tag ?: DEFAULT_TAG)
	}

	fun setLoggerBaseUrl(baseUrl: String?, force: Boolean = false) {
		val context = context ?: return

		baseUrl ?: return

		val sp = context.getSharedPreferences(ONE_ONE_SP_NAME, Context.MODE_PRIVATE)

		val savedUrl = if (force) {
			baseUrl
		} else {
			sp.getString(ONE_ONE_LOGGER_URL, baseUrl) ?: baseUrl
		}

		helper.setLoggerBaseUrl(savedUrl)

		sp.edit().putString(ONE_ONE_LOGGER_URL, savedUrl).apply()
	}
}