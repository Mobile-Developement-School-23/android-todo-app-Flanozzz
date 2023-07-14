package com.example.todolist.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.todolist.R
import com.example.todolist.ToDoApp
import com.example.todolist.data.workers.NotificationWorker
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.di.activity.ActivityComponent
import com.example.todolist.utils.makeRefreshSnackbar
import com.example.todolist.ui.viewModels.ToDoListViewModel
import com.example.todolist.ui.viewModels.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val toDoListViewModel: ToDoListViewModel by viewModels {viewModelFactory}
    private lateinit var connectivityCallback: ConnectivityManager.NetworkCallback
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (applicationContext as ToDoApp)
            .appComponent
            .activityComponent()
            .inject(this)

        toDoListViewModel.syncData()
        registerNetworkCallback()
        startObserveByRequestStatus()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
        }
    }

    private fun startObserveByRequestStatus(){
        lifecycleScope.launch {
            toDoListViewModel.isLastResponseSuccess.collect{ isSuccess ->
                if(!isSuccess){
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