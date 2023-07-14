package com.example.todolist.ui.fragments

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.todolist.R
import com.example.todolist.data.workers.NotificationWorker
import com.example.todolist.data.workers.NotificationBroadcastReceiver
import com.example.todolist.databinding.FragmentSettingsBinding
import com.example.todolist.utils.Constants.DARK_THEME
import com.example.todolist.utils.Constants.LIGHT_THEME
import com.example.todolist.utils.Constants.SETTINGS
import com.example.todolist.utils.Constants.SYSTEM_THEME
import com.example.todolist.utils.Constants.THEME
import com.example.todolist.utils.setSettingsTheme

class SettingsFragment : Fragment() {


    private lateinit var binding: FragmentSettingsBinding
    private lateinit var settingsSharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setListeners()
        settingsSharedPreferences = requireContext()
            .getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        requireContext().setTheme(R.style.Theme_ToDoList)
        setThemeRadioGroupState()

        return binding.root
    }

    private fun setListeners(){
        binding.closeButton.setOnClickListener(closeButtonListener)
        binding.themeRadioGroup.setOnCheckedChangeListener(themeRadioGroupListener)
        binding.applyButton.setOnClickListener(applyButtonListener)
    }

    private val applyButtonListener = OnClickListener {
        //NotificationBroadcastReceiver.testSchedule(requireContext())
    }

    private val closeButtonListener = OnClickListener{
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private val themeRadioGroupListener = RadioGroup.OnCheckedChangeListener{ radioGroup, id ->
        var newTheme = SYSTEM_THEME
        with(binding){
            when(id){
                systemThemeRadioButton.id -> newTheme = SYSTEM_THEME
                lightThemeRadioButton.id -> newTheme = LIGHT_THEME
                darkThemeRadioButton.id -> newTheme = DARK_THEME
            }
        }
        settingsSharedPreferences.edit().putString(THEME, newTheme).apply()
        setSettingsTheme(settingsSharedPreferences)
    }

    private fun setThemeRadioGroupState(){
        val currentTheme = settingsSharedPreferences.getString(THEME, SYSTEM_THEME)
        with(binding){
            when(currentTheme){
                DARK_THEME -> themeRadioGroup.check(darkThemeRadioButton.id)
                LIGHT_THEME -> themeRadioGroup.check(lightThemeRadioButton.id)
                else -> themeRadioGroup.check(systemThemeRadioButton.id)
            }
        }
    }

    private fun showNotification(title: String?, message: String?) {
        val notificationBuilder = NotificationCompat.Builder(requireContext(), NotificationWorker.CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_list_alt_24)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager =
            NotificationManagerCompat.from(requireContext())
        Log.d("notifications", "test notification")
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channelName = "ToDoChannel"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(NotificationWorker.CHANNEL_ID, channelName, importance)
                notificationManager.createNotificationChannel(channel)
            }
            Log.d("notifications", "test notification in if")
            notificationManager.notify(NotificationWorker.NOTIFICATION_ID, notificationBuilder.build())
        }
    }
}