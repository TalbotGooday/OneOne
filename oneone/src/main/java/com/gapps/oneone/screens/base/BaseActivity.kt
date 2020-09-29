package com.gapps.oneone.screens.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gapps.oneone.R
import com.gapps.oneone.utils.extensions.color
import com.gapps.oneone.utils.extensions.setSystemBarColorInt

abstract class BaseActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setSystemBarColorInt(color(R.color.colorOOPrimaryDarkLog))
	}
}