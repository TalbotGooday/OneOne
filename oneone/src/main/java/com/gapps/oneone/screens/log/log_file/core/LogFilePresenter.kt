package com.gapps.oneone.screens.log.log_file.core

import android.content.Context
import android.net.Uri
import com.gapps.oneone.OneOne
import com.gapps.oneone.screens.base.BaseAPresenter
import com.gapps.oneone.utils.copyFileToExternalCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class LogFilePresenter : LogFileContract.Presenter, BaseAPresenter() {
	override lateinit var view: LogFileContract.View
	private lateinit var context: Context

	private var logFile: File? = null

	override fun create(context: Context) {
		this.context = context

		if (context is LogFileContract.View) {
			this.view = context
		}
	}

	override fun openFile(path: String) {
		val file = File(path)
		this.logFile = file

		if (file.exists().not()) {
			view.onFileLoadError()
			return
		}

		launch(Dispatchers.Main) {
			val fileText = withContext(Dispatchers.IO) {
				file.readText()
			}

			view.onFileLoadSuccess(fileText)
		}
	}

	override fun deleteLogFile(name: String) {
		launch {
			withContext(Dispatchers.IO) {
				OneOne.deleteLogFile(name)
			}

			view.onLogFileDeleted(name)
		}
	}

	override fun prepareLogFileToSend() {
		launch {
			val uri = withContext(Dispatchers.IO){
				val file = logFile?.copyFileToExternalCache(context) ?: return@withContext null

				Uri.fromFile(file)
			}

			view.onLogFilePreparedToSend(uri)
		}
	}


	override fun destroy() {
	}
}
