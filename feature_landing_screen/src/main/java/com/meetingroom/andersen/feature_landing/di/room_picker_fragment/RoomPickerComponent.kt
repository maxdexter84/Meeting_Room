package com.meetingroom.andersen.feature_landing.di.room_picker_fragment

import com.meetingroom.andersen.feature_landing.di.Screen
import com.meetingroom.andersen.feature_landing.room_picker_dialog.ui.RoomPickerDialogFragment
import dagger.Component

@Component(modules = [RoomPickerModule::class])
@Screen
interface RoomPickerComponent {
    fun inject(roomPickerDialogFragment: RoomPickerDialogFragment)
}