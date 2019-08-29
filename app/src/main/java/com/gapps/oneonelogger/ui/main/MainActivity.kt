package com.gapps.oneonelogger.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.gapps.oneone.OneOne
import com.gapps.oneonelogger.R
import com.gapps.oneone.log.LogActivity

import kotlinx.android.synthetic.main.activity_main.*
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		OneOne.d("MainActivity", message = "Started MainActivity")

		fab.setOnClickListener { view ->
			OneOne.d("MainActivity", "send letter")
			OneOne.sendLog("alexey.mostovoy.w@gmail.com")

		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.menu_main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return when (item.itemId) {
			R.id.action_see_log -> {
				OneOne.w("MainActivity","action_see_log")
				startActivity(Intent(this, LogActivity::class.java))
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}
}
