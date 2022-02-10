package com.meetingroom.feature_workplaces_screen.presentation.fragments.book_workplace_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_module.state.State
import com.example.core_network.RequestResult
import com.meeringroom.ui.event_dialogs.dialog_floor_picker.model.FloorData
import com.meetingroom.feature_workplaces_screen.domain.use_case.GetOfficeDataUseCase
import com.meetingroom.feature_workplaces_screen.presentation.mappers.Mappers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookWorkplaceViewModel @Inject constructor(
    private val getOfficeDataUseCase: GetOfficeDataUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<State>(State.Loading)
    val loadingState: StateFlow<State> get() = _loadingState.asStateFlow()

    private val _officeList = MutableStateFlow<List<FloorData>>(emptyList())
    val officeList: StateFlow<List<FloorData>> = _officeList

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
}