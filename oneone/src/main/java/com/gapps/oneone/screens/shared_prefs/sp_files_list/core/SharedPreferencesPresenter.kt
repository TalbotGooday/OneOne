package com.gapps.oneone.screens.shared_prefs.sp_files_list.core

import android.content.Context
import com.gapps.oneone.models.shared_prefs.SharedPrefsFileModel
import com.gapps.oneone.utils.getAllSharedPreferences

class SharedPreferencesPresenter : SharedPreferencesContract.Presenter {
	override lateinit var view: SharedPreferencesContract.View
	private lateinit var context: Context

	override fun create(context: Context) {
		this.context = context

		if (context is SharedPreferencesContract.View) {
			this.view = context
		}
	}

	override fun destroy() {
	}

	fun getSharedPrefFiles(): List<SharedPrefsFileModel> {
		return context.getAllSharedPreferences()
	}
}
