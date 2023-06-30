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
    private lateinit var connectivityCallback: ConnectivityManager.NetworkCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel.syncData()
        registerNetworkCallback()

        setContentView(binding.root)
    }

    private fun registerNetworkCallback(){
        connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                viewModel.syncData()
            }
            override fun onLost(network: Network) {}
        }

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(connectivityCallback)
    }

    private fun unregisterNetworkCallback() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityCallback.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkCallback()
    }
}