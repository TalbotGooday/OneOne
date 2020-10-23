package com.gapps.oneonelogger.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.gapps.oneone.OneOne
import com.gapps.oneone.utils.extensions.printOneOneLogFile
import com.gapps.oneonelogger.R

import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.Exception
import kotlin.RuntimeException

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		val sp = this.getSharedPreferences("test_sp_main", Context.MODE_PRIVATE)
		sp.edit().putString("test", "test").apply()
		sp.edit().putBoolean("test_bool", false).apply()
		sp.edit().putBoolean("test_bool1", true).apply()
		sp.edit().putInt("test_int", 0).apply()
		sp.edit().putLong("test_long", 1231231231231230).apply()
		sp.edit().putFloat("test_float", 99.99f).apply()

		fab.setOnClickListener {
			OneOne.d("MainActivity", "send letter")
			OneOne.sendLog("alexey.mostovoy.w@gmail.com")
		}

		OneOne.i("MainActivity", "MainActivity opened")

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
//				startActivity(Intent(this, LogActivity::class.java))
				OneOne.openLog(this)
				OneOne.d("A", "open")
				true
			}
			R.id.action_write_log ->{
				throw RuntimeException("AAAAAAAAA")
				try {
					File(cacheDir, "asdfasdf/asdf/asdfasdfasdf").writeText("sasdf")

				}catch (e: Exception){
					e.printOneOneLogFile()
				}

				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}
}
