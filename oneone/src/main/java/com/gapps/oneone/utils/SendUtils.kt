package com.gapps.oneone.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.gapps.oneone.R
import com.gapps.oneone.utils.extensions.getAppInfoString
import java.io.File


fun sendEmail(context: Context, email: String? = null, type: String? = null) {
	val filesNames = mutableListOf<String>()

	when (type) {
		null -> {
			filesNames.add(FILE_LOG_D)
			filesNames.add(FILE_LOG_W)
			filesNames.add(FILE_LOG_E)
			filesNames.add(FILE_LOG_I)
			filesNames.add(FILE_LOG_V)
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
		INFO -> {
			filesNames.add(FILE_LOG_I)
		}
		VERBOSE -> {
			filesNames.add(FILE_LOG_V)
		}
	}

	val filesUri = mutableListOf<Uri>()

	filesNames.forEach {
		val file = File(context.cacheDir, getLogFilePath(it))

		if (file.exists()) {
			val fileExt = file.copyFileToExternalCache(context, it)

			filesUri.add(Uri.fromFile(fileExt))
		}
	}

	val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)

	emailIntent.type = "vnd.android.cursor.dir/email"

	email?.let {
		val to = arrayOf(it)

		emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
	}

	emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(filesUri))

	emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Log info")

	emailIntent.putExtra(Intent.EXTRA_TEXT, context.getAppInfoString())

	if (emailIntent.resolveActivity(context.packageManager) != null) {
		try {
			context.startActivity(Intent.createChooser(emailIntent, "Send email...")
					.apply {
						addFlags(FLAG_ACTIVITY_NEW_TASK)
					}, null)
		}catch (e: Exception){
			e.printStackTrace()
		}
	}
}

fun File.copyFileToExternalCache(context: Context, fileName: String? = null): File {
	val fileExt = File(context.externalCacheDir, getLogFilePath(fileName ?: this.name))

	this.copyTo(fileExt, true)
	return fileExt
}

fun Context.sendTo(uri: Uri? = null, text: String? = null, type: String = "text/*") {

	val sendIntent = Intent(Intent.ACTION_SEND)
	sendIntent.type = type
	text?.run { sendIntent.putExtra(Intent.EXTRA_TEXT, text) }
	uri?.run { sendIntent.putExtra(Intent.EXTRA_STREAM, this) }
	val title = resources.getString(R.string.send_intent_title)
	val chooser = Intent.createChooser(sendIntent, title)

	if (sendIntent.resolveActivity(this.packageManager) != null) {
		startActivity(Intent.createChooser(chooser, title)
				.apply {
					addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				}, null)
	}
}

