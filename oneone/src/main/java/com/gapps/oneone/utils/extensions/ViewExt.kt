package com.gapps.oneone.utils.extensions

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gapps.oneone.utils.views.divider.DividerItemDecoration
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

fun View.gone() {
	if (visibility != View.GONE) {
		visibility = View.GONE
	}
}

fun View.goneAlpha() {
	if (alpha != 0f) {
		alpha = 0f
	}
}

fun View.visibleAlpha() {
	if (alpha != 1f) {
		alpha = 1f
	}
}

fun View.animateGone(duration: Long = 100) {
	if (visibility != View.GONE) {
		animate().setDuration(duration)
				.alpha(0f)
				.withEndAction {
					visibility = View.GONE
				}
	}
}

fun View.animateGoneResize(duration: Long = 100) {
	if (visibility != View.GONE) {
		animate().setDuration(duration)
				.scaleX(0f)
				.scaleY(0f)
				.withEndAction {
					visibility = View.GONE
				}
	}
}

fun View.visible() {
	if (visibility != View.VISIBLE) {
		visibility = View.VISIBLE
	}
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.animateVisible(duration: Long = 100) {
	if (visibility != View.VISIBLE) {
		alpha = 0f
		visibility = View.VISIBLE
		animate().setDuration(duration)
				.alpha(1f)
	}
}

fun View.invisible() {
	if (visibility != View.INVISIBLE) {
		visibility = View.INVISIBLE
	}
}

fun View.animateInvisible(duration: Long = 100) {
	if (visibility != View.INVISIBLE) {
		animate().setDuration(duration)
				.alpha(0f)
				.withEndAction {
					visibility = View.INVISIBLE
				}
	}
}

fun View.visibleOrGone(visible: Boolean) {
	visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleOrInvisible(visible: Boolean) {
	visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun AppCompatTextView.setTextAnimated(text: CharSequence?, animationTime: Long, listener: () -> Unit) {
	this.animate().setDuration(animationTime)
			.alpha(0f)
			.withEndAction {
				visibility = View.GONE
				setText(text)
				alpha = 0f
				visibility = View.VISIBLE
				animate().setDuration(animationTime)
						.alpha(1f)
						.withEndAction {
							listener.invoke()
						}
			}
}

/* Edit Text */
fun EditText.text(): String = text.toString()

fun ImageView.setTint(@ColorRes color: Int) {
	this.setColorFilter(context.color(color), PorterDuff.Mode.SRC_IN)
}


fun View?.attachWithKeyboardHideEvent(viewForFocus: View? = null) {
	this?.setOnTouchListener { view, event ->
		if (event.action == MotionEvent.ACTION_DOWN)
			this.context.hideKeyboard()


		(viewForFocus ?: view).apply {
			isFocusable = true
			isClickable = true
			isFocusableInTouchMode = true

			requestFocus()
		}

		return@setOnTouchListener false
	}
}

fun TextView.showKeyboard(): Boolean {
	requestFocus()

	//                     show keyboard without InputMethodManager.SHOW_FORCED method
	return postDelayed({
		dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0))
		dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0))
	}, 200)
}


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


fun TextView?.attachWithRecyclerViewKeyboardHide(recyclerView: RecyclerView) {
	requestFocus(recyclerView)
	recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
		override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
			super.onScrollStateChanged(recyclerView, newState)
			requestFocus(recyclerView)

			this@attachWithRecyclerViewKeyboardHide?.context?.hideKeyboard(this@attachWithRecyclerViewKeyboardHide)
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


fun View.showSnackBar(s: String) {
	Snackbar.make(this, s, Snackbar.LENGTH_LONG).show()
}


fun Drawable.tint(context: Context, @ColorRes color: Int) {
	tint(context.color(color))
}

fun Drawable.tint(@ColorInt color: Int) {
	this.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
}

fun NavigationView.setTextColorForMenuItems(@ColorInt color: Int) {
	for (i: Int in 0 until menu.size()) {
		val menuItem = menu.getItem(i)
		val spanString = SpannableString(menuItem.title.toString())
		spanString.setSpan(ForegroundColorSpan(color), 0, spanString.length, 0)
		menuItem.title = spanString
	}
}

fun RecyclerView.addDivider(dividerDrawable: Drawable?, orientation: Int = LinearLayoutManager.VERTICAL, @Px inset: Int = 0, withLast: Boolean = true) {
	dividerDrawable?.let {
		val dividerItemDecoration = DividerItemDecoration(it, inset, 0, withLast).apply {
			this.orientation = orientation
		}

		addItemDecoration(dividerItemDecoration)
	}
}

fun RecyclerView.addDivider(@DrawableRes res: Int, orientation: Int = LinearLayoutManager.VERTICAL, @Px inset: Int = 0, withLast: Boolean = true) {
	val dividerDrawable = ContextCompat.getDrawable(context, res)
	addDivider(dividerDrawable, orientation, inset, withLast)
}
