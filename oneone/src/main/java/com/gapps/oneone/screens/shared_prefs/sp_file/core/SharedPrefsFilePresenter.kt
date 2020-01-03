package com.gapps.oneone.screens.shared_prefs.sp_file.core

import android.content.Context
import android.content.SharedPreferences
import com.gapps.oneone.models.shared_prefs.SharedPrefEntry
import java.util.*

class SharedPrefsFilePresenter : SharedPrefsFileContract.Presenter {
	override lateinit var view: SharedPrefsFileContract.View
	private lateinit var context: Context

	private var sp: SharedPreferences? = null

	override fun create(context: Context) {
		this.context = context

		if (context is SharedPrefsFileContract.View) {
			this.view = context
		}
	}

	override fun destroy() {
	}

	fun getSharedPrefEntries(fileName: String): List<SharedPrefEntry> {
		sp = context.getSharedPreferences(fileName.substringBeforeLast("."), Context.MODE_PRIVATE)

		return sp?.all?.map {
			SharedPrefEntry().apply {
				this.key = it.key
				this.value = it.value.toString()
				this.type = it.value?.javaClass?.name?.substringAfterLast(".") ?: ""
			}
		} ?: emptyList()
	}

	override fun changeSpEntry(type: String, key: String?, value: String?) {
		key ?: return

		when (type.toLowerCase(Locale.ENGLISH)) {
			"string" -> saveStringEntry(key, value)
			"long" -> editLongEntry(key, value)
			"integer" -> editIntEntry(key, value)
			"float" -> editFloatEntry(key, value)
			"boolean" -> editBooleanEntry(key, value)
		}

		view.onSpEntryUpdated(key, value)
	}

	private fun editBooleanEntry(key: String, value: String?) {
		val sp = sp ?: return

		val bool = value?.toBoolean() ?: false

		sp.edit().putBoolean(key, bool).apply()
	}

	private fun editFloatEntry(key: String, value: String?) {
		val sp = sp ?: return

		val fl = value?.toFloatOrNull() ?: return

		sp.edit().putFloat(key, fl).apply()
	}

	private fun editIntEntry(key: String, value: String?) {
		val sp = sp ?: return

		val int = value?.toIntOrNull() ?: return

		sp.edit().putInt(key, int).apply()
	}

	private fun editLongEntry(key: String, value: String?) {
		val sp = sp ?: return

		val long = value?.toLongOrNull() ?: return

		sp.edit().putLong(key, long).apply()
	}

	private fun saveStringEntry(key: String, value: String?) {
		val sp = sp ?: return

		sp.edit().putString(key, value).apply()
	}
}
