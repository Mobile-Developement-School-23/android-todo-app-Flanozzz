package com.example.todolist.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.RadioGroup
import com.example.todolist.R
import com.example.todolist.ToDoApp
import com.example.todolist.databinding.FragmentSettingsBinding
import com.example.todolist.utils.Constants.DARK_THEME
import com.example.todolist.utils.Constants.LIGHT_THEME
import com.example.todolist.utils.Constants.SHARED_PREFERENCES_NAME
import com.example.todolist.utils.Constants.SYSTEM_THEME
import com.example.todolist.utils.Constants.THEME
import com.example.todolist.utils.setSettingsTheme
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setupDependencies()
        setListeners()

        requireContext().setTheme(R.style.Theme_ToDoList)
        setThemeRadioGroupState()

        return binding.root
    }

    private fun setupDependencies(){
        (requireContext().applicationContext as ToDoApp)
        .appComponent
            .activityComponent()
            .settingsFragmentComponent()
            .inject(this)
    }

    private fun setListeners(){
        binding.closeButton.setOnClickListener(closeButtonListener)
        binding.themeRadioGroup.setOnCheckedChangeListener(themeRadioGroupListener)
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
        sharedPreferences.edit().putString(THEME, newTheme).apply()
        setSettingsTheme(sharedPreferences)
    }

    private fun setThemeRadioGroupState(){
        val currentTheme = sharedPreferences.getString(THEME, SYSTEM_THEME)
        with(binding){
            when(currentTheme){
                DARK_THEME -> themeRadioGroup.check(darkThemeRadioButton.id)
                LIGHT_THEME -> themeRadioGroup.check(lightThemeRadioButton.id)
                else -> themeRadioGroup.check(systemThemeRadioButton.id)
            }
        }
    }
}