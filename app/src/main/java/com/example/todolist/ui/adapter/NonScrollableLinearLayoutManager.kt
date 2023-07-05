package com.example.todolist.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class NonScrollableLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }
}