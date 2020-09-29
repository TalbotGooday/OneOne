package com.gapps.oneone

import android.content.Context
import com.gapps.oneone.api.Api
import com.gapps.oneone.api.ServiceInterceptor
import com.gapps.oneone.api.models.LogOutModel
import com.gapps.oneone.utils.*
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class OneOneHelper : CoroutineScope {
	override val coroutineContext: CoroutineContext
		get() = SupervisorJob() + Dispatchers.Main

	private var api: Api? = null
	private var gson: Gson? = null

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
		gson = provideGson()

		api = provideRetrofit(baseUrl, requireNotNull(gson), okHttpClient)
	}

	fun logWeb(type: String, tag: String? = null, message: Any?) {
		val gson = gson ?: return

		sendLogEvent(LogOutModel.create(gson, type, message, tag))
	}

	private fun sendLogEvent(event: LogOutModel) {
		launch {
			try {
				api?.logEvent(event)
			} catch (e: Exception) {
				e.printStackTrace()
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
