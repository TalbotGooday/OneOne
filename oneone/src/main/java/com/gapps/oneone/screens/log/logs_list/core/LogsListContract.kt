package com.gapps.oneone.screens.log.logs_list.core

import com.gapps.oneone.models.log.LogFileModel
import com.gapps.oneone.screens.base.BasePresenter
import com.gapps.oneone.screens.base.BaseView

interface LogsListContract {
	interface View : BaseView<Presenter> {

	}

	interface Presenter : BasePresenter<View> {
		fun getLogsList(): List<LogFileModel>
	}
}
