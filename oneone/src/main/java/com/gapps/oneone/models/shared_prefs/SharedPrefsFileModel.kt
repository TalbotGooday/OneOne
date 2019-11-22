package com.gapps.oneone.models.shared_prefs

import android.os.Parcel
import android.os.Parcelable

class SharedPrefsFileModel() : Parcelable {
	var name: String = ""

	constructor(parcel: Parcel) : this() {
		name = parcel.readString() ?: ""
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(name)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<SharedPrefsFileModel> {
		override fun createFromParcel(parcel: Parcel): SharedPrefsFileModel {
			return SharedPrefsFileModel(parcel)
		}

		override fun newArray(size: Int): Array<SharedPrefsFileModel?> {
			return arrayOfNulls(size)
		}
	}

}