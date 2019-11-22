package com.gapps.oneone.screens.log.logs_list.core

import android.content.Context
import com.gapps.oneone.OneOne
import com.gapps.oneone.models.log.LogFileModel

class LogsListPresenter : LogsListContract.Presenter {
	override lateinit var view: LogsListContract.View
	private lateinit var context: Context

	override fun create(context: Context) {
		this.context = context

		if (context is LogsListContract.View) {
			this.view = context
		}
	}

	override fun getLogsList(): List<LogFileModel> {
		return OneOne.getLogsList()
	}

	override fun destroy() {
	}
}
