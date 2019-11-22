package com.gapps.oneone.screens.log.log_messages.core

import com.gapps.oneone.models.log.LogModel
import com.gapps.oneone.screens.base.BasePresenter
import com.gapps.oneone.screens.base.BaseView

interface LogMessagesContract {
	interface View : BaseView<Presenter> {

	}

	interface Presenter : BasePresenter<View> {
		fun getLogList(type: String): List<LogModel>
	}
}
