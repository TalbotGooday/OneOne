package com.gapps.oneone.screens.shared_prefs.sp_file.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gapps.oneone.R
import com.gapps.oneone.models.shared_prefs.SharedPrefEntry
import kotlinx.android.synthetic.main.item_sp_entry.view.*


class SharedPrefEntriesAdapter(private val listener: Listener) : RecyclerView.Adapter<SharedPrefEntriesAdapter.Holder>() {

	private var data: MutableList<SharedPrefEntry> = mutableListOf()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
		return Holder(
				LayoutInflater.from(parent.context)
						.inflate(R.layout.item_sp_entry, parent, false)
		)
	}

	override fun getItemCount() = data.size

	override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(data[position], listener)

	fun swapData(data: List<SharedPrefEntry>) {
		this.data.clear()
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	fun addData(data: List<SharedPrefEntry>) {
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	fun updateByKey(key: String, value: String?) {
		val position = this.data.indexOfFirst { it.key == key }

		if (position < 0) return

		value?.let {
			this.data[position] = this.data[position].apply {
				this.value = it
			}
		}

		this.notifyItemChanged(position)
	}

	class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(item: SharedPrefEntry, listener: Listener) = with(itemView) {
			key_text.text = item.key
			value_text.text = item.value
			type_text.text = item.type

			setOnClickListener {
				listener.onItemClick(item)
			}
		}
	}

	interface Listener {
		fun onItemClick(item: SharedPrefEntry)
	}
}