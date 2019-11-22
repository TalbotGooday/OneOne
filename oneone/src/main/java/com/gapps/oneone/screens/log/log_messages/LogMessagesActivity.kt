package com.gapps.oneone.screens.log.log_messages

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gapps.oneone.OneOne
import com.gapps.oneone.R
import com.gapps.oneone.models.log.LogFileModel
import com.gapps.oneone.models.log.LogModel
import com.gapps.oneone.screens.base.BaseActivity
import com.gapps.oneone.screens.log.log_details.LogDetailsActivity
import com.gapps.oneone.screens.log.log_messages.adapters.LogListAdapter
import com.gapps.oneone.screens.log.log_messages.core.LogMessagesContract
import com.gapps.oneone.screens.log.log_messages.core.LogMessagesPresenter
import com.gapps.oneone.utils.*
import com.gapps.oneone.utils.extensions.addDivider
import com.gapps.oneone.utils.extensions.color
import com.gapps.oneone.utils.extensions.toColorDrawable
import kotlinx.android.synthetic.main.activity_log_messages.*

class LogMessagesActivity : BaseActivity(), LogMessagesContract.View {
	companion object {
		private const val TITLE = "title"
		private const val TYPE = "type"

		fun newInstance(context: Context, item: LogFileModel) = Intent(context, LogMessagesActivity::class.java).apply {
			putExtra(TITLE, item.name)
			putExtra(TYPE, item.type)
		}
	}

	override var presenter = LogMessagesPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_log_messages)

		initViews()
	}

	private fun initViews() {
		back.setOnClickListener { onBackPressed() }

		val type = intent.getStringExtra(TYPE) ?: return
		val titleText = intent.getStringExtra(TITLE)

		initTypeIndicator(type)

		toolbar_title.text = titleText

		log_list.apply {
			layoutManager = LinearLayoutManager(this@LogMessagesActivity)
			adapter = LogListAdapter(object : LogListAdapter.Listener {
				override fun onItemClick(item: LogModel) {
					openLogDetails(item)
				}
			}).apply {
				OneOne.getMessages()
				swapData(presenter.getLogList(type))
			}

			addDivider(color(R.color.dividerColor).toColorDrawable(resources.getDimensionPixelSize(R.dimen.divider_height)), inset = resources.getDimensionPixelSize(R.dimen.divider_inset))
		}
	}

	private fun openLogDetails(item: LogModel) {
		startActivity(LogDetailsActivity.newInstance(this, item))
	}


	private fun initTypeIndicator(type: String) {
		val colorRes = when (type) {
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
				android.R.color.white
			}
			else -> {
				Color.BLACK
			}
		}

		log_indicator.setColorFilter(color(colorRes))
	}
}