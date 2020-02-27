package com.gapps.oneone.utils.views.placeholder

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.gapps.oneone.R
import kotlinx.android.synthetic.main.layout_oo_placeholder.view.*

class OOEmptyListPlaceholder @JvmOverloads constructor(
		context: Context, private val attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
	init {
		inflate(context, R.layout.layout_oo_placeholder, this)
		initViews()
	}

	fun setIcon(@DrawableRes resource: Int) {
		placeholder_icon.setImageResource(resource)
	}

	fun setIconTint(@ColorInt color: Int) {
		placeholder_icon.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
	}

	fun setText(@StringRes text: Int) {
		setText(context.getString(text))
	}

	fun setText(text: String?) {
		placeholder_text.text = text
	}

	fun setTextColor(@ColorInt color: Int) {
		placeholder_text.setTextColor(color)
	}

	private fun initViews() {
		val a = context.obtainStyledAttributes(attrs, R.styleable.OOEmptyListPlaceholder)
		for (i in 0 until a.indexCount) {
			when (val attr = a.getIndex(i)) {
				R.styleable.OOEmptyListPlaceholder_srcCompat -> {
					setIcon(a.getResourceId(attr, 0))
				}
				R.styleable.OOEmptyListPlaceholder_android_text -> {
					setText(a.getString(attr))
				}
				R.styleable.OOEmptyListPlaceholder_android_textColor -> {
					setTextColor(a.getColor(attr, Color.parseColor("#99FFFFFF")))
				}
				R.styleable.OOEmptyListPlaceholder_android_tint -> {
					setIconTint(a.getColor(attr, Color.parseColor("#99FFFFFF")))
				}
			}
		}
		
		a.recycle()
	}
}