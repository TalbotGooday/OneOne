package com.gapps.oneone.screens.shared_prefs.sp_file

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gapps.oneone.R
import com.gapps.oneone.models.shared_prefs.SharedPrefEntry
import com.gapps.oneone.screens.base.BaseActivity
import com.gapps.oneone.screens.shared_prefs.sp_file.adapters.SharedPrefEntriesAdapter
import com.gapps.oneone.screens.shared_prefs.sp_file.core.SharedPrefsFileContract
import com.gapps.oneone.screens.shared_prefs.sp_file.core.SharedPrefsFilePresenter
import com.gapps.oneone.utils.bottom_menu.BottomMenu
import com.gapps.oneone.utils.bottom_menu.MenuData
import com.gapps.oneone.utils.bottom_menu.models.MenuDataItem
import com.gapps.oneone.utils.extensions.*
import com.gapps.oneone.utils.views.dialog.SpEditorDialog
import kotlinx.android.synthetic.main.activity_shared_prefs_file.*

class SharedPrefsFileActivity : BaseActivity(), SharedPrefsFileContract.View {
	companion object {
		private const val FILE_NAME = "file_name"

		private const val MENU_EDIT = 0
		private const val MENU_COPY_KEY = 1
		private const val MENU_COPY_VALUE = 2
		private const val MENU_COPY_AS_PAIR = 3
		private const val MENU_COPY_AS_JSON = 4

		private const val MENU_SHARE_FILE = 0
		fun newInstance(context: Context, fileName: String) = Intent(context, SharedPrefsFileActivity::class.java).apply {
			putExtra(FILE_NAME, fileName)
		}
	}

	override var presenter = SharedPrefsFilePresenter()

	private var fileName = ""

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_shared_prefs_file)

		presenter.create(this)

		fileName = intent.getStringExtra(FILE_NAME) ?: ""

		initViews()
	}

	override fun onSpEntryUpdated(key: String, value: String?) {
		(entries_list.adapter as SharedPrefEntriesAdapter).updateByKey(key, value)
	}

	override fun onDestroy() {
		super.onDestroy()
		presenter.destroy()
	}

	private fun initViews() {
		toolbar_title.text = fileName
		back.setOnClickListener { onBackPressed() }
//		more.setOnClickListener { showEntryMenu() }

		entries_list.apply {
			layoutManager = LinearLayoutManager(this@SharedPrefsFileActivity)
			adapter = SharedPrefEntriesAdapter(object : SharedPrefEntriesAdapter.Listener {
				override fun onItemClick(item: SharedPrefEntry) {
					showEntryMenu(item)
				}
			}).apply {
				swapData(presenter.getSharedPrefEntries(fileName))
			}

			addDivider(color(R.color.dividerColor).toColorDrawable(resources.getDimensionPixelSize(R.dimen.divider_height)), inset = 0)
		}
	}

	private fun showEntryMenu(item: SharedPrefEntry) {
		val menu = MenuData().apply {
			this.title = item.key
			addMenu(R.drawable.ic_edit, R.string.edit, MENU_EDIT)
			addMenu(R.drawable.ic_content_copy, R.string.copy_key, MENU_COPY_KEY)
			addMenu(R.drawable.ic_content_copy, R.string.copy_value, MENU_COPY_VALUE)
			addMenu(R.drawable.ic_content_copy, R.string.copy_as_pair, MENU_COPY_AS_PAIR)
			addMenu(R.drawable.ic_content_copy, R.string.copy_as_json, MENU_COPY_AS_JSON)
		}

		BottomMenu.build {
			with(this@SharedPrefsFileActivity)
			withBackgroundColor(color(R.color.colorPrimaryLog))
			withMainColor(color(R.color.colorPrimaryDarkLog))
			withAccentColor(Color.WHITE)
			withMenuData(menu)
			withListener(getMenuListener(item))
		}.show()
	}

	private fun getMenuListener(entry: SharedPrefEntry): BottomMenu.Listener? {
		return object : BottomMenu.Listener {
			override fun onItemSelected(index: Int, item: MenuDataItem) {
				when (index) {
					MENU_EDIT -> {
						editEntry(entry)
					}
					MENU_COPY_KEY -> {
						copy(entry.key)
					}
					MENU_COPY_VALUE -> {
						copy(entry.value)
					}
					MENU_COPY_AS_PAIR -> {
						copy("${entry.key}=${entry.value}")
					}
					MENU_COPY_AS_JSON -> {
						copy("{\"key\":\"${entry.key}\", \"value\":\"${entry.value}\", \"type\":\"${entry.type}\"}")
					}
				}
			}

			override fun onCancel() {
			}

			override fun onAdditionalClick() {
			}
		}
	}

	private fun editEntry(entry: SharedPrefEntry) {
		SpEditorDialog(
				this,
				entry.key,
				entry.type,
				entry.value,
				getString(android.R.string.ok),
				getString(android.R.string.cancel),
				::onEntryChangeRequested
		).show()
	}

	private fun onEntryChangeRequested(type: String?, key: String?, value: String?) {
		type ?: return

		presenter.changeSpEntry(type, key, value)
	}

	private fun copy(value: String) {
		copyToClipboard(value) {
			toast(R.string.copied)
		}
	}
}
