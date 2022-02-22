package com.meetingroom.feature_meet_now.presentation.fast_booking_fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.deeplink_manager.DeeplinkNavigatorHelper
import com.example.core_module.utils.minutesToTimeString
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.RangeSlider
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view.snackbar.ConfirmationSnackbar
import com.meeringroom.ui.view_utils.onClick
import com.meetingroom.feature_meet_now.presentation.di.MeetNowComponent
import com.meetingroom.feature_meet_now.presentation.di.viewmodel.assistedViewModels
import com.meetingroom.feature_meet_now.presentation.fast_booking_fragment.Constants.MAX_EVENT_TIME
import com.meetingroom.feature_meet_now.presentation.utils.getValidAvailableTime
import com.meetingroom.feature_meet_now_screen.R
import com.meetingroom.feature_meet_now_screen.databinding.FragmentFastBookingBinding
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

private const val SLIDER_START_THUMB_INDEX = 0
private const val TWO = 2
private const val TEXT_VIEWS_OVERLAP_MARGIN = 10
private const val DEFAULT_SCREEN_WIDTH = 1080

class FastBookingFragment :
    BaseFragment<FragmentFastBookingBinding>(FragmentFastBookingBinding::inflate) {

    private val args: FastBookingFragmentArgs by navArgs()
    private val deepLinkHelper: DeeplinkNavigatorHelper by lazy {
        DeeplinkNavigatorHelper(findNavController())
    }

    @Inject
    lateinit var viewModelFactory: FastBookingViewModel.Factory
    private val viewModel: FastBookingViewModel by assistedViewModels {
        viewModelFactory.create(args.room)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<MeetNowComponent>().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setListeners()
        sliderValidationObserver()
        sliderStartObserver()
        sliderEndObserver()
        eventCreatedObserver()
        limitTimeStringObservers()
        selectedTimeStringObservers()
        sliderStateObserver()
    }

    private fun setViews() {
        with(binding) {
            fastBookingToolbar.toolbarSaveTitle.text = viewModel.room.title
            availableForText.text = resources.getString(
                R.string.available_for,
                viewModel.room.getValidAvailableTime(MAX_EVENT_TIME).minutesToTimeString()
            )
            fastBookingSlider.setLabelFormatter {
                viewModel.getSelectedTime(it.toInt())
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            fastBookingToolbar.toolbarSaveCancel.onClick {
                findNavController().navigateUp()
            }
            fastBookingToolbar.buttonSaveToolbar.onClick {
                viewModel.bookRoom()
            }
            fastBookingRoomsLink.onClick {
                deepLinkHelper.navigate(resources.getString(R.string.deeplink_uri_rooms_fragment))
            }
            fastBookingSlider.addOnSliderTouchListener(FastBookingSliderTouchListener())
        }
    }

    private fun sliderValidationObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.validationStateFlow.collectLatest { validationState ->
                if (validationState is ValidationState.Invalid) {
                    val message = when (validationState) {
                        ValidationState.Invalid.START_TIME_VIOLATION -> R.string.incorrect_start_time_message
                        ValidationState.Invalid.MIN_EVENT_TIME_VIOLATION -> R.string.incorrect_min_time_message
                        ValidationState.Invalid.MAX_EVENT_TIME_VIOLATION -> R.string.incorrect_max_time_message
                    }
                    binding.fastBookingSlider.setValues(
                        viewModel.getSliderState().startSelected.toFloat(),
                        viewModel.getSliderState().endSelected.toFloat()
                    )
                    showAlertDialog(message)
                }
            }
        }
    }

    private fun sliderStartObserver() {
        with(binding) {
            lifecycleScope.launchWhenStarted {
                viewModel.sliderStartFlow.collectLatest { startValue ->
                    fastBookingStartTimeLimit.isVisible =
                        startValue !in viewModel.getSliderState().startLimit..(viewModel.getSliderState().startLimit + TEXT_VIEWS_OVERLAP_MARGIN)
                }
            }
        }
    }

    private fun sliderEndObserver() {
        with(binding) {
            lifecycleScope.launchWhenStarted {
                viewModel.sliderEndFlow.collectLatest { endValue ->
                    fastBookingEndTimeLimit.isVisible =
                        endValue !in viewModel.getSliderState().endLimit - TEXT_VIEWS_OVERLAP_MARGIN..viewModel.getSliderState().endLimit
                }
            }
        }
    }

    private fun eventCreatedObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.eventCreatedFlow.collectLatest { status ->
                when (status) {
                    true -> {
                        showRoomSuccessfullyBookedDialog(
                            resources.getString(
                                R.string.room_booked_successfully,
                                viewModel.room.title
                            )
                        )
                        findNavController().navigateUp()
                    }
                    false -> {
                    }
                }
            }
        }
    }

    private fun selectedTimeStringObservers() {
        with(binding) {
            viewModel.startSelectedTimeStringData.observe(viewLifecycleOwner) {
                fastBookingStartTimeSelected.text = it
                fastBookingStartTimeSelected.applyOffset(
                    getSelectedTextViewOffset(
                        fastBookingStartTimeSelected,
                        viewModel.getSliderState().startSelected
                    )
                )
            }
            viewModel.endSelectedTimeStringData.observe(viewLifecycleOwner) {
                fastBookingEndTimeSelected.text = it
                fastBookingEndTimeSelected.applyOffset(
                    getSelectedTextViewOffset(
                        fastBookingStartTimeSelected,
                        viewModel.getSliderState().endSelected
                    )
                )
            }
        }
    }

    private fun limitTimeStringObservers() {
        with(binding) {
            viewModel.startLimitTimeStringData.observe(viewLifecycleOwner) {
                fastBookingStartTimeLimit.text = it
            }
            viewModel.endLimitTimeStringData.observe(viewLifecycleOwner) {
                fastBookingEndTimeLimit.text = it
            }
        }
    }

    private fun sliderStateObserver() {
        with(binding) {
            viewModel.sliderStateData.observe(viewLifecycleOwner) { sliderState ->
                fastBookingSlider.valueFrom = sliderState.startLimit.toFloat()
                fastBookingSlider.valueTo = sliderState.endLimit.toFloat()
                fastBookingSlider.setValues(
                    sliderState.startSelected.toFloat(),
                    sliderState.endSelected.toFloat()
                )
            }
        }
    }

    private fun showAlertDialog(message: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setCancelable(false)
            .setNegativeButton(R.string.cancel_button) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showRoomSuccessfullyBookedDialog(snackbarMessage: String) {
        ConfirmationSnackbar.make(binding.root).apply {
            message = snackbarMessage
        }.show()
    }

    inner class FastBookingSliderTouchListener :
        RangeSlider.OnSliderTouchListener {
        private var thumbIndex = SLIDER_START_THUMB_INDEX

        override fun onStartTrackingTouch(slider: RangeSlider) {
            binding.fastBookingTimeSelectedGroup.isVisible = false
            thumbIndex = slider.activeThumbIndex
        }

        override fun onStopTrackingTouch(slider: RangeSlider) {
            binding.fastBookingTimeSelectedGroup.isVisible = true
            viewModel.onSliderStateChanged(thumbIndex, slider.values)
        }
    }

    private fun getSelectedTextViewOffset(textView: TextView, thumbPosition: Int): Int {
        with(viewModel.getSliderState()) {
            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            return getSliderWidth() * (thumbPosition - startLimit) / (endLimit - startLimit) +
                    resources.getDimensionPixelSize(R.dimen.dimens_16_dp) + binding.fastBookingSlider.haloRadius / TWO - textView.measuredWidth / TWO
        }
    }

    private fun View.applyOffset(offset: Int) {
        val layoutParams = this.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginStart = offset
        this.layoutParams = layoutParams
    }

    private fun getSliderWidth(): Int {
        return getScreenWidth() - TWO * resources.getDimensionPixelSize(R.dimen.dimens_16_dp) - binding.fastBookingSlider.haloRadius
    }

    private fun getScreenWidth(): Int {
        return requireActivity().resources?.displayMetrics?.widthPixels ?: DEFAULT_SCREEN_WIDTH
    }
}