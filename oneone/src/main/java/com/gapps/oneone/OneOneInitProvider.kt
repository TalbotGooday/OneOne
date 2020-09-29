package com.gapps.oneone

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri

class OneOneInitProvider : ContentProvider() {
	override fun onCreate(): Boolean {
		// get the context (Application context)
		val context = context ?: return false

		OneOne.init(context)

		getLoggerBaseUrl(context)

		return true
	}

	private fun getLoggerBaseUrl(context: Context) {
		val metaData = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData
		val endpoint = metaData.getString("com.gapps.oneone.BASE_URL")

		OneOne.setLoggerBaseUrl(endpoint)
	}

	override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
		return null
	}

	override fun getType(uri: Uri): String? {
		return null
	}

	override fun insert(uri: Uri, values: ContentValues?): Uri? {
		return null
	}

	override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
		return 0
	}

	override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
		return 0
	}

	override fun attachInfo(context: Context?, providerInfo: ProviderInfo?) {
		if (providerInfo == null) {
			throw NullPointerException("YourLibraryInitProvider ProviderInfo cannot be null.")
		}
		// So if the authorities equal the library internal ones, the developer forgot to set his applicationId
		check("com.helpcrunch.library.helpcrunchinitprovider" != providerInfo.authority) {
			("Incorrect provider authority in manifest. Most likely due to a "
					+ "missing applicationId variable in application\'s build.gradle.")
		}
		super.attachInfo(context, providerInfo)
	}
}