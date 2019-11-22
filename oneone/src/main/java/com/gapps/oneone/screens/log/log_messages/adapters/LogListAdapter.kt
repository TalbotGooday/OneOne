package com.gapps.oneone.screens.log.log_messages.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gapps.oneone.R
import com.gapps.oneone.models.log.LogModel
import com.gapps.oneone.utils.extensions.toStrDate
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
			tag_text.text = item.tag
			time_text.text = item.time.toStrDate()
			setOnClickListener {
				listener.onItemClick(item)
			}
		}
	}

	interface Listener {
		fun onItemClick(item: LogModel)
	}
}