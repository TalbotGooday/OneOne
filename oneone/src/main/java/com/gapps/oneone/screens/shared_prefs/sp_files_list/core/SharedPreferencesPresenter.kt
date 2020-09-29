package com.gapps.oneone.screens.shared_prefs.sp_files_list.core

import android.content.Context
import com.gapps.oneone.screens.base.BaseAPresenter
import com.gapps.oneone.utils.getAllSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class SharedPreferencesPresenter : SharedPreferencesContract.Presenter, BaseAPresenter() {
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

	override fun getSharedPrefFiles() {
		launch {
			val data = withContext(Dispatchers.IO) {
				context.getAllSharedPreferences()
			}

			view.onGotSharedPreferencesFiles(data)
		}
	}
}
