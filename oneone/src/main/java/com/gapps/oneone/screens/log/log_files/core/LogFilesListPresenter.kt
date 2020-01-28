package com.gapps.oneone.screens.log.log_files.core

import android.content.Context
import com.gapps.oneone.OneOne
import com.gapps.oneone.screens.base.BaseAPresenter
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LogFilesListPresenter : LogFilesListContract.Presenter, BaseAPresenter() {
	override lateinit var view: LogFilesListContract.View
	private lateinit var context: Context

	override fun create(context: Context) {
		this.context = context

		if (context is LogFilesListContract.View) {
			this.view = context
		}
	}

	override fun getLogFilesList() {
		view.onGotLogFilesList(OneOne.getLogFilesList())
	}

	override fun clearAllLogFiles() {
		launch {
			withContext(Dispatchers.IO) {
				OneOne.clearAllLogFiles()
			}

			view.onLogFilesDeleted()
		}
	}

	override fun destroy() {
	}
}
