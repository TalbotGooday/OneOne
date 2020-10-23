package com.gapps.oneone.api.models

import com.gapps.oneone.OneOne.DEFAULT_TAG
import com.gapps.oneone.utils.FATAL
import com.gapps.oneone.utils.extensions.toUTC
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class LogOutModel() {
	companion object {
		fun create(gson: Gson, type: String, message: Any?, tag: String? = DEFAULT_TAG): LogOutModel {
			val toJson = if (message is String) {
				message
			} else {
				gson.toJson(message)
			}

			return LogOutModel(tag ?: DEFAULT_TAG, toJson, System.currentTimeMillis().toUTC(), type)
		}

		fun fatal(message: String, time: String): LogOutModel {
			return LogOutModel(DEFAULT_TAG, message, time, FATAL)
		}
	}

	@SerializedName("tag")
	var tag: String = ""

	@SerializedName("message")
	var message: String = ""

	@SerializedName("time")
	var time: String = ""

	@SerializedName("type")
	var type: String = ""

	constructor(tag: String, message: String, time: String, type: String) : this() {
		this.tag = tag
		this.message = message
		this.time = time
		this.type = type
	}


}