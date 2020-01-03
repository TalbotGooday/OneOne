package com.gapps.oneone.screens.shared_prefs.sp_files_list

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gapps.oneone.R
import com.gapps.oneone.models.shared_prefs.SharedPrefsFileModel
import com.gapps.oneone.screens.base.BaseActivity
import com.gapps.oneone.screens.shared_prefs.sp_file.SharedPrefsFileActivity
import com.gapps.oneone.screens.shared_prefs.sp_files_list.adapters.SharedPrefsAdapter
import com.gapps.oneone.screens.shared_prefs.sp_files_list.core.SharedPreferencesContract
import com.gapps.oneone.screens.shared_prefs.sp_files_list.core.SharedPreferencesPresenter
import com.gapps.oneone.utils.bottom_menu.BottomMenu
import com.gapps.oneone.utils.bottom_menu.MenuData
import com.gapps.oneone.utils.bottom_menu.models.MenuDataItem
import com.gapps.oneone.utils.extensions.addDivider
import com.gapps.oneone.utils.extensions.color
import com.gapps.oneone.utils.extensions.toColorDrawable
import kotlinx.android.synthetic.main.activity_shared_preferences.*

class SharedPreferencesActivity : BaseActivity(), SharedPreferencesContract.View {
	companion object {
		fun newInstance(context: Context) = Intent(context, SharedPreferencesActivity::class.java)
	}

	override var presenter = SharedPreferencesPresenter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_shared_preferences)

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

		files_list.apply {
			layoutManager = LinearLayoutManager(this@SharedPreferencesActivity)
			adapter = SharedPrefsAdapter(object : SharedPrefsAdapter.Listener {
				override fun onItemClick(item: SharedPrefsFileModel) {
					openSharedPrefsFile(item.name)
				}
			}).apply {
				swapData(presenter.getSharedPrefFiles())
			}

			addDivider(color(R.color.dividerColor).toColorDrawable(resources.getDimensionPixelSize(R.dimen.divider_height)), inset = resources.getDimensionPixelSize(R.dimen.divider_inset_icon))
		}
	}

	private fun showMenu() {
		val menu = MenuData().apply {
			titleRes = R.string.select_action
		}

		BottomMenu.build {
			with(this@SharedPreferencesActivity)
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
					1 -> {
					}
				}
			}

			override fun onCancel() {
			}

			override fun onAdditionalClick() {
			}
		}
	}

	private fun openSharedPrefsFile(name: String) {
		startActivity(SharedPrefsFileActivity.newInstance(this, name))
	}
}
