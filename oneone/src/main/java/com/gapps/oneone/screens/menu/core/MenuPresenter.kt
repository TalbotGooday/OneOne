package com.gapps.oneone.screens.menu.core

import android.content.Context
import com.gapps.oneone.OneOne

internal class MenuPresenter : MenuContract.Presenter {
	override lateinit var view: MenuContract.View
	private lateinit var context: Context

	override fun create(context: Context) {
		this.context = context

		if (context is MenuContract.View) {
			this.view = context
		}
	}

	override fun saveNewLoggerUrl(newUrl: String) {
		OneOne.setLoggerBaseUrl(newUrl, true)
	}

	override fun destroy() {
	}
}
