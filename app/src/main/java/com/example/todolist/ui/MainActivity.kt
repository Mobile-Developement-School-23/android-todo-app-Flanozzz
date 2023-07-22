package com.example.todolist.ui

import android.Manifest
import android.R.attr.fragment
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.todolist.R
import com.example.todolist.ToDoApp
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.ui.screens.EditTaskScreen.EditTaskFragment
import com.example.todolist.ui.viewModels.SelectedTaskViewModel
import com.example.todolist.ui.viewModels.ToDoListViewModel
import com.example.todolist.ui.viewModels.ViewModelFactory
import com.example.todolist.utils.makeRefreshSnackbar
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val toDoListViewModel: ToDoListViewModel by viewModels {viewModelFactory}
    private val selectedTaskViewModel: SelectedTaskViewModel by viewModels {viewModelFactory}
    private lateinit var connectivityCallback: ConnectivityManager.NetworkCallback
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.w("AAA", "onCreate")
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