package com.gapps.oneone.utils.views.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.Window
import com.gapps.oneone.R
import com.gapps.oneone.utils.extensions.gone
import com.gapps.oneone.utils.extensions.visible
import kotlinx.android.synthetic.main.dialog_sp_editor.*
import java.util.*


class SpEditorDialog(context: Context,
                     private val key: String?,
                     private val type: String?,
                     private val value: String?,
                     private val ok: String?,
                     private val cancel: String?,
                     private val okAction: ((String?, String?, String?) -> Unit)? = null,
                     private val cancelAction: (() -> Unit)? = null) : Dialog(context) {

	init {
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		setContentView(R.layout.dialog_sp_editor)
		window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		setCancelable(true)

		initViews()
	}

	private fun initViews() {
		editEntry(type)

		dialog_title.apply {
			if (key == null) {
				gone()
			} else {
				text = key
			}
		}

		ok_btn.apply {
			if (ok == null) {
				gone()
			} else {
				setOnClickListener {
					onOkClick()
					cancel()
				}
				text = ok
			}
		}

		cancel_btn.apply {
			if (cancel == null) {
				gone()
			} else {
				setOnClickListener {
					cancelAction?.invoke()
					cancel()
				}
				text = cancel
			}
		}
	}

	private fun onOkClick() {
		val value = when (type?.toLowerCase(Locale.ENGLISH)) {
			"boolean" -> if(radio_true.isChecked) "true" else "false"

			else -> text_editor?.text?.toString()
		}

		okAction?.invoke(type, key, value)
	}

	private fun editEntry(type: String?) {
		type ?: return

		when (type.toLowerCase(Locale.ENGLISH)) {
			"string" -> editEntryAsString()
			"long" -> editEntryAsLong()
			"integer" -> editEntryAsInt()
			"float" -> editEntryAsFloat()
			"boolean" -> editEntryAsBoolean()
		}
	}

	private fun editEntryAsFloat() {
		boolean_editor.gone()
		text_editor.apply {
			visible()
			inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
			setText(value)
			setSelection(value?.length ?: 0)
		}
	}

	private fun editEntryAsBoolean() {
		boolean_editor.visible()
		text_editor.gone()

		if (value?.toLowerCase(Locale.ENGLISH) == "true") {
			boolean_editor.check(R.id.radio_true)
		} else {
			boolean_editor.check(R.id.radio_false)
		}

	}

	private fun editEntryAsInt() {
		boolean_editor.gone()
		text_editor.apply {
			visible()
			inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
			setText(value)
			setSelection(value?.length ?: 0)
		}
	}

	private fun editEntryAsLong() {
		boolean_editor.gone()
		text_editor.apply {
			visible()
			inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
			setText(value)
			setSelection(value?.length ?: 0)
		}
	}

	private fun editEntryAsString() {
		boolean_editor.gone()
		text_editor.apply {
			visible()
			inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
			setText(value)
			setSelection(value?.length ?: 0)
		}
	}
}