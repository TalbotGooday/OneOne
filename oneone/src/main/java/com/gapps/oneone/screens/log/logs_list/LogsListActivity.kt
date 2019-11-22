package com.gapps.oneone.screens.log.logs_list

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gapps.oneone.OneOne
import com.gapps.oneone.R
import com.gapps.oneone.models.log.LogFileModel
import com.gapps.oneone.screens.base.BaseActivity
import com.gapps.oneone.screens.log.log_messages.LogMessagesActivity
import com.gapps.oneone.screens.log.logs_list.adapters.LogsListAdapter
import com.gapps.oneone.screens.log.logs_list.core.LogsListContract
import com.gapps.oneone.screens.log.logs_list.core.LogsListPresenter
import com.gapps.oneone.utils.bottom_menu.BottomMenu
import com.gapps.oneone.utils.bottom_menu.MenuData
import com.gapps.oneone.utils.bottom_menu.models.MenuDataItem
import com.gapps.oneone.utils.extensions.*
import kotlinx.android.synthetic.main.activity_logs_list.*

class LogsListActivity : BaseActivity(), LogsListContract.View {
	companion object {
		const val SEND_REPORT = 0
		const val CLEAR_ALL_DATA = 1

		fun newInstance(context: Context) = Intent(context, LogsListActivity::class.java)
	}

	override var presenter = LogsListPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_logs_list)

		presenter.create(this)

		initViews()
	}

	private fun initViews() {
		back.setOnClickListener { onBackPressed() }
		more.setOnClickListener { showMenu() }

		logs_list.apply {
			layoutManager = LinearLayoutManager(this@LogsListActivity)
			adapter = LogsListAdapter(object : LogsListAdapter.Listener {
				override fun onItemClick(item: LogFileModel) {
					openLogScreen(item)
				}
			}).apply {
				swapData(presenter.getLogsList())
			}

			addDivider(color(R.color.dividerColor).toColorDrawable(resources.getDimensionPixelSize(R.dimen.divider_height)), inset = resources.getDimensionPixelSize(R.dimen.divider_inset))
		}
	}

	private fun openLogScreen(item: LogFileModel) {
		startActivity(LogMessagesActivity.newInstance(this, item))
	}

	private fun showMenu() {
		val menu = MenuData().apply {
			titleRes = R.string.select_action
			addMenu(R.drawable.ic_bug_report, R.string.sent_report, SEND_REPORT)
			addMenu(R.drawable.ic_delete, R.string.clear_all_log_data, CLEAR_ALL_DATA)
		}

		BottomMenu.build {
			with(this@LogsListActivity)
			withBackgroundColor(color(R.color.colorPrimaryLog))
			withMainColor(color(R.color.colorPrimaryDarkLog))
			withAccentColor(Color.WHITE)
			withMenuData(menu)
			withListener(getMenuListener())
		}.show()
	}

	private fun getMenuListener(): BottomMenu.Listener? {
		return object : BottomMenu.Listener {
			override fun onItemSelected(index: Int, item: MenuDataItem) {
				when (index) {
					SEND_REPORT -> {
						OneOne.sendLog()
					}

					CLEAR_ALL_DATA -> {
						OneOne.clearAll()
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
