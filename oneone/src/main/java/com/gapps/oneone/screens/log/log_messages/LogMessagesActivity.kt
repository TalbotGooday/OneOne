package com.gapps.oneone.screens.log.log_messages

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gapps.oneone.R
import com.gapps.oneone.models.log.LogFileModel
import com.gapps.oneone.models.log.LogModel
import com.gapps.oneone.screens.base.BaseActivity
import com.gapps.oneone.screens.log.log_details.LogDetailsActivity
import com.gapps.oneone.screens.log.log_messages.adapters.LogListAdapter
import com.gapps.oneone.screens.log.log_messages.core.LogMessagesContract
import com.gapps.oneone.screens.log.log_messages.core.LogMessagesPresenter
import com.gapps.oneone.utils.bottom_menu.BottomMenu
import com.gapps.oneone.utils.bottom_menu.MenuData
import com.gapps.oneone.utils.bottom_menu.models.MenuDataItem
import com.gapps.oneone.utils.extensions.*
import kotlinx.android.synthetic.main.activity_log_messages.*

class LogMessagesActivity : BaseActivity(), LogMessagesContract.View {
	companion object {
		private const val TITLE = "title"
		private const val TYPE = "type"

		private const val CLEAR = 0

		fun newInstance(context: Context, item: LogFileModel) = Intent(context, LogMessagesActivity::class.java).apply {
			putExtra(TITLE, item.name)
			putExtra(TYPE, item.type)
		}
	}

	private var type: String = ""

	override var presenter = LogMessagesPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_log_messages)

		presenter.create(this)

		type = intent.getStringExtra(TYPE) ?: return

		initViews()

		presenter.getLogList(type)
	}

	override fun onDestroy() {
		super.onDestroy()
		presenter.destroy()
	}

	override fun onGotMessages(messages: List<LogModel>) {
		(log_list.adapter as LogListAdapter).swapData(messages)

		messages_placeholder.visibleOrGone(messages.isEmpty())
	}

	override fun onLogCleared() {
		(log_list.adapter as LogListAdapter).removeAll()

		messages_placeholder.visible()
	}

	private fun initViews() {
		back.setOnClickListener { onBackPressed() }
		more.setOnClickListener { showMenu() }

		val titleText = intent.getStringExtra(TITLE)

		initTypeIndicator(type)

		toolbar_title.text = titleText

		log_list.apply {
			layoutManager = LinearLayoutManager(this@LogMessagesActivity)
			adapter = LogListAdapter(object : LogListAdapter.Listener {
				override fun onItemClick(item: LogModel) {
					openLogDetails(item)
				}
			})

			addDivider(color(R.color.dividerColor).toColorDrawable(resources.getDimensionPixelSize(R.dimen.divider_height)), inset = resources.getDimensionPixelSize(R.dimen.divider_inset))
		}
	}

	private fun openLogDetails(item: LogModel) {
		startActivity(LogDetailsActivity.newInstance(this, item))
	}

	private fun initTypeIndicator(type: String) {
		val colorRes = getLogIndicatorByType(type)

		val color = color(colorRes)

		log_indicator.setColorFilter(color)

		messages_placeholder.setIconTint(color)
	}

	private fun showMenu() {
		val menu = MenuData().apply {
			titleRes = R.string.select_action
			addMenu(R.drawable.ic_delete, R.string.clear_all_messages, CLEAR)
		}

		BottomMenu.build {
			with(this@LogMessagesActivity)
			withBackgroundColor(color(R.color.colorPrimaryLog))
			withMainColor(color(R.color.colorPrimaryDarkLog))
			withAccentColor(Color.WHITE)
			withMenuData(menu)
			withListener(getMenuListener())
		}.showIfNotVisible()
	}

	private fun getMenuListener(): BottomMenu.Listener? {
		return object : BottomMenu.Listener {
			override fun onItemSelected(index: Int, item: MenuDataItem) {
				when (index) {
					CLEAR -> {
						presenter.clearLog(type)
					}
				}
			}

			override fun onCancel() {
			}

			override fun onAdditionalClick() {
			}
		}
	}

}