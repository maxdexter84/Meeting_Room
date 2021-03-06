package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.presentation.RoomsEventViewModel
import com.andersen.feature_rooms_screen.presentation.di.DaggerRoomsEventComponent
import com.andersen.feature_rooms_screen.presentation.di.RoomsEventComponent
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.multiple_room_grid.MainEventAdapter
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.multiple_room_grid.RoomsAdapter
import com.andersen.feature_rooms_screen.presentation.rooms_event_grid.single_room_event.SingleRoomEventAdapter
import com.andersen.feature_rooms_screen.presentation.utils.RoomEventForGrid
import com.andersen.feature_rooms_screen.presentation.utils.toEmptyEventListForGrid
import com.andersen.feature_rooms_screen.presentation.utils.toEventListForGrid
import com.example.core_module.component_manager.IHasComponent
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.state.State
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view.indicator_view.IndicatorView
import com.meeringroom.ui.view.snackbar.ConfirmationSnackbar
import com.meeringroom.ui.view.toolbar.ToolbarHandlerOptions
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentRoomsBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlinx.android.synthetic.main.fragment_rooms.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

class RoomsEventGridFragment : BaseFragment<FragmentRoomsBinding>(FragmentRoomsBinding::inflate),
    IHasComponent<RoomsEventComponent> {

    private lateinit var selectedDateForGrid: LocalDate

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RoomsEventViewModel by viewModels {
        viewModelFactory
    }

    private val singleRoomEventAdapter =
        SingleRoomEventAdapter(onEventTileClick = { navigateToDetail(it) })
    private val roomsAdapter = RoomsAdapter()
    private val mainEventAdapter = MainEventAdapter(onClickEvent = { navigateToDetail(it) }) {
        findNavController().navigate(
            RoomsEventGridFragmentDirections.actionRoomsFragmentToNewEventFragment(
                eventDate = selectedDateForGrid,
                eventStartTime = it.first,
                eventEndTime = it.second,
                roomTitle = it.third.title,
                roomId = it.third.id
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.bindComponent(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initCalendar()
        initRecyclerView()
        eventListObserver()
        roomListObserver()
        loadingStateObserver()
        synchronizationScrolling()
        openDialogWithRooms()
        getEventsByDate()
        eventListByRoomObserver()
        observeRoomChange()
        observeIndicatorTimeRange()
        initCalendarListener()
        observeSuccessBooked()
        getUserRole()
    }

    private fun observeIndicatorTimeRange() {
        lifecycleScope.launchWhenStarted {
            IndicatorView.rangePeriod.collectLatest {
                binding.timeLineView.apply {
                    dynamicStartTime = it.first
                    dynamicEndTime = it.second
                }
            }
        }
    }

    override fun getComponent(): RoomsEventComponent {
        return DaggerRoomsEventComponent
            .factory()
            .create(requireContext(), XInjectionManager.findComponent())
    }

    private fun initToolbar() {
        with(binding) {
            roomsToolbar.setToolBarTitle(getString(R.string.toolbar_rooms_title))
            roomsToolbar.changeToolBarConfiguration(
                ToolbarHandlerOptions.AddEvent(
                    onIconClick = {
                        findNavController().navigate(
                            RoomsEventGridFragmentDirections.actionRoomsFragmentToNewEventFragment(
                                selectedDateForGrid,
                                viewModel.mutableRoomList.value[0].title,
                                eventStartTime = LocalTime.now(),
                                eventEndTime = LocalTime.now().plusHours(ONE_HOUR),
                                roomId = viewModel.mutableRoomList.value[0].id
                            )
                        )
                    }
                )
            )
        }
    }

    private fun initRecyclerView() {
        val singleRoomEventRecyclerView = binding.singleRoomGridRecyclerView
        singleRoomEventRecyclerView.adapter = singleRoomEventAdapter
        singleRoomEventRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val roomRecyclerView = binding.roomRecyclerView
        roomRecyclerView.adapter = roomsAdapter
        roomRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val gridRecyclerView = binding.gridRecyclerView
        gridRecyclerView.adapter = mainEventAdapter
        gridRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun eventListObserver() {
        lifecycleScope.launch {
            viewModel.mutableRoomEventList.collectLatest {
                val heightSingleRoomGrid = binding.timeLineView.getAllHoursHeight()
                mainEventAdapter.emptyEventList = it.toEmptyEventListForGrid(
                    heightSingleRoomGrid,
                    binding.oneWeekCalendar.currentDate
                )
                mainEventAdapter.eventList = it.toEventListForGrid(heightSingleRoomGrid)
            }
        }
    }

    private fun eventListByRoomObserver() {
        lifecycleScope.launch {
            viewModel.mutableRoomEventList.collectLatest {
                val heightSingleRoomGrid = binding.timeLineView.getAllHoursHeight()
                singleRoomEventAdapter.emptyEventList =
                    it.toEmptyEventListForGrid(
                        heightSingleRoomGrid,
                        binding.oneWeekCalendar.currentDate
                    )
                singleRoomEventAdapter.eventList = it.toEventListForGrid(heightSingleRoomGrid)
            }
        }
    }

    private fun roomListObserver() {
        lifecycleScope.launch {
            viewModel.mutableRoomList.collectLatest {
                with(viewModel) {
                    roomsAdapter.roomList = it
                    mainEventAdapter.roomList = it
                    getEventList(binding.oneWeekCalendar.selectedDate)
                }
            }
        }
    }

    private fun loadingStateObserver() {
        lifecycleScope.launch {
            viewModel.mutableState.collectLatest {
                when (it) {
                    is State.Loading -> binding.progressBar.isVisible = true
                    else -> binding.progressBar.isVisible = false
                }
            }
        }
    }

    private fun synchronizationScrolling() {
        with(binding) {
            gridNestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                timeLineView.scrollOnDy(
                    scrollY - oldScrollY
                )
            })

            timeLineView.onScroll = {
                gridNestedScrollView.scrollBy(0, it)
            }

            roomRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (gridRecyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        super.onScrolled(recyclerView, dx, dy)
                        gridRecyclerView.scrollBy(dx, 0)
                    }
                }
            })

            gridRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (roomRecyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        super.onScrolled(recyclerView, dx, dy)
                        roomRecyclerView.scrollBy(dx, 0)
                    }
                }
            })
        }
    }

    private fun initCalendar() {
        with(binding.oneWeekCalendar) {
            if (!this@RoomsEventGridFragment::selectedDateForGrid.isInitialized) {
                selectedDateForGrid = LocalDate.now()
            }
            setDateSelected(CalendarDay.today(), true)
            setCurrentDateColor()
            setOnTitleClickListener {
                showDatePickerDialog()
            }
        }
    }

    private fun initCalendarListener() {
        with(binding.oneWeekCalendar) {
            setOnDateChangedListener { _, date, _ ->
                selectedDateForGrid = LocalDate.of(date.year, date.month, date.day)
                viewModel.getEventList(date)
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                selectedDateForGrid = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                val selectedDayForCalendar = CalendarDay.from(year, monthOfYear + 1, dayOfMonth)
                with(binding.oneWeekCalendar) {
                    setDateSelected(selectedDate, false)
                    setCurrentDate(selectedDayForCalendar, true)
                    setDateSelected(selectedDayForCalendar, true)
                }
            }, year, month, day
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
    }

    private fun setCurrentDateColor() {
        binding.oneWeekCalendar.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                return day == CalendarDay.today()
            }

            override fun decorate(view: DayViewFacade) {
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.selector_calendar
                )?.let { view.setBackgroundDrawable(it) }
            }
        })
    }

    private fun openDialogWithRooms() {
        with(binding) {
            buttonDropDown.setOnClickListener {
                findNavController().navigate(
                    RoomsEventGridFragmentDirections.actionRoomsFragmentToDialogRoomsFragment(
                        binding.buttonDropDown.text.toString(), roomsAdapter.roomList.toTypedArray()
                    )
                )
            }
        }
    }

    private fun observeRoomChange() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            ROOM_KEY
        )
            ?.observe(viewLifecycleOwner) {
                it?.let { it ->
                    binding.buttonDropDown.text = it
                    getSelectedRoomsFromDialog(it)
                }
            }
    }

    private fun getSelectedRoomsFromDialog(roomTitle: String) {
        if (roomTitle.contains(getString(R.string.allString), true)) {
            val floor = roomTitle.filter { char -> char.isDigit() }
            viewModel.getRoomsOnTheFloor(floor)
            allRoomsOnTheFloorObserver()
        } else {
            viewModel.getRoom(roomTitle)
            oneRoomStateObserver(roomTitle)
            if (viewModel.userRole == getString(R.string.adminRole)) {
                displayLockRoomAction()
            }
        }
    }

    private fun getEventsByDate() {
        viewModel.getEventList(binding.oneWeekCalendar.selectedDate)
        binding.oneWeekCalendar.setOnDateChangedListener { _, date, _ -> viewModel.getEventList(date) }
    }

    private fun checkEventRoom(room: Room) {
        checkWhiteboard(room)
        checkProjector(room)
        with(binding) {
            ivIconCapacity.visibility = View.VISIBLE
            tvMaxCapacity.visibility = View.VISIBLE
            tvMaxCapacity.text = room.capacity.toString()
        }
    }

    private fun checkWhiteboard(room: Room) {
        if (room.board) {
            binding.ivIconWhiteboard.visibility = View.VISIBLE
        } else {
            binding.ivIconWhiteboard.visibility = View.GONE
        }
    }

    private fun checkProjector(room: Room) {
        if (room.projector) {
            binding.ivIconProjector.visibility = View.VISIBLE
        } else {
            binding.ivIconProjector.visibility = View.GONE
        }
    }

    private fun hideIconsForAllRooms() {
        with(binding) {
            iv_icon_capacity.visibility = View.GONE
            iv_icon_projector.visibility = View.GONE
            iv_icon_whiteboard.visibility = View.GONE
            tvMaxCapacity.visibility = View.GONE
        }
    }

    private fun oneRoomStateObserver(roomTitle: String) {
        lifecycleScope.launch {
            viewModel.room.collectLatest {
                it?.let {
                    checkEventRoom(it)
                }
                binding.gridRecyclerView.isVisible = false
                binding.singleRoomGridRecyclerView.isVisible = true
                binding.roomRecyclerView.isVisible = false
                singleRoomEventAdapter.roomTitle = roomTitle
                viewModel.getEventList(binding.oneWeekCalendar.selectedDate)
            }
        }
    }

    private fun allRoomsOnTheFloorObserver() {
        lifecycleScope.launch {
            viewModel.mutableRoomListByFloor.collectLatest {
                hideIconsForAllRooms()
                if (viewModel.userRole == getString(R.string.adminRole)) {
                    hideRoomLockAction()
                }
                binding.singleRoomGridRecyclerView.isVisible = false
                binding.gridRecyclerView.isVisible = true
                binding.roomRecyclerView.isVisible = true
                roomsAdapter.roomList = it
                mainEventAdapter.roomList = it
                viewModel.getEventList(binding.oneWeekCalendar.selectedDate)
            }
        }
    }

    private fun displayLockRoomAction() {

        binding.lockIcon.visibility = View.VISIBLE
        binding.lockButton.visibility = View.VISIBLE
        binding.lockButton.setOnClickListener {
            findNavController().navigate(
                RoomsEventGridFragmentDirections.actionRoomsFragmentToNewLockEventFragment(
                    eventDate = selectedDateForGrid,
                    eventStartTime = LocalTime.now(),
                    eventEndTime = LocalTime.now()
                )
            )
        }

        val metrics = requireActivity().resources?.displayMetrics
        val tvMaxCapMarginStart = 16f
        val ivIconCapMarginStart = 9f
        val ivIconWhiteboardMarginStart = 24f
        val ivIconProjectorMarginStart = 24f

        with(binding) {
            tvMaxCapacity.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToTop = -1
                bottomToBottom = -1
                endToStart = -1
                topToBottom = buttonDropDown.id
                startToStart = buttonDropDown.id
                marginStart = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    tvMaxCapMarginStart,
                    metrics
                ).toInt()
            }
            ivIconCapacity.updateLayoutParams<ConstraintLayout.LayoutParams> {
                endToStart = -1
                startToEnd = tvMaxCapacity.id
                topToTop = tvMaxCapacity.id
                bottomToBottom = tvMaxCapacity.id
                marginStart = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ivIconCapMarginStart,
                    metrics
                ).toInt()
            }
            ivIconWhiteboard.updateLayoutParams<ConstraintLayout.LayoutParams> {
                endToStart = -1
                topToTop = ivIconCapacity.id
                bottomToBottom = ivIconCapacity.id
                startToEnd = ivIconCapacity.id
                marginStart = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ivIconWhiteboardMarginStart,
                    metrics
                ).toInt()
            }
            ivIconProjector.updateLayoutParams<ConstraintLayout.LayoutParams> {
                endToEnd = -1
                startToEnd = ivIconWhiteboard.id
                topToTop = ivIconCapacity.id
                bottomToBottom = ivIconCapacity.id
                marginStart = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    ivIconProjectorMarginStart,
                    metrics
                ).toInt()
            }
            roomRecyclerView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToBottom = ivIconCapacity.id
            }
        }
    }

    private fun getUserRole() {
        viewModel.getUserRole()
    }

    private fun hideRoomLockAction() {
        binding.lockIcon.visibility = View.GONE
        binding.lockButton.visibility = View.GONE
    }

    private fun navigateToDetail(roomEvent: RoomEventForGrid) {
        if (roomEvent.isUserOwnEvent) findNavController().navigate(
            RoomsEventGridFragmentDirections.actionRoomsFragmentToEventDetailDialog(
                roomEvent = roomEvent
            )
        )
    }

    private fun observeSuccessBooked() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            SUCCESS_KEY
        )
            ?.observe(viewLifecycleOwner) {
                showSuccessBooked(resources.getString(R.string.booked_successfully))
            }
    }

    private fun showSuccessBooked(snackbarMessage: String) {
        ConfirmationSnackbar.make(binding.root).apply {
            message = snackbarMessage
        }
            .setAnchorView(R.id.rooms_screen_navigation)
            .show()
    }

    companion object {
        const val ROOM_KEY = "ROOM_KEY"
        const val ONE_HOUR = 1L
        const val SUCCESS_KEY = "BOOKED"
    }
}
