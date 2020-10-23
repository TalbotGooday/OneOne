package com.gapps.oneone

import android.content.Context
import android.util.Log
import com.gapps.oneone.api.Api
import com.gapps.oneone.api.ServiceInterceptor
import com.gapps.oneone.api.models.LogOutModel
import com.gapps.oneone.utils.*
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class OneOneHelper : CoroutineScope {
	override val coroutineContext: CoroutineContext
		get() = Job() + Dispatchers.Main

	private var api: Api? = null
	private var gson: Gson? = null
	private var loggerUrl: String? = null

	fun checkCrashes(context: Context) {
		launch {
			val files = getLastCrashFilesText(context)
			files.entries.forEach {
				val file = it.value

				try {
					api?.logEvent(LogOutModel.fatal(file.text, file.name.substringBeforeLast(".")))
					val crashFile = File(file.path)
					if (!crashFile.isDirectory) {
						crashFile.delete()
					}
				} catch (e: Exception) {

				}
			}
		}
	}

	fun getNameFromType(context: Context?, fileName: String): String {
		context ?: return ""

		return when (fileName) {
			DEBUG -> context.getString(R.string.debug)
			WARNING -> context.getString(R.string.warning)
			ERROR -> context.getString(R.string.error)
			INFO -> context.getString(R.string.info)
			VERBOSE -> context.getString(R.string.verbose)
			else -> context.getString(R.string.log)
		}
	}

	fun setLoggerBaseUrl(baseUrl: String) {
		val okHttpClient = provideHttpClient()
		gson = gson ?: provideGson()
		loggerUrl = baseUrl
		api = provideRetrofit(baseUrl, requireNotNull(gson), okHttpClient)
	}

	fun logWeb(type: String, tag: String? = null, message: Any?) {
		val gson = gson ?: return

		if (isActive) {
			sendLogEvent(LogOutModel.create(gson, type, message, tag))
		} else {
			try {
				OneOne.writeToLogFile(
						text = if (message is String) {
							message
						} else {
							gson.toJson(message)
						},
						isCrash = true
				)
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
	}

	private fun sendLogEvent(event: LogOutModel) {
		launch {
			try {
				api?.logEvent(event)
			} catch (e: Exception) {
				Log.e("OneOne", "Log server not responding: $loggerUrl")
			}
		}

	}

	private fun provideHttpClient(
	): OkHttpClient {
		val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
			level = if (BuildConfig.DEBUG) {
				HttpLoggingInterceptor.Level.BODY
			} else {
				HttpLoggingInterceptor.Level.NONE
			}
		}

		return OkHttpClient.Builder()
				.addInterceptor(httpLoggingInterceptor)
				.addInterceptor(ServiceInterceptor())
				.connectTimeout(15, TimeUnit.MINUTES)
				.readTimeout(15, TimeUnit.MINUTES)
				.writeTimeout(15, TimeUnit.MINUTES)
				.build()
	}

	private fun provideGson(): Gson {
		return GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
				.create()
	}

	private fun provideRetrofit(baseUrl: String, factory: Gson, client: OkHttpClient): Api {
		val retrofit = Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create(factory))
				.client(client)
				.build()

		return retrofit.create(Api::class.java)
	}
}
