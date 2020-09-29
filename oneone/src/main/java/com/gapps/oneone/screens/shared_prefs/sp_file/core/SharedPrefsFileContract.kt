package com.gapps.oneone.screens.shared_prefs.sp_file.core

import com.gapps.oneone.screens.base.BasePresenter
import com.gapps.oneone.screens.base.BaseView

internal interface SharedPrefsFileContract {
	interface View : BaseView<Presenter> {
		fun onSpEntryUpdated(key: String, value: String?)
	}

	interface Presenter : BasePresenter<View> {
		fun changeSpEntry(type: String, key: String?, value: String?)

	}
}
