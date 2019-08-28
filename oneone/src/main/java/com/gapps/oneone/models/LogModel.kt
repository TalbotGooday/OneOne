package com.gapps.oneone.models

import android.os.Parcel
import android.os.Parcelable

class LogModel() : Parcelable {
	var tag: String = ""
	var type: String = ""
	var message: String = ""

	constructor(parcel: Parcel) : this() {
		tag = parcel.readString() ?: ""
		type = parcel.readString() ?: ""
		message = parcel.readString() ?: ""
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(tag)
		parcel.writeString(type)
		parcel.writeString(message)
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