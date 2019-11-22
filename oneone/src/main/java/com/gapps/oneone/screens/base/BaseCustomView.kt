package com.gapps.oneone.screens.base

interface BaseCustomView<out T : BaseViewPresenter<*>> {

    val presenter: T
}