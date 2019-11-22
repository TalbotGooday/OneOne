package com.gapps.oneone.models.log

import android.os.Parcel
import android.os.Parcelable

class LogFileModel() : Parcelable {
	var tag: String = ""
	var type: String = ""
	var name: String = ""

	constructor(parcel: Parcel) : this() {
		tag = parcel.readString() ?: ""
		type = parcel.readString() ?: ""
		name = parcel.readString() ?: ""
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(tag)
		parcel.writeString(type)
		parcel.writeString(name)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<LogFileModel> {
		override fun createFromParcel(parcel: Parcel): LogFileModel {
			return LogFileModel(parcel)
		}

		override fun newArray(size: Int): Array<LogFileModel?> {
			return arrayOfNulls(size)
		}
	}

}