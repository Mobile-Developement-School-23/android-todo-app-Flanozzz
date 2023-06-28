package com.example.todolist

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.utils.getDeviceId
import com.example.todolist.viewModels.SelectedTaskViewModel

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