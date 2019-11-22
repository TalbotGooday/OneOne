package com.gapps.oneone.utils.views.divider

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Adds interior dividers to a RecyclerView with a LinearLayoutManager or its
 * subclass.
 */
class DividerItemDecoration : RecyclerView.ItemDecoration {

	private var mDivider: Drawable
	var orientation: Int = 0
	private val mLeftInset: Int
	private val mRightInset: Int
	private var withLastItem: Boolean = true
	private var dividerWidth = 0
	private var dividerHeight = 0
	/**
	 * Sole constructor. Takes in a [Drawable] to be used as the interior
	 * divider.
	 *
	 * @param divider A divider `Drawable` to be drawn on the RecyclerView
	 */
	constructor(divider: Drawable) {
		mDivider = divider
		mLeftInset = 0
		mRightInset = 0
	}

	/**
	 * Sole constructor. Takes in a [Drawable] to be used as the interior
	 * divider.
	 *
	 * @param divider A divider `Drawable` to be drawn on the RecyclerView
	 * @param leftInset Left inset of a divider
	 * @param rightInset Right inset of a divider
	 */
	constructor(divider: Drawable, leftInset: Int, rightInset: Int, withLast: Boolean = true) {
		mDivider = divider
		mLeftInset = leftInset
		mRightInset = rightInset
		withLastItem = withLast

		dividerWidth = if (mDivider.intrinsicWidth != -1) {
			mDivider.intrinsicWidth
		} else {
			mDivider.bounds.right
		}

		dividerHeight = if (mDivider.intrinsicHeight != -1) {
			mDivider.intrinsicHeight
		} else {
			mDivider.bounds.bottom
		}
	}

	/**
	 * Draws horizontal or vertical dividers onto the parent RecyclerView.
	 *
	 * @param canvas The [Canvas] onto which dividers will be drawn
	 * @param parent The RecyclerView onto which dividers are being added
	 * @param state The current RecyclerView.State of the RecyclerView
	 */
	override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
		if (orientation == LinearLayoutManager.HORIZONTAL) {
			drawHorizontalDividers(canvas, parent)
		} else if (orientation == LinearLayoutManager.VERTICAL) {
			drawVerticalDividers(canvas, parent)
		}
	}

	/**
	 * Determines the size and location of offsets between items in the parent
	 * RecyclerView.
	 *
	 * @param outRect The [Rect] of offsets to be added around the child
	 * view
	 * @param view The child view to be decorated with an offset
	 * @param parent The RecyclerView onto which dividers are being added
	 * @param state The current RecyclerView.State of the RecyclerView
	 */
	override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
		super.getItemOffsets(outRect, view, parent, state)

		if (parent.getChildAdapterPosition(view) == 0) {
			return
		}

		orientation = (parent.layoutManager as LinearLayoutManager).orientation
		if (orientation == LinearLayoutManager.HORIZONTAL) {
			outRect.left = dividerWidth
		} else if (orientation == LinearLayoutManager.VERTICAL) {
			outRect.top = dividerHeight
		}
	}

	/**
	 * Adds dividers to a RecyclerView with a LinearLayoutManager or its
	 * subclass oriented horizontally.
	 *
	 * @param canvas The [Canvas] onto which horizontal dividers will be
	 * drawn
	 * @param parent The RecyclerView onto which horizontal dividers are being
	 * added
	 */
	private fun drawHorizontalDividers(canvas: Canvas, parent: RecyclerView) {
		val parentTop = parent.paddingTop
		val parentBottom = parent.height - parent.paddingBottom

		val childCount = if (withLastItem) parent.childCount else parent.childCount - 1
		for (i in 0 until childCount) {
			val child = parent.getChildAt(i)

			val params = child.layoutParams as RecyclerView.LayoutParams

			val parentLeft = mLeftInset + child.right + params.rightMargin
			val parentRight = mRightInset + parentLeft + dividerWidth

			mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom)
			mDivider.draw(canvas)
		}
	}

	/**
	 * Adds dividers to a RecyclerView with a LinearLayoutManager or its
	 * subclass oriented vertically.
	 *
	 * @param canvas The [Canvas] onto which vertical dividers will be
	 * drawn
	 * @param parent The RecyclerView onto which vertical dividers are being
	 * added
	 */
	private fun drawVerticalDividers(canvas: Canvas, parent: RecyclerView) {
		val parentLeft = mLeftInset + parent.paddingLeft
		val parentRight = parent.width - parent.paddingRight - mRightInset

		val childCount = if (withLastItem) parent.childCount else parent.childCount - 1
		for (i in 0 until childCount) {
			val child = parent.getChildAt(i)

			val params = child.layoutParams as RecyclerView.LayoutParams

			val parentTop = child.bottom + params.bottomMargin
			val parentBottom = parentTop + dividerHeight

			mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom)
			mDivider.draw(canvas)
		}
	}
}