package com.gapps.oneone

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.gapps.oneone.models.LogModel
import com.gapps.oneone.utils.*
import com.gapps.oneone.utils.extensions.printOneOne


@SuppressLint("StaticFieldLeak")
object OneOne {
	const val DEFAULT_TAG = "ONE_ONE"

	private const val ONE_ONE_HANDLER_PACKAGE_NAME = "com.gapps.oneone"
	private const val DEFAULT_HANDLER_PACKAGE_NAME = "com.android.internal.os"

	private var context: Context? = null

	fun init(context: Context) {
		this.context = context

		initCrashLogger()
	}

	private fun initCrashLogger() {
		val oldHandler = Thread.getDefaultUncaughtExceptionHandler()

		if (oldHandler != null && oldHandler.javaClass.name.startsWith(ONE_ONE_HANDLER_PACKAGE_NAME)) run {
			Log.e(DEFAULT_TAG, "CustomActivityOnCrash was already installed, doing nothing!")
		} else {
			if (oldHandler != null && !oldHandler.javaClass.name.startsWith(DEFAULT_HANDLER_PACKAGE_NAME)) {
				Log.e(DEFAULT_TAG, "IMPORTANT WARNING! You already have an UncaughtExceptionHandler, are you sure this is correct? If you use a custom UncaughtExceptionHandler, you must initialize it AFTER CustomActivityOnCrash! Installing anyway, but your original handler will not be called.")
			}

			Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
				throwable.printOneOne()
				oldHandler?.uncaughtException(thread, throwable)
			}
		}
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
	fun e(tag: String? = null, message: Any?) {
		log(ERROR, tag, message)
	}

	fun sendLog(email: String) {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		sendEmail(ctx, email)
	}

	fun getMessages(): Map<String, List<LogModel>?> {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		return getMessagesFromLog(ctx)
	}

	private fun log(type: String, tag: String? = null, message: Any?) {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		message ?: return

		addMessageToLog(ctx, type, message, tag ?: DEFAULT_TAG)
	}

}