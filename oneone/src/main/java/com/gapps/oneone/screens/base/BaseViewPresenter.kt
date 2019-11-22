package com.gapps.oneone.screens.base

interface BaseViewPresenter<T> {
    var view: T

    fun create(context: T)

    fun destroy()

    companion object {
        const val DAYS_ERROR: Int = -1
        const val SOMETHING_WRONG: String = "Something went wrong"
    }
}