package com.example.todolist.di.fragment

import com.example.todolist.di.scopes.FragmentScope
import com.example.todolist.ui.screens.SettingsScreen.SettingsFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface SettingsFragmentComponent {
    fun inject(settingsFragment: SettingsFragment)
}