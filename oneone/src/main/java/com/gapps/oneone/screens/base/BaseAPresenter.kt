package com.gapps.oneone.screens.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseAPresenter: CoroutineScope {
	private val job = Job()

	override val coroutineContext: CoroutineContext
		get() = job + Dispatchers.Main
}