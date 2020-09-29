package com.gapps.oneone.utils.views.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.text.InputType
import android.view.Window
import com.gapps.oneone.R
import com.gapps.oneone.utils.extensions.gone
import com.gapps.oneone.utils.extensions.showKeyboard
import kotlinx.android.synthetic.main.dialog_oo_input.*


class InputDialog(
		context: Context,
		private val title: String?,
		private val ok: String?,
		private val cancel: String?,
		private val okAction: ((String) -> Unit)? = null,
		private val cancelAction: (() -> Unit)? = null,
) : Dialog(context) {

	init {
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		setContentView(R.layout.dialog_oo_input)
		window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		setCancelable(true)

		initViews()
	}

	private fun initViews() {
		editEntryAsString()

		dialog_title.apply {
			if (title == null) {
				gone()
			} else {
				text = title
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

		Handler().postDelayed({context.showKeyboard(text_editor)}, 200)
	}

	private fun onOkClick() {
		val inputText = text_editor.text?.toString()

		if (inputText != null) {
			okAction?.invoke(inputText)
		}
	}

	private fun editEntryAsString() {
		text_editor.apply {
			inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
		}
	}
}