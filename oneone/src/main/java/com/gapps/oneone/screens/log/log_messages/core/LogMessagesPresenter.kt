package com.gapps.oneone.screens.log.log_messages.core

import android.content.Context
import com.gapps.oneone.OneOne
import com.gapps.oneone.models.log.LogModel

class LogMessagesPresenter : LogMessagesContract.Presenter {
	override lateinit var view: LogMessagesContract.View
	private lateinit var context: Context

	override fun create(context: Context) {
		this.context = context

		if (context is LogMessagesContract.View) {
			this.view = context
		}
	}

	override fun getLogList(type: String): List<LogModel> {
		return OneOne.getMessages(type).values.firstOrNull() ?: emptyList()
	}

	override fun destroy() {
	}
}
