package com.gapps.oneone.screens.log.log_files

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.gapps.oneone.utils.extensions.visibleOrGone
import com.gapps.oneone.utils.sendTo
import kotlinx.android.synthetic.main.activity_log_files_list.*

class LogFilesListActivity : BaseActivity(), LogFilesListContract.View {
	companion object {
		const val CLEAR_ALL_DATA = 0

		fun newInstance(context: Context) = Intent(context, LogFilesListActivity::class.java)
	}

	override var presenter = LogFilesListPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_log_files_list)

		presenter.create(this)

		initViews()

		presenter.getLogFilesList()
	}

	override fun onDestroy() {
		super.onDestroy()
		presenter.destroy()
	}

	override fun onGotLogFilesList(data: List<FileModel>) {
		(logs_list.adapter as LogFilesListAdapter).swapData(data)

		messages_placeholder.visibleOrGone(data.isEmpty())
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (resultCode == Activity.RESULT_OK && data != null) {
			if (requestCode == LogFileActivity.CODE) {
				val deletedFileName = data.getStringExtra("delete") ?: return

				removeLogFileFromList(deletedFileName)
			}
		}
	}

	override fun onLogFilesDeleted() {
		(logs_list.adapter as LogFilesListAdapter).removeAll()

		messages_placeholder.visibleOrGone(true)
	}

	private fun removeLogFileFromList(deletedFileName: String) {
		(logs_list.adapter as LogFilesListAdapter).remove(deletedFileName)

		messages_placeholder.visibleOrGone((logs_list.adapter?.itemCount ?: 0) <= 0)
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
			})

			addDivider(color(R.color.dividerColor).toColorDrawable(resources.getDimensionPixelSize(R.dimen.divider_height)), inset = resources.getDimensionPixelSize(R.dimen.divider_inset))
		}
	}

	private fun openLogFileScreen(item: FileModel) {
		startActivityForResult(LogFileActivity.newInstance(this, item), LogFileActivity.CODE)
	}

	private fun showMenu() {
		val menu = MenuData().apply {
			titleRes = R.string.select_action
			addMenu(R.drawable.ic_delete, R.string.clear_all_log_files_data, CLEAR_ALL_DATA)
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
					CLEAR_ALL_DATA -> {
						presenter.clearAllLogFiles()
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
