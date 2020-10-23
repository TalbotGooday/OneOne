package com.gapps.oneone.screens.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.gapps.oneone.R
import com.gapps.oneone.utils.extensions.color
import com.gapps.oneone.utils.extensions.setSystemBarColorInt

abstract class BaseActivity(@LayoutRes layoutRes: Int) : AppCompatActivity(layoutRes) {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setSystemBarColorInt(color(R.color.colorOOPrimaryDarkLog))
	}
}