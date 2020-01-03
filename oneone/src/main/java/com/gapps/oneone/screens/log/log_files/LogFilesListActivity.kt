package com.gapps.oneone.screens.log.log_files

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gapps.oneone.OneOne
import com.gapps.oneone.R
import com.gapps.oneone.models.log.FileModel
import com.gapps.oneone.screens.base.BaseActivity
import com.gapps.oneone.screens.log.log_file.LogFileActivity
import com.gapps.oneone.screens.log.log_files.adapters.LogFilesListAdapter
import com.gapps.oneone.screens.log.log_files.core.LogFilesListContract
import com.gapps.oneone.screens.log.log_files.core.LogFilesListPresenter
import com.gapps.oneone.utils.bottom_menu.BottomMenu
import com.gapps.oneone.utils.bottom_menu.MenuData
import com.gapps.oneone.utils.bottom_menu.models.MenuDataItem
import com.gapps.oneone.utils.extensions.addDivider
import com.gapps.oneone.utils.extensions.color
import com.gapps.oneone.utils.extensions.toColorDrawable
import kotlinx.android.synthetic.main.activity_log_files_list.*

class LogFilesListActivity : BaseActivity(), LogFilesListContract.View {
	companion object {
		const val SEND_REPORT = 0
		const val CLEAR_ALL_DATA = 1

		fun newInstance(context: Context) = Intent(context, LogFilesListActivity::class.java)
	}

	override var presenter = LogFilesListPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_log_files_list)

		presenter.create(this)

		initViews()
	}

	override fun onDestroy() {
		super.onDestroy()
		presenter.destroy()
	}

	private fun initViews() {
		back.setOnClickListener { onBackPressed() }
		more.setOnClickListener { showMenu() }

		logs_list.apply {
			layoutManager = LinearLayoutManager(this@LogFilesListActivity)
			adapter = LogFilesListAdapter(object : LogFilesListAdapter.Listener {
				override fun onItemClick(item: FileModel) {
					openLogFileScreen(item)
				}
			}).apply {
				swapData(presenter.getLogFilesList())
			}

			addDivider(color(R.color.dividerColor).toColorDrawable(resources.getDimensionPixelSize(R.dimen.divider_height)), inset = resources.getDimensionPixelSize(R.dimen.divider_inset))
		}
	}

	private fun openLogFileScreen(item: FileModel) {
		startActivity(LogFileActivity.newInstance(this, item))
	}

	private fun showMenu() {
		val menu = MenuData().apply {
			titleRes = R.string.select_action
			addMenu(R.drawable.ic_bug_report, R.string.sent_report, SEND_REPORT)
			addMenu(R.drawable.ic_delete, R.string.clear_all_log_data, CLEAR_ALL_DATA)
		}

		BottomMenu.build {
			with(this@LogFilesListActivity)
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
