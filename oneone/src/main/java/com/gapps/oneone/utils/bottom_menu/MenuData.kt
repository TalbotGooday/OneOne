package com.gapps.oneone.utils.bottom_menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.gapps.oneone.utils.bottom_menu.models.MenuDataItem

internal class MenuData {
	var title: String? = null
	@StringRes
	var titleRes: Int? = null

	@DrawableRes
	var buttonIcon: Int? = null
	var button: String? = null
	@StringRes
	var buttonRes: Int? = null

	@StringRes
	var cancelText: Int? = null

	val data: MutableList<MenuDataItem> = mutableListOf()

	fun addMenu(menuItem: MenuDataItem) {
		data.add(menuItem)
	}

	fun addMenu(@DrawableRes icon: Int, @StringRes text: Int, id: Int? = null) {
		data.add(MenuDataItem().apply {
			id?.let { this.id = it }

			this.icon = icon
			this.titleRes = text
		})
	}

	fun addMenu(@DrawableRes icon: Int, text: String) {
		data.add(MenuDataItem().apply {
			this.icon = icon
			this.titleString = text
		})
	}

	fun addMenu(text: String, id: Int? = null) {
		data.add(MenuDataItem().apply {
			this.icon = icon
			this.titleString = text
		})
	}

	fun addAll(data: List<MenuDataItem>) {
		this.data.addAll(data)
	}
}