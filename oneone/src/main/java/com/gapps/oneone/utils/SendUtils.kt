package com.gapps.oneone.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import com.gapps.oneone.BuildConfig
import java.io.File
import java.lang.StringBuilder

fun sendEmail(context: Context, email: String, type: String? = null) {
	val filesNames = mutableListOf<String>()

	when (type) {
		null -> {
			filesNames.add(FILE_LOG_D)
			filesNames.add(FILE_LOG_W)
			filesNames.add(FILE_LOG_E)
		}
		DEBUG -> {
			filesNames.add(FILE_LOG_D)
		}
		WARNING -> {
			filesNames.add(FILE_LOG_W)
		}
		ERROR -> {
			filesNames.add(FILE_LOG_E)
		}
	}

	val filesUri = mutableListOf<Uri>()

	filesNames.forEach {
		val file = File(context.cacheDir, it)
		val fileExt = File(context.externalCacheDir, it)

		file.copyTo(fileExt, true)

		filesUri.add(Uri.fromFile(fileExt))
	}

	val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)

	emailIntent.type = "vnd.android.cursor.dir/email"

	val to = arrayOf(email)

	emailIntent.putExtra(Intent.EXTRA_EMAIL, to)

	emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(filesUri))

	emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Log info")

	emailIntent.putExtra(Intent.EXTRA_TEXT, getAppInfo())

	context.startActivity(Intent.createChooser(emailIntent, "Send email...")
			.apply {
				addFlags(FLAG_ACTIVITY_NEW_TASK)
			}, null)
}

fun getAppInfo(): String {
	val dataBuilder = StringBuilder().apply {
		append("Version Name: ").append(BuildConfig.VERSION_NAME).append("\n")
		append("Version Code: ").append(BuildConfig.VERSION_CODE).append("\n")
		append("Android Version: ").append(Build.VERSION.RELEASE).append("\n")
		append("Phone: ").append(Build.MANUFACTURER).append(" ").append(Build.MODEL).append("\n")
	}

	return dataBuilder.toString()
}
