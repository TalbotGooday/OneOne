package com.gapps.oneone.screens.base

import android.content.Context

interface BasePresenter<T> {
    var view: T

    fun create(context: Context)

    fun destroy()

    companion object {
        const val DAYS_ERROR: Int = -1
        const val SOMETHING_WRONG: String = "Something went wrong"
    }
}