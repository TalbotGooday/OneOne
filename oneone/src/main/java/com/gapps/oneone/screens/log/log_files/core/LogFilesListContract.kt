package com.gapps.oneone.screens.log.log_files.core

import com.gapps.oneone.models.log.FileModel
import com.gapps.oneone.models.log.LogFileModel
import com.gapps.oneone.screens.base.BasePresenter
import com.gapps.oneone.screens.base.BaseView

interface LogFilesListContract {
	interface View : BaseView<Presenter> {

	}

	interface Presenter : BasePresenter<View> {
		fun getLogFilesList(): List<FileModel>
	}
}
