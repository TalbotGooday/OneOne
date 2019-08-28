package com.gapps.oneonelogger

import android.app.Application
import com.gapps.oneone.OneOne

class App: Application() {
	override fun onCreate() {
		super.onCreate()

		OneOne.init(this)

		OneOne.d(message = "init")
	}
}