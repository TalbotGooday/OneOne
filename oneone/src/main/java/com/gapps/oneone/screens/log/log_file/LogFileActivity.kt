package com.gapps.oneone.screens.log.log_file

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gapps.oneone.R
import com.gapps.oneone.models.log.FileModel
import com.gapps.oneone.screens.log.log_file.core.LogFileContract
import com.gapps.oneone.screens.log.log_file.core.LogFilePresenter
import com.gapps.oneone.utils.extensions.toast
import kotlinx.android.synthetic.main.activity_log_file.*

class LogFileActivity : AppCompatActivity(), LogFileContract.View {
	companion object {
		private const val PATH = "path"
		private const val FILE_NAME = "name"

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

	private fun initViews() {
		val fileName = intent.getStringExtra(FILE_NAME) ?: getString(R.string.log_files)

		toolbar_title.text = fileName

		more.setOnClickListener { openFileMenu() }
		back.setOnClickListener { onBackPressed() }
	}

	private fun openFileMenu() {

	}
}
