package com.andersen.feature_rooms_screen.presentation.rooms_event_grid

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andersen.feature_rooms_screen.domain.entity.Room
import com.andersen.feature_rooms_screen.presentation.di.DaggerRoomsEventComponent
import com.andersen.feature_rooms_screen.presentation.di.RoomsEventComponent
import com.andersen.feature_rooms_screen.presentation.utils.toEmptyEventListForGrid
import com.andersen.feature_rooms_screen.presentation.utils.toEventListForGrid
import com.example.core_module.state.State
import com.example.core_module.utils.stringToDate
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view.time_line.TimeLineView
import com.meetingroom.andersen.feature_rooms_screen.R
import com.meetingroom.andersen.feature_rooms_screen.databinding.FragmentRoomsBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlinx.android.synthetic.main.fragment_rooms.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class RoomsEventGridFragment : BaseFragment<FragmentRoomsBinding>(FragmentRoomsBinding::inflate), IHasComponent<RoomsEventComponent> {

    private var selectedDateForGrid: LocalDate? = null
    private var eventRoom = "All rooms"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RoomsEventViewModel by viewModels {
        viewModelFactory
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
        observeViewModelState()
    }

    override fun getComponent(): RoomsEventComponent =
        DaggerRoomsEventComponent.builder().build()

    private fun initToolbar() {
        with(binding) {
            roomsToolbar.setToolBarTitle(getString(R.string.toolbar_rooms_title))
        }
    }

    private fun initRecyclerView() {
        val singleRoomEventRecyclerView = binding.singleRoomGridRecyclerView
        singleRoomEventRecyclerView.adapter = viewModel.singleRoomEventAdapter
        singleRoomEventRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val roomRecyclerView = binding.roomRecyclerView
        roomRecyclerView.adapter = viewModel.roomsAdapter
        roomRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val gridRecyclerView = binding.gridRecyclerView
        gridRecyclerView.adapter = viewModel.mainEventAdapter
        gridRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun eventListObserver() {
        lifecycleScope.launch {
            viewModel.mutableRoomEventListByRoom.collectLatest {
                val heightSingleRoomGrid = binding.timeLineView.getAllHoursHeight()
                viewModel.mainEventAdapter.emptyEventList = it.toEmptyEventListForGrid(heightSingleRoomGrid, binding.oneWeekCalendar.currentDate)
                viewModel.mainEventAdapter.eventList = it.toEventListForGrid(heightSingleRoomGrid)
            }
        }
    }

    private fun eventListByRoomObserver() {
        lifecycleScope.launch {
            viewModel.mutableRoomEventListByRoom.collectLatest {
                val heightSingleRoomGrid = binding.timeLineView.getAllHoursHeight()
                viewModel.singleRoomEventAdapter.emptyEventList = it.toEmptyEventListForGrid(heightSingleRoomGrid,binding.oneWeekCalendar.currentDate)
                viewModel.singleRoomEventAdapter.eventList = it.toEventListForGrid(heightSingleRoomGrid)
            }
        }
    }

    private fun roomListObserver() {
        lifecycleScope.launch {
            viewModel.mutableRoomList.collectLatest {
                viewModel.roomsAdapter.roomList = it
                viewModel.mainEventAdapter.roomList = it
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
            setDateSelected(CalendarDay.today(), true)
            setCurrentDateColor()
            setOnDateChangedListener { widget, date, selected ->
                selectedDateForGrid =
                    "${date.day}/${date.month}/${date.year}".stringToDate(DATE_FORMAT)
            }
            setOnTitleClickListener {
                showDatePickerDialog()
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
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                selectedDateForGrid =
                    "$dayOfMonth/${monthOfYear + 1}/$year".stringToDate(DATE_FORMAT)
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
            observeRoomChange()
            buttonDropDown.setOnClickListener {
                findNavController().navigate(
                    RoomsEventGridFragmentDirections.actionRoomsFragmentToDialogRoomsFragment(
                        eventRoom
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
                it?.let {
                    binding.buttonDropDown.text = it
                    eventRoom = it
                    if(it.contains(getString(R.string.allString), true)){
                        //TODO request all rooms on the floor
                        checkEventRoom(eventRoom)
                    }else{
                        viewModel.getRoom(eventRoom)
                    }
                }
            }
    }

    private fun getEventsByDate() {
        viewModel.getEventList(binding.oneWeekCalendar.selectedDate)
        binding.oneWeekCalendar.setOnDateChangedListener { _, date, _ -> viewModel.getEventList(date) }
    }

    private fun checkEventRoom(roomTitle: String) {
        if (roomTitle.contains(getString(R.string.allString), true)) {
            //TODO call fun show allRoomsByTheFloor on the grid
            with(binding) {
                iv_icon_capacity.visibility = View.GONE
                iv_icon_projector.visibility = View.GONE
                iv_icon_whiteboard.visibility = View.GONE
                tvMaxCapacity.visibility = View.GONE
            }
        } else {
            val room = viewModel.room.value
            if (room != null) {
                //TODO call fun show one room on the grid
                checkWhiteboard(room)
                checkProjector(room)
                with(binding){
                    ivIconCapacity.visibility = View.VISIBLE
                    tvMaxCapacity.visibility = View.VISIBLE
                    tvMaxCapacity.text = room.capacity.toString()
                }
            }
        }
    }

    private fun checkWhiteboard(room: Room){
        if (room.board) {
            binding.ivIconWhiteboard.visibility = View.VISIBLE
        } else {
            binding.ivIconWhiteboard.visibility = View.GONE
        }
    }

    private fun checkProjector(room: Room){
        if (room.projector) {
            binding.ivIconProjector.visibility = View.VISIBLE
        } else {
            binding.ivIconProjector.visibility = View.GONE
        }
    }

    private fun observeViewModelState() {
        viewModel.room.observe(viewLifecycleOwner) { room -> checkEventRoom(room.title) }
    }

    companion object {
        private const val DATE_FORMAT = "d/M/yyyy"
        const val ROOM_KEY = "ROOM_KEY"
    }
}
