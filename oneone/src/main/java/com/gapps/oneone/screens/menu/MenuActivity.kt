package com.gapps.oneone.screens.menu

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gapps.oneone.R
import com.gapps.oneone.models.menu.MenuItem
import com.gapps.oneone.screens.base.BaseActivity
import com.gapps.oneone.screens.log.log_files.LogFilesListActivity
import com.gapps.oneone.screens.log.logs_list.LogsListActivity
import com.gapps.oneone.screens.menu.adapters.MenuAdapter
import com.gapps.oneone.screens.menu.core.MenuContract
import com.gapps.oneone.screens.menu.core.MenuPresenter
import com.gapps.oneone.screens.shared_prefs.sp_files_list.SharedPreferencesActivity
import com.gapps.oneone.utils.bottom_menu.BottomMenu
import com.gapps.oneone.utils.bottom_menu.MenuData
import com.gapps.oneone.utils.bottom_menu.models.MenuDataItem
import com.gapps.oneone.utils.extensions.*
import com.gapps.oneone.utils.sendTo
import com.gapps.oneone.utils.views.dialog.InputDialog
import kotlinx.android.synthetic.main.activity_oo_menu.*

internal class MenuActivity : BaseActivity(), MenuContract.View {
	companion object {
		private const val GET_APP_INFO = 0
		private const val LOGGER_URL = 1
		private const val ID_LOG = 0
		private const val ID_LOG_FILES = 1
		private const val ID_LOGCAT = 2
		private const val ID_SP = 3
		private const val ID_DATABASES = 4

		fun newInstance(context: Context) = Intent(context, MenuActivity::class.java)
	}

	override var presenter = MenuPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_oo_menu)

		initViews()
	}

	override fun onDestroy() {
		super.onDestroy()
		presenter.destroy()
	}

	private fun initViews() {
		back.setOnClickListener { onBackPressed() }
		more.setOnClickListener { showMenu() }

		initMenuList()
	}

	private fun initMenuList() {
		val menuData = listOf(
				MenuItem(ID_LOG, R.string.log, R.drawable.ic_oo_console),
				MenuItem(ID_LOG_FILES, R.string.log_files, R.drawable.ic_oo_console_file),
//				MenuItem(ID_LOGCAT, R.string.log_logcat, R.drawable.ic_oo_notes),
				MenuItem(ID_SP, R.string.oo_shared_prefs, R.drawable.ic_oo_file),
//				MenuItem(ID_DATABASES, R.string.oo_databases, R.drawable.ic_oo_storage)
		)

		menu_list.apply {
			layoutManager = LinearLayoutManager(this@MenuActivity)
			adapter = MenuAdapter(object : MenuAdapter.Listener {
				override fun onItemClick(item: MenuItem) {
					when (item.id) {
						ID_LOG -> openLogs()
						ID_LOG_FILES -> openLogFiles()
						ID_LOGCAT -> openLogcat()
						ID_SP -> openSharedPreferences()
						ID_DATABASES -> openDatabases()
					}
				}
			}).apply {
				swapData(menuData)
			}

			addDivider(color(R.color.colorDividerOOColor).toColorDrawable(resources.getDimensionPixelSize(R.dimen.divider_height)), inset = resources.getDimensionPixelSize(R.dimen.divider_inset_icon))
		}
	}

	private fun openLogcat() {

	}

	private fun openDatabases() {

	}

	private fun showMenu() {
		val menu = MenuData().apply {
			titleRes = R.string.select_action
			addMenu(R.drawable.ic_oo_phone, R.string.app_info, GET_APP_INFO)
			addMenu(R.drawable.ic_oo_cloud_upload, R.string.web_logger_url, LOGGER_URL)
		}

		BottomMenu.build {
			with(this@MenuActivity)
			withBackgroundColor(color(R.color.colorOOPrimaryLog))
			withMainColor(color(R.color.colorOOPrimaryDarkLog))
			withAccentColor(Color.WHITE)
			withMenuData(menu)
			withListener(getMenuListener())
		}.show()
	}

	private fun getMenuListener(): BottomMenu.Listener? {
		return object : BottomMenu.Listener {
			override fun onItemSelected(index: Int, item: MenuDataItem) {
				when (index) {
					GET_APP_INFO -> {
						showAppInfo()
					}
					LOGGER_URL -> {
						showLoggerUrlDialog()
					}
				}
			}

			override fun onCancel() {
			}

			override fun onAdditionalClick() {
			}
		}
	}

	private fun showLoggerUrlDialog() {
		InputDialog(
				this,
				getString(R.string.web_logger_url),
				getString(android.R.string.ok),
				getString(android.R.string.cancel),
				::onUrlChanged
		).show()
	}

	private fun onUrlChanged(newUrl: String) {
		presenter.saveNewLoggerUrl(newUrl)
	}

	private fun showAppInfo() {
		val menu = MenuData().apply {
			titleRes = R.string.app_info
			buttonRes = R.string.send
			buttonIcon = R.drawable.ic_oo_mail
		}

		getAppInfoMapLocalized().entries.forEach {
			menu.addMenu("${it.key}: ${it.value}")
		}

		BottomMenu.build {
			with(this@MenuActivity)
			withBackgroundColor(color(R.color.colorOOPrimaryLog))
			withMainColor(color(R.color.colorOOPrimaryDarkLog))
			withAccentColor(Color.WHITE)
			withMenuData(menu)
			withListener(object : BottomMenu.Listener {
				override fun onItemSelected(index: Int, item: MenuDataItem) {
					val titleString = item.titleString ?: return

					copyToClipboard(titleString) {
						toast(R.string.copied)
					}
				}

				override fun onCancel() {
				}

				override fun onAdditionalClick() {
					sendTo(text = getAppInfoString())
				}
			})
		}.show()
	}

	private fun openLogs() {
		startActivity(LogsListActivity.newInstance(this))
	}

	private fun openLogFiles() {
		startActivity(LogFilesListActivity.newInstance(this))
	}

	private fun openSharedPreferences() {
		startActivity(SharedPreferencesActivity.newInstance(this))
	}

}
