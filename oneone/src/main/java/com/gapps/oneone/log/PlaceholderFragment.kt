package com.gapps.oneone.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gapps.oneone.models.LogModel
import com.gapps.oneone.R
import kotlinx.android.synthetic.main.fragment_main.*

class PlaceholderFragment : Fragment() {
	companion object {

		private const val DATA = "data"

		@JvmStatic
		fun newInstance(data: List<LogModel>?): PlaceholderFragment {
			return PlaceholderFragment().apply {
				arguments = Bundle().apply {
					data?.run { putParcelableArrayList(DATA, ArrayList(this)) }
				}
			}
		}
	}


	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_main, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initList()
	}

	private fun initList() {
		list.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = LogListAdapter(object : LogListAdapter.Listener {
				override fun onItemClick(item: LogModel) {
					openLogItem(item)
				}
			}).apply {
				arguments?.getParcelableArrayList<LogModel>(DATA)?.toList()?.let {
					swapData(it)
				}
			}
		}
	}

	private fun openLogItem(item: LogModel) {

	}
}