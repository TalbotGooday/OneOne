package com.gapps.oneone.screens.log.log_files.core

import android.content.Context
import com.gapps.oneone.OneOne
import com.gapps.oneone.models.log.FileModel
import com.gapps.oneone.models.log.LogFileModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class LogFilesListPresenter : LogFilesListContract.Presenter, CoroutineScope {
	override lateinit var view: LogFilesListContract.View
	private lateinit var context: Context
	private val job = Job()

	override val coroutineContext: CoroutineContext
		get() = job + Dispatchers.Main

	override fun create(context: Context) {
		this.context = context

		if (context is LogFilesListContract.View) {
			this.view = context
		}
	}

	override fun getLogFilesList(): List<FileModel> {
		return OneOne.getLogFilesList()
	}

	override fun destroy() {
	}
}
