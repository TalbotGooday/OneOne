package com.gapps.oneone.utils.extensions

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Px
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gapps.oneone.utils.views.divider.DividerItemDecoration

fun View.gone() {
	if (visibility != View.GONE) {
		visibility = View.GONE
	}
}

fun View.visible() {
	if (visibility != View.VISIBLE) {
		visibility = View.VISIBLE
	}
}

fun View.visibleOrGone(visible: Boolean) {
	visibility = if (visible) View.VISIBLE else View.GONE
}

/* Edit Text */
fun EditText.text(): String = text.toString()

fun RecyclerView?.attachWithKeyboardHideEvent(viewForFocus: View? = null) {
	this?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
		override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
			super.onScrollStateChanged(recyclerView, newState)

			(viewForFocus ?: recyclerView).apply {
				isFocusable = true
				isClickable = true
				isFocusableInTouchMode = true
				requestFocus()
			}

			this@attachWithKeyboardHideEvent.context.hideKeyboard()
		}
	})
}

fun requestFocus(view: View) {
	view.apply {
		isFocusable = true
		isClickable = true
		isFocusableInTouchMode = true
		requestFocus()
	}
}


fun Drawable.tint(context: Context, @ColorRes color: Int) {
	tint(context.color(color))
}

fun Drawable.tint(@ColorInt color: Int) {
	this.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
}

fun RecyclerView.addDivider(dividerDrawable: Drawable?, orientation: Int = LinearLayoutManager.VERTICAL, @Px inset: Int = 0, withLast: Boolean = true) {
	dividerDrawable?.let {
		val dividerItemDecoration = DividerItemDecoration(it, inset, 0, withLast).apply {
			this.orientation = orientation
		}

		addItemDecoration(dividerItemDecoration)
	}
}