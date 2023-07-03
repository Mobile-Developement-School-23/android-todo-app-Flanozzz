package com.example.todolist.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.data.source.NetworkSource
import com.example.todolist.ui.viewModels.deviceIdFactory
import com.example.todolist.utils.makeRefreshSnackbar
import com.example.todolist.ui.viewModels.SelectedTaskViewModel
import com.example.todolist.ui.viewModels.ToDoListViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val toDoListViewModel: ToDoListViewModel by viewModels()
    private val selectedTaskViewModel: SelectedTaskViewModel by viewModels{deviceIdFactory()}
    private lateinit var connectivityCallback: ConnectivityManager.NetworkCallback
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        toDoListViewModel.syncData()
        registerNetworkCallback()

        startObserveByRequestStatus()

        setContentView(binding.root)
    }

    private fun startObserveByRequestStatus(){
        // Это выглядит не очень, исправлю в домашке по архитектуре)
        lifecycleScope.launch {
            toDoListViewModel.repositoryRequestStatus.collect{
                if(it == NetworkSource.ResponseStatus.Unsuccessful){
                    try {
                        val snackbar = makeRefreshSnackbar(binding.root){toDoListViewModel.syncData()}
                        snackbar.show()
                    }
                    catch (_: Exception){}
                }
            }
        }

        lifecycleScope.launch {
            selectedTaskViewModel.repositoryRequestStatus.collect{
                if(it == NetworkSource.ResponseStatus.Unsuccessful){
                    try {
                        val snackbar = makeRefreshSnackbar(binding.root){toDoListViewModel.syncData()}
                        snackbar.show()
                    }
                    catch (_: Exception){}
                }
            }
        }
    }

    private fun registerNetworkCallback(){
        connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                toDoListViewModel.syncData()
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