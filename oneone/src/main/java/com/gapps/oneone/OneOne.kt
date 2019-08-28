package com.gapps.oneone

import android.annotation.SuppressLint
import android.content.Context
import com.gapps.oneone.models.LogModel
import com.gapps.oneone.utils.*
import com.gapps.oneone.utils.addMessageToLog
import com.gapps.oneone.utils.sendEmail


@SuppressLint("StaticFieldLeak")
object OneOne {
	const val DEFAULT_TAG = "ONE_ONE"

	private var context: Context? = null

	fun init(context: Context) {
		this.context = context
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

		sendEmail(ctx, email, FILE_LOG)
	}

	fun getMessages(): Map<String, List<LogModel>>?{
		val ctx = context ?: throw IllegalStateException("Call init() first")

		return getMessagesFromLog(ctx)
	}

	private fun log(type: String, tag: String? = null, message: Any?) {
		val ctx = context ?: throw IllegalStateException("Call init() first")

		message ?: return

		addMessageToLog(ctx, type, message, tag ?: DEFAULT_TAG)
	}

}