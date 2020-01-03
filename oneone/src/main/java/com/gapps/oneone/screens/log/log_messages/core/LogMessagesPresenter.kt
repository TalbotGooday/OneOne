package com.gapps.oneone.screens.log.log_messages.core

import android.content.Context
import com.gapps.oneone.OneOne
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LogMessagesPresenter : LogMessagesContract.Presenter, CoroutineScope {
	override lateinit var view: LogMessagesContract.View
	private lateinit var context: Context
	private val job = Job()

	override val coroutineContext: CoroutineContext
		get() = job + Dispatchers.Main

	override fun create(context: Context) {
		this.context = context

		if (context is LogMessagesContract.View) {
			this.view = context
		}
	}

	override fun getLogList(type: String) {
		launch {
			val messages = withContext(Dispatchers.IO) {
				OneOne.getMessages(type).values.firstOrNull() ?: emptyList()
			}

			view.onGotMessages(messages)
		}
	}

	override fun destroy() {
	}
}
