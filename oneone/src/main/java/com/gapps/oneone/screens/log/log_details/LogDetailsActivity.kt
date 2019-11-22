package com.gapps.oneone.screens.log.log_details

import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.graphics.Color

import com.gapps.oneone.R
import com.gapps.oneone.models.log.LogModel
import com.gapps.oneone.screens.base.BaseActivity

import com.gapps.oneone.screens.log.log_details.core.LogDetailsContract
import com.gapps.oneone.screens.log.log_details.core.LogDetailsPresenter
import com.gapps.oneone.utils.*
import com.gapps.oneone.utils.extensions.color
import com.gapps.oneone.utils.extensions.toStrDate
import com.gapps.oneone.utils.extensions.visibleOrGone
import kotlinx.android.synthetic.main.activity_log_details.*

class LogDetailsActivity : BaseActivity(), LogDetailsContract.View {
	companion object {
		private const val ITEM = "item"

		fun newInstance(context: Context, item: LogModel) = Intent(context, LogDetailsActivity::class.java).apply {
			putExtra(ITEM, item)
		}
	}

	override var presenter = LogDetailsPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_log_details)

		initViews()
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
