package com.gapps.oneone.log

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gapps.oneone.R
import com.gapps.oneone.models.LogModel
import com.gapps.oneone.utils.DEBUG
import com.gapps.oneone.utils.ERROR
import com.gapps.oneone.utils.WARNING
import kotlinx.android.synthetic.main.item_log.view.*
import java.util.*

class LogListAdapter(private val listener: Listener) : RecyclerView.Adapter<LogListAdapter.Holder>() {

	private var data: MutableList<LogModel> = ArrayList()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
		return Holder(
				LayoutInflater.from(parent.context)
						.inflate(R.layout.item_log, parent, false)
		)
	}

	override fun getItemCount() = data.size

	override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(data[position], listener)

	fun swapData(data: List<LogModel>) {
		this.data.clear()
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	fun addData(data: List<LogModel>) {
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(item: LogModel, listener: Listener) = with(itemView) {

			message.text = item.message

			val indicator = ContextCompat.getDrawable(itemView.context, R.drawable.child_indicator)

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
				else -> {
					Color.BLACK
				}
			}

			indicator?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(itemView.context, color), PorterDuff.Mode.SRC_IN)

			log_indicator.setImageDrawable(indicator)

			setOnClickListener {
				listener.onItemClick(item)
			}
		}
	}

	interface Listener {
		fun onItemClick(item: LogModel)
	}
}