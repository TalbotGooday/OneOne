package com.gapps.oneone.utils.bottom_menu.models

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

internal class MenuDataItem {
	var id: Int? = null
	@DrawableRes
	var icon: Int? = null
	@StringRes
	var titleRes: Int? = null
	var titleString: String? = null
	@ColorRes
	var color: Int? = null
}