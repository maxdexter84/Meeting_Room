package com.meetingroom.feature_meet_now.presentation.fast_booking_fragment

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core_module.component_manager.XInjectionManager
import com.example.core_module.deeplink_manager.DeeplinkNavigatorHelper
import com.example.core_module.utils.addToStringTime
import com.example.core_module.utils.minutesToTimeString
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.RangeSlider
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view.snackbar.ConfirmationSnackbar
import com.meeringroom.ui.view_utils.onClick
import com.meetingroom.feature_meet_now.presentation.di.MeetNowComponent
import com.meetingroom.feature_meet_now.presentation.di.viewmodel.assistedViewModels
import com.meetingroom.feature_meet_now_screen.R
import com.meetingroom.feature_meet_now_screen.databinding.FragmentFastBookingBinding
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

private const val MAX_EVENT_TIME = 90F
private const val SLIDER_START_THUMB_INDEX = 0
private const val SLIDER_END_THUMB_INDEX = 1
private const val TEXT_VIEWS_OVERLAP_MARGIN = 10

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
    }

    private fun setViews() {
        with(binding) {
            fastBookingToolbar.toolbarSaveTitle.text = viewModel.room.title
            availableForText.text = resources.getString(
                R.string.available_for,
                viewModel.room.timeUntilNextEvent?.minutesToTimeString()
                    ?: MAX_EVENT_TIME.toInt().minutesToTimeString()
            )
            fastBookingStartTimeLimit.text = viewModel.rangeSliderAdapter.startTimeLimit
            fastBookingEndTimeLimit.text = viewModel.rangeSliderAdapter.endTimeLimit
            fastBookingSlider.setLabelFormatter {
                viewModel.rangeSliderAdapter.getSelectedTime(it)
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            fastBookingToolbar.toolbarSaveCancel.onClick {
                findNavController().navigateUp()
            }
            fastBookingToolbar.buttonSaveToolbar.onClick {
                showRoomSuccessfullyBookedDialog(
                    resources.getString(
                        R.string.room_booked_successfully,
                        viewModel.room.title
                    )
                )
                findNavController().navigateUp()
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
                        viewModel.sliderState.startSelected,
                        viewModel.sliderState.endSelected,
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
                        startValue !in viewModel.sliderState.startLimit..(viewModel.sliderState.startLimit + TEXT_VIEWS_OVERLAP_MARGIN)
                    fastBookingStartTimeSelected.text = fastBookingStartTimeLimit.text.toString()
                        .addToStringTime(startValue.toInt())
                }
            }
        }
    }

    private fun sliderEndObserver() {
        with(binding) {
            lifecycleScope.launchWhenStarted {
                viewModel.sliderEndFlow.collectLatest { endValue ->
                    fastBookingEndTimeLimit.isVisible =
                        endValue !in viewModel.sliderState.endLimit - TEXT_VIEWS_OVERLAP_MARGIN..viewModel.sliderState.endLimit
                    fastBookingEndTimeSelected.text = fastBookingStartTimeLimit.text.toString()
                        .addToStringTime(endValue.toInt())
                }
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
            displayTextUnderThumbs(slider)
        }

        private fun displayTextUnderThumbs(slider: RangeSlider) {
            var layoutParams: ConstraintLayout.LayoutParams
            var offset: Float
            val margin: Int
            var timeSelected: String

            with(slider) {
                offset = trackWidth * values[thumbIndex] / valueTo
                margin = resources.getDimensionPixelSize(R.dimen.dimens_16_dp)
            }

            with(binding) {
                if (thumbIndex == SLIDER_START_THUMB_INDEX) {
                    fastBookingStartTimeSelected.isVisible = true
                    offset += margin
                    timeSelected =
                        viewModel.rangeSliderAdapter.getSelectedTime(slider.values[SLIDER_START_THUMB_INDEX])
                    layoutParams =
                        fastBookingStartTimeSelected.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.marginStart = offset.toInt()
                    fastBookingStartTimeSelected.layoutParams = layoutParams
                }
                if (thumbIndex == SLIDER_END_THUMB_INDEX) {
                    fastBookingEndTimeSelected.isVisible = true
                    offset -= margin
                    timeSelected =
                        viewModel.rangeSliderAdapter.getSelectedTime(slider.values[SLIDER_END_THUMB_INDEX])
                    layoutParams =
                        fastBookingEndTimeSelected.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.marginEnd = (slider.trackWidth - offset).toInt()
                    fastBookingEndTimeSelected.layoutParams = layoutParams
                }
            }
        }
    }
}