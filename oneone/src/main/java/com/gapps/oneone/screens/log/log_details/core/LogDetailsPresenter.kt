package com.gapps.oneone.screens.log.log_details.core

import android.content.Context

class LogDetailsPresenter : LogDetailsContract.Presenter {
	override lateinit var view: LogDetailsContract.View
	private lateinit var context: Context

	override fun create(context: Context) {
		this.context = context

		if (context is LogDetailsContract.View) {
			this.view = context
		}
	}

	override fun destroy() {
	}
}
