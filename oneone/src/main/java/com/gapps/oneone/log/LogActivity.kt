package com.gapps.oneone.log

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gapps.oneone.OneOne
import com.gapps.oneone.R
import kotlinx.android.synthetic.main.activity_log.*

class LogActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_log)

		initPager()

		back.setOnClickListener { onBackPressed() }
	}

	private fun initPager() {
		val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, OneOne.getMessages())

		view_pager.adapter = sectionsPagerAdapter
		tabs.setupWithViewPager(view_pager)
	}
}