package com.gapps.oneone.api

import com.gapps.oneone.api.models.LogOutModel
import com.gapps.oneone.api.models.ResponseLog
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

	@POST("log")
	suspend fun logEvent(@Body logOutModel: LogOutModel): ResponseLog
}