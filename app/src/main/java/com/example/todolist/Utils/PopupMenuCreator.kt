package com.example.todolist.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.example.todolist.R

class PopupMenuCreator {
    companion object{
        @SuppressLint("ClickableViewAccessibility")
        fun setCloseWhenTouchOutsideEvent(popupWindow: PopupWindow){
            popupWindow.isOutsideTouchable = true
            popupWindow.setTouchInterceptor { _, event ->
                if (event.action == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss()
                    true
                } else {
                    false
                }
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun showPopupMenu(view: View, layout: Int) : PopupWindow {
            val popupWindow = PopupWindow(view.context)

            val inflater = LayoutInflater.from(view.context)

            val contentView = inflater.inflate(layout, null)

            popupWindow.contentView = contentView
            setCloseWhenTouchOutsideEvent(popupWindow)

            popupWindow.setBackgroundDrawable(view.context.getDrawable(R.drawable.popup_window_background))
            popupWindow.elevation = 20f

            popupWindow.showAsDropDown(view, 0, 0)
            return popupWindow
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun showPopupMenu(view: View, layout: ViewGroup) : PopupWindow {
            val popupWindow = PopupWindow(view.context)

            popupWindow.contentView = layout
            setCloseWhenTouchOutsideEvent(popupWindow)

            popupWindow.setBackgroundDrawable(view.context.getDrawable(R.drawable.popup_window_background))
            popupWindow.elevation = 20f

            popupWindow.showAsDropDown(view, 0, 0)
            return popupWindow
        }

        fun createPopupMenuField(context: Context, text: String, textColor: Int): View {
            val field = TextView(ContextThemeWrapper(context, R.style.PopUpMenuField), null, 0)
            field.id = R.id.moveUpButton
            field.layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
            )
            field.text = text
            field.setTextColor(textColor)
            return field
        }

        fun createPopupMenuLayoutContainer(context: Context): LinearLayoutCompat {
            val layout = LinearLayoutCompat(context)
            layout.orientation = LinearLayoutCompat.VERTICAL
            layout.minimumWidth = context.resources.getDimensionPixelSize(R.dimen.min_popupMenu_width)
            return layout
        }
    }
}