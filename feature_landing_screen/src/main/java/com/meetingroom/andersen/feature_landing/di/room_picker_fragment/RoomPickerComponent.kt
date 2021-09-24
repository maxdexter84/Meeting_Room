package com.meetingroom.andersen.feature_landing.di.room_picker_fragment

import com.example.core_module.sharedpreferences_di.SharedPreferencesModule
import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.room_picker_dialog.ui.RoomPickerDialogFragment
import dagger.Component

@Component(modules = [RoomPickerModule::class, SharedPreferencesModule::class])
@Screen
interface RoomPickerComponent {
    fun inject(roomPickerDialogFragment: RoomPickerDialogFragment)
}