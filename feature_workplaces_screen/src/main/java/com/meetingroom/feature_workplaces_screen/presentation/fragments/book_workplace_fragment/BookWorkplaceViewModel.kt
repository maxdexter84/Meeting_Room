package com.meetingroom.feature_workplaces_screen.presentation.fragments.book_workplace_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.state.State
import com.example.core_network.RequestResult
import com.meeringroom.ui.event_dialogs.dialog_floor_picker.model.FloorData
import com.meeringroom.ui.event_dialogs.dialog_floor_room_picker.model.FloorRoomData
import com.meetingroom.feature_workplaces_screen.domain.use_case.GetFloorsUseCase
import com.meetingroom.feature_workplaces_screen.domain.use_case.GetRoomsByFloorUseCase
import com.meetingroom.feature_workplaces_screen.presentation.mappers.Mappers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookWorkplaceViewModel @Inject constructor(
    private val getOfficeDataUseCase: GetFloorsUseCase,
    private val getRoomsByFloorUseCase: GetRoomsByFloorUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<State>(State.Loading)
    val loadingState: StateFlow<State> get() = _loadingState.asStateFlow()

    private val _officeList = MutableStateFlow<List<FloorData>>(emptyList())
    val officeList: StateFlow<List<FloorData>> = _officeList

    private val _floorRoomList = MutableStateFlow<List<FloorRoomData>>(emptyList())
    val floorRoomList: StateFlow<List<FloorRoomData>> = _floorRoomList

    private var externalMonitorState = false

    init {
        viewModelScope.launch {
            _loadingState.emit(State.Loading)
            when(val response = getOfficeDataUseCase.invoke()) {
                is RequestResult.Success -> {
                    _officeList.emit(Mappers.mapOfficeDataToFloorData(response.data))
                    _loadingState.emit(State.NotLoading)
                }
                is RequestResult.Error -> {
                    _officeList.emit(emptyList())
                    _loadingState.emit(State.Error)
                }
                is RequestResult.Loading -> {
                    _loadingState.emit(State.Loading)
                }
            }
        }
    }

    fun getRoomList(floor: String) {
        viewModelScope.launch {
            _loadingState.emit(State.Loading)
            when(val response = getRoomsByFloorUseCase.invoke(floor)) {
                is RequestResult.Success -> {
                    _floorRoomList.emit(Mappers.mapRoomDataToFloorRoomData(response.data))
                    _loadingState.emit(State.NotLoading)
                }
                is RequestResult.Error -> {
                    _floorRoomList.emit(emptyList())
                    _loadingState.emit(State.Error)
                }
                is RequestResult.Loading -> {
                    _loadingState.emit(State.Loading)
                }
            }
        }
    }

    fun setExternalMonitorState(isChecked: Boolean) {
        externalMonitorState = isChecked
    }
}