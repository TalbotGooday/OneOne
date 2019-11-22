package com.gapps.oneone.utils.bottom_menu.models

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class MenuDataItem {
	companion object {

		fun fromPairsList(list: List<Pair<Int, Int>>): List<MenuDataItem> {
			return list.mapIndexed {index, it ->
				MenuDataItem().apply {
					this.id = index
					this.icon = it.first
					this.titleRes = it.second
				}
			}
		}

	}

	var id: Int? = null
	@DrawableRes
	var icon: Int? = null
	@StringRes
	var titleRes: Int? = null
	var titleString: String? = null
	@ColorRes
	var color: Int? = null

	var action: Action = Action.HEART

	enum class Action {
		HEART,
		CANCEL,
		NEVER
	}
}