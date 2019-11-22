package com.gapps.oneone.utils.bottom_menu.fancy

import android.content.Context
import android.text.Editable

class FancyTextController(context: Context) {
	private var data: MutableList<List<String>> = mutableListOf()

	init {
		context.assets.open("symbols/symbols.txt").bufferedReader().use {
			it.useLines { lines ->
				lines.forEach {
					data.add(it.split(" "))
				}
			}
		}
	}

	private fun getFor(it: Char, position: Int): String {
		val indexOf = data.getOrNull(0)?.indexOf(it.toString()) ?: return ""

		return if (indexOf >= 0) {
			data[position][indexOf]
		} else {
			it.toString()
		}
	}

	fun getFor(string: String, position: Int): String {
		val builder = StringBuilder()
		string.forEach {
			builder.append(getFor(it, position))
		}


		val result = builder.toString()

		return if (position == 1) { //for upside down text
			result.reversed()
		} else {
			result
		}
	}

	fun getFor(editable: Editable, position: Int) = getFor(editable.toString(), position)

	fun getAll(string: String): List<String> {
		val result = mutableListOf<String>()

		(1 until data.size).forEach {
			result.add(getFor(string, it))
		}

		return result
	}

	fun count() = data.size
}