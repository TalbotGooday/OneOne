package com.gapps.oneone.screens.menu.core

import com.gapps.oneone.screens.base.BasePresenter
import com.gapps.oneone.screens.base.BaseView

internal interface MenuContract {
	interface View : BaseView<Presenter> {

	}

	interface Presenter : BasePresenter<View> {
		fun saveNewLoggerUrl(newUrl: String)

	}
}
