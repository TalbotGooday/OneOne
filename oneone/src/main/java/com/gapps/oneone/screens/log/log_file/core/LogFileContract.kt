package com.gapps.oneone.screens.log.log_file.core

import android.net.Uri
import com.gapps.oneone.screens.base.BasePresenter
import com.gapps.oneone.screens.base.BaseView

interface LogFileContract {
	interface View : BaseView<Presenter> {
		fun onFileLoadSuccess(text: String)
		fun onFileLoadError()
		fun onLogFileDeleted(name: String)
		fun onLogFilePreparedToSend(uri: Uri?)
	}

	interface Presenter : BasePresenter<View> {
		fun openFile(path: String)
		fun deleteLogFile(name: String)
		fun prepareLogFileToSend()
	}
}
