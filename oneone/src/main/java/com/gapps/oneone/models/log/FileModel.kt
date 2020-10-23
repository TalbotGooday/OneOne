package com.gapps.oneone.models.log

import android.os.Parcel
import android.os.Parcelable

class FileModel() : Parcelable {
	var name: String = ""
	var path: String = ""
	var text: String = ""

	constructor(parcel: Parcel) : this() {
		name = parcel.readString() ?: ""
		path = parcel.readString() ?: ""
		text = parcel.readString() ?: ""
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(name)
		parcel.writeString(path)
		parcel.writeString(text)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<FileModel> {
		override fun createFromParcel(parcel: Parcel): FileModel {
			return FileModel(parcel)
		}

		override fun newArray(size: Int): Array<FileModel?> {
			return arrayOfNulls(size)
		}
	}

}