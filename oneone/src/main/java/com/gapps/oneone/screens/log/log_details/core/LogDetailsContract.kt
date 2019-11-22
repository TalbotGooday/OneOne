package com.gapps.oneone.screens.log.log_details.core

import com.gapps.oneone.screens.base.BasePresenter
import com.gapps.oneone.screens.base.BaseView

interface LogDetailsContract {
	interface View : BaseView<Presenter> {

	}

	interface Presenter : BasePresenter<View> {

	}
}
