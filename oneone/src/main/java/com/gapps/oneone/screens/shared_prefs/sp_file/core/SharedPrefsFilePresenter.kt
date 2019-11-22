package com.gapps.oneone.screens.shared_prefs.sp_file.core

import android.content.Context
import com.gapps.oneone.models.shared_prefs.SharedPrefEntry
import com.gapps.oneone.screens.shared_prefs.sp_file.core.SharedPrefsFileContract

class SharedPrefsFilePresenter : SharedPrefsFileContract.Presenter {
	override lateinit var view: SharedPrefsFileContract.View
	private lateinit var context: Context

	override fun create(context: Context) {
		this.context = context

		if (context is SharedPrefsFileContract.View) {
			this.view = context
		}
	}

	override fun destroy() {
	}

	fun getSharedPrefEntries(fileName: String): List<SharedPrefEntry> {
		val sp = context.getSharedPreferences(fileName.substringBeforeLast("."), Context.MODE_PRIVATE)

		return sp.all.map {
			SharedPrefEntry().apply {
				this.key = it.key
				this.value = it.value.toString()
				this.type = it.value?.javaClass?.name?.substringAfterLast(".") ?: ""
			}
		}
	}
}
