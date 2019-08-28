package com.gapps.oneone.log

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.gapps.oneone.models.LogModel
import com.gapps.oneone.utils.DEBUG
import com.gapps.oneone.utils.ERROR
import com.gapps.oneone.utils.WARNING

private val TAB_TITLES = arrayOf(
		DEBUG,
		WARNING,
		ERROR
)

class SectionsPagerAdapter(fm: FragmentManager, private val logData: Map<String, List<LogModel>>?)
	: FragmentPagerAdapter(fm) {

	override fun getItem(position: Int): Fragment {
		val tag = when (position) {
			0 -> {
				DEBUG
			}
			1 -> {
				WARNING
			}
			else -> {
				ERROR
			}
		}
		val data = logData?.get(tag)

		return PlaceholderFragment.newInstance(data)
	}

	override fun getPageTitle(position: Int): CharSequence? {
		return TAB_TITLES[position]
	}

	override fun getCount(): Int {
		return TAB_TITLES.size
	}
}