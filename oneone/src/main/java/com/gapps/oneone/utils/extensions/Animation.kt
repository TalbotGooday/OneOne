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