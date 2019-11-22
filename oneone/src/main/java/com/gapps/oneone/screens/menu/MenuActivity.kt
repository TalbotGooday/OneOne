package com.gapps.oneone.screens.menu

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.gapps.oneone.R
import com.gapps.oneone.screens.base.BaseActivity
import com.gapps.oneone.screens.log.logs_list.LogsListActivity
import com.gapps.oneone.screens.menu.core.MenuContract
import com.gapps.oneone.screens.menu.core.MenuPresenter
import com.gapps.oneone.screens.shared_prefs.sp_files_list.SharedPreferencesActivity
import com.gapps.oneone.utils.bottom_menu.BottomMenu
import com.gapps.oneone.utils.bottom_menu.MenuData
import com.gapps.oneone.utils.bottom_menu.models.MenuDataItem
import com.gapps.oneone.utils.extensions.*
import com.gapps.oneone.utils.sendTo
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : BaseActivity(), MenuContract.View {
	companion object {
		private const val GET_APP_INFO = 0

		fun newInstance(context: Context) = Intent(context, MenuActivity::class.java)
	}

	override var presenter = MenuPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_menu)

		initViews()
	}

	private fun initViews() {
		back.setOnClickListener { onBackPressed() }
		more.setOnClickListener { showMenu() }

		log_container.setOnClickListener { openLogs() }
		shared_prefs_container.setOnClickListener { openSharedPreferences() }
	}

	private fun showMenu() {
		val menu = MenuData().apply {
			titleRes = R.string.select_action
			addMenu(R.drawable.ic_phone, R.string.app_info, GET_APP_INFO)
		}

		BottomMenu.build {
			with(this@MenuActivity)
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
					GET_APP_INFO -> {
						showAppInfo()
					}
				}
			}

			override fun onCancel() {
			}

			override fun onAdditionalClick() {
			}
		}
	}

	private fun showAppInfo() {
		val menu = MenuData().apply {
			titleRes = R.string.app_info
			buttonRes = R.string.send
			buttonIcon = R.drawable.ic_mail
		}

		getAppInfoMapLocalized().entries.forEach {
			menu.addMenu("${it.key}: ${it.value}")
		}

		BottomMenu.build {
			with(this@MenuActivity)
			withBackgroundColor(color(R.color.colorPrimaryLog))
			withMainColor(color(R.color.colorPrimaryDarkLog))
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

	private fun openSharedPreferences() {
		startActivity(SharedPreferencesActivity.newInstance(this))
	}

}
