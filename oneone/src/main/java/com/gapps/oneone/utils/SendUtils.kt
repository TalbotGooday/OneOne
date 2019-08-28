package com.gapps.oneone.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.core.content.ContextCompat
import java.io.File

fun sendEmail(context: Context, email: String, fileName: String){

	val file = File(context.cacheDir, fileName)
	val fileExt = File(context.externalCacheDir, fileName)

	file.copyTo(fileExt, true)

	val path = Uri.fromFile(fileExt)
	val emailIntent = Intent(Intent.ACTION_SEND)

	emailIntent.type = "vnd.android.cursor.dir/email"

	val to = arrayOf(email)

	emailIntent.putExtra(Intent.EXTRA_EMAIL, to)

	emailIntent.putExtra(Intent.EXTRA_STREAM, path)

	emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Log info")

	context.startActivity(Intent.createChooser(emailIntent, "Send email...")
			.apply {
				addFlags(FLAG_ACTIVITY_NEW_TASK)
			}, null)
}