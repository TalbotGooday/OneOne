package com.gapps.oneone.utils.extensions

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.Transformation
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

const val DURATION_IN_RIGHT_LEFT: Long = 150
const val DURATION_HIDE_DOWN: Long = 150
const val DURATION_IN_FADE: Long = 300
const val DURATION_IN_BOTTOM_TOP: Long = 75
const val DURATION_OUT_BOTTOM_TOP: Long = 175

private fun View.expandAction(): Animation {
	this.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
	val targtetHeight = this.measuredHeight

	this.layoutParams.height = 0
	this.visibility = View.VISIBLE
	val a = object : Animation() {
		override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
			this@expandAction.layoutParams.height = if (interpolatedTime == 1f)
				ViewGroup.LayoutParams.WRAP_CONTENT
			else
				(targtetHeight * interpolatedTime).toInt()
			this@expandAction.requestLayout()
		}

		override fun willChangeBounds(): Boolean {
			return true
		}
	}

	a.duration = (targtetHeight / this.context.resources.displayMetrics.density).toInt().toLong()
	this.startAnimation(a)
	return a
}

fun View.expandActionColor(targtetHeight: Int = this.measuredHeight, @ColorRes colorInit: Int, @ColorRes colorExpand: Int, view: List<View>) {
	val argbEvaluator = ArgbEvaluator()

	this.layoutParams.height = 0
	this.visibility = View.VISIBLE
	val a = object : Animation() {
		override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
			this@expandActionColor.layoutParams.height = if (interpolatedTime == 1f)
				ViewGroup.LayoutParams.WRAP_CONTENT
			else {
				val fl = targtetHeight * interpolatedTime
				fl.toInt()
			}

			val newColor = argbEvaluator.evaluate(interpolatedTime,
					ContextCompat.getColor(context, colorInit),
					ContextCompat.getColor(context, colorExpand)) as Int

			val newColorOther = argbEvaluator.evaluate(interpolatedTime,
					ContextCompat.getColor(context, colorExpand),
					ContextCompat.getColor(context, colorInit)) as Int

			if (view.isNotEmpty()) {
				view[0].setBackgroundColor(newColor)

				for (i: Int in 1 until view.size) {
					val view1 = view[i]
					if (view1 is TextView && view1.background == null) {
						view1.setTextColor(newColorOther)
					}
				}
			}

			this@expandActionColor.requestLayout()
		}

		override fun willChangeBounds(): Boolean {
			return true
		}
	}

	var duration = (targtetHeight / this.context.resources.displayMetrics.density).toInt()
	if (duration > 300)
		duration = 300

	a.duration = duration.toLong()

	this.startAnimation(a)
}

fun View.collapse() {
	val initialHeight = this.measuredHeight

	val a = object : Animation() {
		override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
			if (interpolatedTime == 1f) {
				this@collapse.gone()
			} else {
				this@collapse.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
				this@collapse.requestLayout()
			}
		}

		override fun willChangeBounds(): Boolean {
			return true
		}
	}

	var duration = (initialHeight / this.context.resources.displayMetrics.density).toInt()
	if (duration > 300)
		duration = 300

	a.duration = duration.toLong()
	this.startAnimation(a)
}

fun View.collapseColor(colorInit: Int, colorCollapse: Int, view: List<View>) {
	val argbEvaluator = ArgbEvaluator()

	val initialHeight = this.measuredHeight

	val a = object : Animation() {
		override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
			if (interpolatedTime == 1f) {
				this@collapseColor.gone()
			} else {
				this@collapseColor.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
				this@collapseColor.requestLayout()
			}

			val newColor = argbEvaluator.evaluate(interpolatedTime,
					ContextCompat.getColor(context, colorInit),
					ContextCompat.getColor(context, colorCollapse)) as Int

			val newColorOther = argbEvaluator.evaluate(interpolatedTime,
					ContextCompat.getColor(context, colorCollapse),
					ContextCompat.getColor(context, colorInit)) as Int

			if (view.isNotEmpty()) {
				view[0].setBackgroundColor(newColor)

				for (i: Int in 1 until view.size) {
					val view1 = view[i]
					if (view1 is TextView && view1.background == null) {
						view1.setTextColor(newColorOther)
					}
				}
			}

		}

		override fun willChangeBounds(): Boolean {
			return true
		}
	}

	var duration = (initialHeight / this.context.resources.displayMetrics.density).toInt()
	if (duration > 300)
		duration = 300

	a.duration = duration.toLong()
	this.startAnimation(a)
}

fun View.expand() {
	val a = this.expandAction()
	this.startAnimation(a)
}


fun View.toggleArrow(show: Boolean, delay: Boolean): Boolean {
	return if (show) {
		this.animate().setDuration((if (delay) 200 else 0).toLong()).rotation(180f)
		true
	} else {
		this.animate().setDuration((if (delay) 200 else 0).toLong()).rotation(0f)
		false
	}
}


