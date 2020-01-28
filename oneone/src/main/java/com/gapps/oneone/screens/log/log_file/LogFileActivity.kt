package com.gapps.oneone.screens.log.log_file

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gapps.oneone.R
import com.gapps.oneone.models.log.FileModel
import com.gapps.oneone.screens.log.log_file.core.LogFileContract
import com.gapps.oneone.screens.log.log_file.core.LogFilePresenter
import com.gapps.oneone.utils.bottom_menu.BottomMenu
import com.gapps.oneone.utils.bottom_menu.MenuData
import com.gapps.oneone.utils.bottom_menu.models.MenuDataItem
import com.gapps.oneone.utils.extensions.color
import com.gapps.oneone.utils.extensions.toast
import com.gapps.oneone.utils.sendTo
import kotlinx.android.synthetic.main.activity_log_file.*

class LogFileActivity : AppCompatActivity(), LogFileContract.View {
	companion object {
		private const val PATH = "path"
		private const val FILE_NAME = "name"

		private const val DELETE = 0
		private const val SHARE_LOG_FILE = 1
		private const val SHARE_LOG_TEXT = 2

		const val CODE = 100

		fun newInstance(context: Context, file: FileModel) = Intent(context, LogFileActivity::class.java).apply {
			putExtra(FILE_NAME, file.name)
			putExtra(PATH, file.path)
		}
	}

	override var presenter = LogFilePresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_log_file)

		val path = intent.getStringExtra(PATH)

		if (path == null) {
			onFileLoadError()
			return
		}

		presenter.create(this)
		presenter.openFile(path)

		initViews()
	}

	override fun onDestroy() {
		super.onDestroy()
		presenter.destroy()
	}

	override fun onFileLoadSuccess(text: String) {
		file_text.text = text
	}

	override fun onFileLoadError() {
		toast(R.string.cant_open_file)
		finish()
	}

	override fun onLogFileDeleted(name: String) {
		val intent = Intent().apply {
			putExtra("delete", name)
		}

		setResult(Activity.RESULT_OK, intent)
		finish()
	}

	override fun onLogFilePreparedToSend(uri: Uri?) {
		sendTo(uri = uri)
	}

	private fun initViews() {
		val fileName = intent.getStringExtra(FILE_NAME) ?: getString(R.string.log_files)

		toolbar_title.text = fileName

		more.setOnClickListener { showMenu() }
		back.setOnClickListener { onBackPressed() }
	}


	private fun showMenu() {
		val menu = MenuData().apply {
			titleRes = R.string.select_action

			addMenu(R.drawable.ic_share, R.string.share_text, SHARE_LOG_TEXT)
			addMenu(R.drawable.ic_share, R.string.share_file, SHARE_LOG_FILE)
			addMenu(R.drawable.ic_delete, R.string.delete, DELETE)
		}

		BottomMenu.build {
			with(this@LogFileActivity)
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
					DELETE -> {
						intent.getStringExtra(FILE_NAME)?.let { presenter.deleteLogFile(it) }
					}

					SHARE_LOG_FILE -> {
						shareLogFile()
					}

					SHARE_LOG_TEXT -> {
						shareLogText()
					}
				}
			}

			override fun onCancel() {
			}

			override fun onAdditionalClick() {
			}
		}
	}

	private fun shareLogText() {
		sendTo(text = file_text.text.toString())
	}

	private fun shareLogFile() {
		presenter.prepareLogFileToSend()
	}

}
