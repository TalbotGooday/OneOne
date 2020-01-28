package com.gapps.oneone.screens.log.log_messages.core

import android.content.Context
import com.gapps.oneone.OneOne
import com.gapps.oneone.screens.base.BaseAPresenter
import com.gapps.oneone.utils.clearLogMessages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogMessagesPresenter : LogMessagesContract.Presenter, BaseAPresenter() {
	override lateinit var view: LogMessagesContract.View
	private lateinit var context: Context

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

	override fun clearLog(type: String) {
		launch {
			withContext(Dispatchers.IO) {
				clearLogMessages(context, type)
			}

			view.onLogCleared()
		}
	}

	override fun destroy() {
	}
}
