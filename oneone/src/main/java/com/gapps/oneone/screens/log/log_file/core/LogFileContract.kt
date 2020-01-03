package com.gapps.oneone.screens.log.log_file.core

import com.gapps.oneone.screens.base.BasePresenter
import com.gapps.oneone.screens.base.BaseView

interface LogFileContract {
	interface View : BaseView<Presenter> {
		fun onFileLoadSuccess(text: String)
		fun onFileLoadError()
	}

	interface Presenter : BasePresenter<View> {
		fun openFile(path: String)

	}
}
