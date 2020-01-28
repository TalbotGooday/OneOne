package com.gapps.oneone.screens.shared_prefs.sp_files_list.core

import com.gapps.oneone.models.shared_prefs.SharedPrefsFileModel
import com.gapps.oneone.screens.base.BasePresenter
import com.gapps.oneone.screens.base.BaseView

interface SharedPreferencesContract {
	interface View : BaseView<Presenter> {
		fun onGotSharedPreferencesFiles(data: List<SharedPrefsFileModel>)
	}

	interface Presenter : BasePresenter<View> {
		fun getSharedPrefFiles()
	}
}
