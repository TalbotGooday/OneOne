package com.gapps.oneone.screens.log.log_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gapps.oneone.R
import com.gapps.oneone.models.log.LogModel
import com.gapps.oneone.screens.base.BaseActivity
import com.gapps.oneone.screens.log.log_details.core.LogDetailsContract
import com.gapps.oneone.screens.log.log_details.core.LogDetailsPresenter
import com.gapps.oneone.utils.extensions.*
import kotlinx.android.synthetic.main.activity_oo_log_details.*

class LogDetailsActivity : BaseActivity(R.layout.activity_oo_log_details), LogDetailsContract.View {
	companion object {
		private const val ITEM = "item"

		fun newInstance(context: Context, item: LogModel) = Intent(context, LogDetailsActivity::class.java).apply {
			putExtra(ITEM, item)
		}
	}

	override var presenter = LogDetailsPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		initViews()
	}

	override fun onDestroy() {
		super.onDestroy()
		presenter.destroy()
	}

	private fun initViews() {
		back.setOnClickListener { onBackPressed() }

		val item = intent.getParcelableExtra<LogModel?>(ITEM) ?: return

		val type = item.type

		initTypeIndicator(type)

		tag_container.visibleOrGone(item.tag.isNotBlank())
		type_container.visibleOrGone(type.isNotBlank())
		message_container.visibleOrGone(item.message.isNotBlank())

		tag_text.text = item.tag
		type_text.text = type
		message_text.text = item.message
		time_text.text = item.time.toStrDate()

		message_container.setOnLongClickListener {
			copyToClipboard(item.message) {
				toast(R.string.copied)
			}

			true
		}
	}

	private fun initTypeIndicator(type: String) {
		val colorRes = getLogIndicatorByType(type)

		log_indicator.setColorFilter(color(colorRes))
	}
}
