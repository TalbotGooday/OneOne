package com.gapps.oneone.screens.log.logs_list.adapters

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gapps.oneone.R
import com.gapps.oneone.models.log.LogFileModel
import com.gapps.oneone.utils.*
import kotlinx.android.synthetic.main.item_log_category.view.*


class LogsListAdapter(private val listener: Listener) : RecyclerView.Adapter<LogsListAdapter.Holder>() {

	private var data: MutableList<LogFileModel> = mutableListOf()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
		return Holder(
				LayoutInflater.from(parent.context)
						.inflate(R.layout.item_log_category, parent, false)
		)
	}

	override fun getItemCount() = data.size

	override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(data[position], listener)

	fun swapData(data: List<LogFileModel>) {
		this.data.clear()
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	fun addData(data: List<LogFileModel>) {
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(item: LogFileModel, listener: Listener) = with(itemView) {
			message.text = item.name

			val color = when (item.type) {
				DEBUG -> {
					R.color.colorDebug
				}
				WARNING -> {
					R.color.colorWarnings
				}
				ERROR -> {
					R.color.colorErrors
				}
				INFO -> {
					R.color.colorErrors
				}
				VERBOSE -> {
					R.color.colorDebug
				}
				else -> {
					Color.BLACK
				}
			}

			val indicator = ContextCompat.getDrawable(itemView.context, R.drawable.child_indicator)?.apply {
				colorFilter = PorterDuffColorFilter(ContextCompat.getColor(itemView.context, color), PorterDuff.Mode.SRC_IN)
			}

			log_indicator.setImageDrawable(indicator)

			setOnClickListener {
				listener.onItemClick(item)
			}
		}
	}

	interface Listener {
		fun onItemClick(item: LogFileModel)
	}
}