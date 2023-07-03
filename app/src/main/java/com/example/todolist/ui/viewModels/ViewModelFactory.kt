package com.example.todolist.ui.viewModels

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.utils.getDeviceId

open class ViewModelFactory(
    private val deviceId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass){
            SelectedTaskViewModel::class.java -> {
                SelectedTaskViewModel(deviceId)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

fun Fragment.deviceIdFactory() = ViewModelFactory(
    deviceId = getDeviceId(requireContext())
)

fun Activity.deviceIdFactory() = ViewModelFactory(
    deviceId = getDeviceId(this)
)