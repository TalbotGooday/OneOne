package com.gapps.oneone.screens.shared_prefs.sp_files_list.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gapps.oneone.R
import com.gapps.oneone.models.shared_prefs.SharedPrefsFileModel
import kotlinx.android.synthetic.main.item_oo_file.view.*


internal class SharedPrefsAdapter(private val listener: Listener) : RecyclerView.Adapter<SharedPrefsAdapter.Holder>() {

	private var data: MutableList<SharedPrefsFileModel> = mutableListOf()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
		return Holder(
				LayoutInflater.from(parent.context)
						.inflate(R.layout.item_oo_file, parent, false)
		)
	}

	override fun getItemCount() = data.size

	override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(data[position], listener)

	fun swapData(data: List<SharedPrefsFileModel>) {
		this.data.clear()
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	fun addData(data: List<SharedPrefsFileModel>) {
		this.data.addAll(data)
		notifyDataSetChanged()
	}

	class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(item: SharedPrefsFileModel, listener: Listener) = with(itemView) {
			file_name.text = item.name

			setOnClickListener {
				listener.onItemClick(item)
			}
		}
	}

	interface Listener {
		fun onItemClick(item: SharedPrefsFileModel)
	}
}