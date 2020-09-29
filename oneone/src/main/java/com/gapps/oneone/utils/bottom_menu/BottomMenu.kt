package com.gapps.oneone.utils.bottom_menu

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.gapps.oneone.R
import com.gapps.oneone.utils.bottom_menu.models.MenuDataItem
import com.gapps.oneone.utils.extensions.*
import com.gapps.oneone.utils.views.bottom_dialog.BottomSheetDialogFixed
import kotlinx.android.synthetic.main.layout_menu_bottom.view.*

internal class BottomMenu(val context: Context?,
                 @ColorInt val mainColor: Int,
                 @ColorInt val backgroundColor: Int,
                 @ColorInt val accentColor: Int,
                 val menuData: MenuData,
                 val listener: Listener?
) {
	companion object {
		var isVisible = false

		inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
	}

	private constructor(builder: Builder) : this(
			builder.context,
			builder.mainColor,
			builder.backgroundColor,
			builder.accentColor,
			builder.menuData,
			builder.listener

	)

	fun show() {
		context ?: return

		context.hideKeyboard()

		val bottomSheetDialog = BottomSheetDialogFixed(context, R.style.BottomSheetDialog)
		val view = getSheetView(menuData) {
			bottomSheetDialog.dismiss()
		}

		bottomSheetDialog.apply {
			setContentView(view)

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
			}
			setOnShowListener {
				isVisible = true
			}
			setOnDismissListener {
				isVisible = false
			}
			show()
		}
	}

	private fun getSheetView(data: MenuData, onSelect: () -> Unit): View {
		return LayoutInflater.from(context).inflate(R.layout.layout_menu_bottom, null).apply {
			val textColorBackground = backgroundColor.getPrimaryTextColor()
			val textColorMain = mainColor.getPrimaryTextColor()

			if (data.title != null) {
				title.text = data.title
			}

			val titleRes = data.titleRes

			if (titleRes != null) {
				title.setText(titleRes)
			}

			title.setTextColor(textColorMain)

			header.backgroundTint(mainColor)
			menu_container.setBackgroundColor(backgroundColor)

			additional_button_icon.apply {
				setColorFilter(accentColor)
				data.buttonIcon?.let { setImageResource(it) }
			}

			additional_button_text.apply {
				data.button?.let { text = it }
				data.buttonRes?.let { text = context.getString(it) }
				setTextColor(textColorMain)
			}

			additional_button.apply {
				visibleOrGone(data.button != null || data.buttonRes != null)
				backgroundTint(textColorMain)
				setOnClickListener {
					onSelect.invoke()
					listener?.onAdditionalClick()
				}
			}

			btn_close.apply {
				val cancelText1 = data.cancelText
				text = if (cancelText1 != null) {
					context.getString(cancelText1)
				} else {
					"Cancel"
				}

				setOnClickListener {
					onSelect.invoke()
					listener?.onCancel()
				}

				setTextColor(textColorBackground)
			}

			close_container.animateBottomTop(data.data.size)

			val dividerColor = ColorUtils.setAlphaComponent(textColorBackground, .4f.toColorAlpha())

			divider.backgroundTint(dividerColor)
			divider_top.backgroundTint(dividerColor)

			menu_list.apply {
				layoutManager = LinearLayoutManager(context)
				adapter = MenuAdapter(accentColor, textColorBackground, object : MenuAdapter.Listener {
					override fun onItemClick(item: MenuDataItem, adapterPosition: Int) {
						listener?.onItemSelected(item.id ?: adapterPosition, item)
						onSelect.invoke()
					}

					override fun onItemDown(item: MenuDataItem, adapterPosition: Int) {
					}

					override fun onItemUp(item: MenuDataItem, adapterPosition: Int) {
					}

					override fun showAnimation(): Boolean = true

				}).apply { swapData(data.data) }

				attachWithKeyboardHideEvent(this)
			}
		}
	}

	class Builder {
		var context: Context? = null
			private set
		@ColorInt
		var mainColor: Int = Color.WHITE
			private set
		@ColorInt
		var backgroundColor: Int = Color.WHITE
			private set
		@ColorInt
		var accentColor: Int = Color.BLACK
			private set
		@StringRes
		var cancelText: Int? = null
			private set
		var menuData: MenuData = MenuData()
			private set
		var listener: Listener? = null
			private set

		fun with(context: Context?) = apply { this.context = context }
		fun withMainColor(@ColorInt color: Int) = apply { this.mainColor = color }
		fun withBackgroundColor(@ColorInt color: Int) = apply { this.backgroundColor = color }
		fun withAccentColor(@ColorInt color: Int) = apply { this.accentColor = color }
		fun withCancelText(@StringRes string: Int) = apply { this.cancelText = string }
		fun withMenuData(data: MenuData) = apply { this.menuData = data }
		fun withListener(listener: Listener?) = apply { this.listener = listener }

		fun build() = BottomMenu(this)
		fun show() = BottomMenu(this).show()
	}

	interface Listener {
		fun onItemSelected(index: Int, item: MenuDataItem)
		fun onCancel()
		fun onAdditionalClick()
	}
}