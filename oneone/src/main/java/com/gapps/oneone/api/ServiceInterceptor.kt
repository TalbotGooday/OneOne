package com.gapps.oneone.api

import com.gapps.oneone.OneOne
import com.gapps.oneone.utils.extensions.PHONE
import com.gapps.oneone.utils.extensions.VERSION_ANDROID
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.io.IOException

class ServiceInterceptor : Interceptor {
	@Throws(IOException::class)
	override fun intercept(chain: Chain): Response {
		var request = chain.request()

		val phone = OneOne.appInfo[PHONE]
		val androidVersion = OneOne.appInfo[VERSION_ANDROID]

		request = request.newBuilder()
				.addHeader("User-Agent", "Phone: $phone, Android: $androidVersion") //Do not remove or edit
				.addHeader("Connection", "close")
				.build()

		return chain.proceed(request)
	}
}