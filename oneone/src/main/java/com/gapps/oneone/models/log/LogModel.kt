package com.gapps.oneone.models.log

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class LogModel() : Parcelable {
	@SerializedName("tag")
	var tag: String = ""
	@SerializedName("message")
	var message: String = ""
	@SerializedName("time")
	var time: Long = 0

	var type: String = ""

	constructor(parcel: Parcel) : this() {
		tag = parcel.readString() ?: ""
		type = parcel.readString() ?: ""
		message = parcel.readString() ?: ""
		time = parcel.readLong()
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(tag)
		parcel.writeString(type)
		parcel.writeString(message)
		parcel.writeLong(time)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<LogModel> {
		override fun createFromParcel(parcel: Parcel): LogModel {
			return LogModel(parcel)
		}

		override fun newArray(size: Int): Array<LogModel?> {
			return arrayOfNulls(size)
		}
	}


}