package com.example.todolist

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.example.todolist.models.ToDoItem
import com.example.todolist.utils.getDeviceId
import com.example.todolist.api.RetrofitInstance
import com.example.todolist.api.ToDoApi
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.viewModels.ToDoListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private val viewModel: ToDoListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        Log.e("AAA", "activity")
        viewModel.syncData()

        setContentView(binding.root)
    }
}