internal fun View.animateRightLeft(position: Int = 0) {
	var position = position
	position += 1
	this.translationX = this.x + 400
	this.alpha = 0f

	val animatorSet = AnimatorSet()
	val animatorTranslateY = ObjectAnimator.ofFloat(this, "translationX", this.x + 400, 0f)
	val animatorAlpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
	animatorTranslateY.startDelay = position * DURATION_IN_RIGHT_LEFT
	animatorTranslateY.duration = DURATION_IN_RIGHT_LEFT
	animatorAlpha.startDelay = position * DURATION_IN_RIGHT_LEFT
	animatorAlpha.duration = DURATION_IN_RIGHT_LEFT
	animatorSet.playTogether(animatorTranslateY, animatorAlpha)
	animatorSet.start()
}

internal fun View.animateHideDown() {
	val animatorTranslateY = ObjectAnimator.ofFloat(this, "translationY", this.height.toFloat())
	animatorTranslateY.startDelay = DURATION_HIDE_DOWN
	animatorTranslateY.duration = DURATION_HIDE_DOWN
	animatorTranslateY.start()
}


internal fun View.animateFadeIn(position: Int = 0, duration: Long = DURATION_IN_FADE) {
	var position = position
	val notFirstItem = position == -1
	position += 1
	this.alpha = 0f
	val animatorSet = AnimatorSet()
	val animatorAlpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 0.5f, 1f)
	ObjectAnimator.ofFloat(this, "alpha", 0f).start()
	animatorAlpha.startDelay = if (notFirstItem) duration / 2 else position * duration / 3
	animatorAlpha.duration = duration
	animatorSet.play(animatorAlpha)
	animatorSet.start()
}

internal fun View.animateBottomTop(position: Int = 0, onFinish: (() -> Unit)? = null) {
	var position = position
	position += 1
	this.translationY = this.y + 50
	this.alpha = 0f

	val animatorSet = AnimatorSet()
	val animatorTranslateY = ObjectAnimator.ofFloat(this, "translationY", this.y + 50, 0f)
	val animatorAlpha = ObjectAnimator.ofFloat(this, "alpha", 1f)
	animatorTranslateY.startDelay = position * DURATION_IN_BOTTOM_TOP
	animatorTranslateY.duration = DURATION_IN_BOTTOM_TOP
	animatorAlpha.startDelay = position * DURATION_IN_BOTTOM_TOP
	animatorAlpha.duration = DURATION_IN_BOTTOM_TOP
	animatorSet.playTogether(animatorTranslateY, animatorAlpha)
	animatorSet.addListener(object : Animator.AnimatorListener {
		override fun onAnimationRepeat(animation: Animator?) {
		}

		override fun onAnimationEnd(animation: Animator?) {
			onFinish?.invoke()
		}

		override fun onAnimationCancel(animation: Animator?) {
		}

		override fun onAnimationStart(animation: Animator?) {
		}
	})
	animatorSet.start()
}

internal fun View.animateTopBottom(position: Int = 0, onFinish: (() -> Unit)? = null) {
	var position = position
	position += 1

	val animatorSet = AnimatorSet()
	val animatorTranslateY = ObjectAnimator.ofFloat(this, "translationY", this.y, 50f)
	val animatorAlpha = ObjectAnimator.ofFloat(this, "alpha", 0f)
	animatorTranslateY.startDelay = position * DURATION_OUT_BOTTOM_TOP
	animatorTranslateY.duration = DURATION_OUT_BOTTOM_TOP
	animatorAlpha.startDelay = position * DURATION_OUT_BOTTOM_TOP
	animatorAlpha.duration = DURATION_OUT_BOTTOM_TOP
	animatorSet.playTogether(animatorTranslateY, animatorAlpha)
	animatorSet.addListener(object : Animator.AnimatorListener {
		override fun onAnimationRepeat(animation: Animator?) {
		}

		override fun onAnimationEnd(animation: Animator?) {
			onFinish?.invoke()
		}

		override fun onAnimationCancel(animation: Animator?) {
		}

		override fun onAnimationStart(animation: Animator?) {
		}
	})

	animatorSet.start()
}

fun View.scaleView(startScale: Float, endScale: Float) {
	val anim: Animation = ScaleAnimation(
			startScale, endScale,  // Start and end values for the X axis scaling
			startScale, endScale,  // Start and end values for the Y axis scaling
			Animation.RELATIVE_TO_SELF, 0f,  // Pivot point of X scaling
			Animation.RELATIVE_TO_SELF, 1f) // Pivot point of Y scaling
	anim.fillAfter = true // Needed to keep the result of the animation
	anim.duration = 1000

	this.startAnimation(anim)
}