package com.gapps.oneone.screens.base

interface BaseView<out T : BasePresenter<*>> {

    val presenter: T
}