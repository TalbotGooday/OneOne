package com.gapps.oneone.screens.shared_prefs.sp_file.core

import com.gapps.oneone.screens.base.BasePresenter
import com.gapps.oneone.screens.base.BaseView

interface SharedPrefsFileContract {
	interface View : BaseView<Presenter> {

	}

	interface Presenter : BasePresenter<View> {

	}
}
