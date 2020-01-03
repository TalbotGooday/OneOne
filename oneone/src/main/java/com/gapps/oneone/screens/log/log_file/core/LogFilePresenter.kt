package com.gapps.oneone.screens.log.log_file.core

import android.content.Context
import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext

class LogFilePresenter : LogFileContract.Presenter, CoroutineScope {
	override lateinit var view: LogFileContract.View
	private lateinit var context: Context
	private val job = Job()

	override val coroutineContext: CoroutineContext
		get() = job + Dispatchers.Main

	override fun create(context: Context) {
		this.context = context

		if (context is LogFileContract.View) {
			this.view = context
		}
	}

	override fun openFile(path: String) {
		val file = File(path)
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

	override fun destroy() {
	}
}
