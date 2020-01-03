package com.gapps.oneone.screens.log.log_files.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gapps.oneone.R
import com.gapps.oneone.models.log.FileModel
import kotlinx.android.synthetic.main.item_file.view.*


class LogFilesListAdapter(private val listener: Listener) : RecyclerView.Adapter<LogFilesListAdapter.Holder>() {

	private var data: MutableList<FileModel> = mutableListOf()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
		return Holder(
				LayoutInflater.from(parent.context)
						.inflate(R.layout.item_file, parent, false)
		)
	}

	override fun getItemCount() = data.size

	override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(data[position], listener)

	fun swapData(data: List<FileModel>) {
		this.data.clear()
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	fun addData(data: List<FileModel>) {
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(item: FileModel, listener: Listener) = with(itemView) {
			file_name.text = item.name

			setOnClickListener {
				listener.onItemClick(item)
			}
		}
	}

	interface Listener {
		fun onItemClick(item: FileModel)
	}
}