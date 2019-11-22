package com.gapps.oneone.utils.bottom_menu

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.gapps.oneone.R
import com.gapps.oneone.utils.bottom_menu.models.MenuDataItem
import com.gapps.oneone.utils.extensions.animateBottomTop
import kotlinx.android.synthetic.main.item_bottom_menu_small.view.*
import java.util.*

class MenuAdapter(
		@ColorInt private val iconsColor: Int,
		@ColorInt private val textColor: Int,
		private val listener: Listener
) : RecyclerView.Adapter<MenuAdapter.Holder>() {

	private var data: MutableList<MenuDataItem> = ArrayList()

	private var lastPosition = -1
	private var onAttach = true

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
		return Holder(
				LayoutInflater.from(parent.context)
						.inflate(R.layout.item_bottom_menu_small, parent, false)
		)
	}

	override fun getItemCount() = data.size


	override fun onBindViewHolder(holder: Holder, position: Int) {
		if (listener.showAnimation()) {
			setAnimation(holder.itemView, position)
		}

		holder.bind(data[position], iconsColor, textColor, listener)
	}

	fun swapData(data: List<MenuDataItem>) {
		this.data.clear()
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	fun addData(data: List<MenuDataItem>) {
		this.data.addAll(data)
		notifyDataSetChanged()

	}

	private fun setAnimation(view: View, position: Int) {
		if (position > lastPosition) {
			view.animateBottomTop(if (onAttach) position else -1)

			lastPosition = position
		}
	}

	override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
		recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
				onAttach = false
				super.onScrollStateChanged(recyclerView, newState)
			}
		})

		super.onAttachedToRecyclerView(recyclerView)
	}

	inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(
				item: MenuDataItem,
				@ColorInt iconsColor: Int,
				@ColorInt textColor: Int,
				listener: Listener
		) = with(itemView) {

			if (item.icon == null) {
				icon.visibility = View.GONE
			} else {
				item.icon?.run { icon.setImageResource(this) }
			}

			icon.setColorFilter(iconsColor)

			item.titleRes?.run { text.setText(this) }
			item.titleString?.run { text.text = this }

			text.setTextColor(textColor)

			setOnClickListener {
				listener.onItemClick(item, adapterPosition)
			}

			setOnTouchListener { _, event ->
				if (event.action == MotionEvent.ACTION_DOWN) {
					listener.onItemDown(item, adapterPosition)
				} else if (event.action == MotionEvent.ACTION_CANCEL) {
					listener.onItemUp(item, adapterPosition)
				}

				return@setOnTouchListener false
			}
		}
	}

	interface Listener {
		fun onItemClick(item: MenuDataItem, adapterPosition: Int)
		fun onItemDown(item: MenuDataItem, adapterPosition: Int)
		fun onItemUp(item: MenuDataItem, adapterPosition: Int)
		fun showAnimation(): Boolean
	}
}