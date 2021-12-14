package com.meetingroom.andersen.feature_landing.presentation.history_of_events_fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.core_module.component_manager.XInjectionManager
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.andersen.feature_landing.databinding.FragmentHistoryOfEventsBinding
import com.meetingroom.andersen.feature_landing.databinding.PopoverCopyBinding
import com.meetingroom.andersen.feature_landing.presentation.di.LandingComponent
import javax.inject.Inject


class HistoryOfEventsFragment :
    BaseFragment<FragmentHistoryOfEventsBinding>(FragmentHistoryOfEventsBinding::inflate) {

    private val eventAdapter by lazy {
        HistoryEventAdapter { view, text ->
            showCopyPrompt(view, text)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: HistoryOfEventsFragmentViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XInjectionManager.findComponent<LandingComponent>().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        viewModel.gagData.observe(viewLifecycleOwner) {
            eventAdapter.setData(it)
            initEmptyUpcomingMessage(it.isEmpty())
        }

    }

    private fun initRecyclerView() {
        with(binding) {
            historyEventsRecyclerView.apply {
                setHasFixedSize(true)
                adapter = eventAdapter
            }
        }
    }

    private fun showCopyPrompt(incomingView: View?, text: String) {
        val popupWindow = PopupWindow(requireActivity())
        val bindingPopup = PopoverCopyBinding.inflate(LayoutInflater.from(requireContext()))
        with(popupWindow) {
            contentView = bindingPopup.root
            isOutsideTouchable = true
            isFocusable = true
            overlapAnchor = true
            setBackgroundDrawable(null)
            bindingPopup.textCopyClick.setOnClickListener {
                saveTextToRAM(text)
                dismiss()
            }
        }

        popupWindow.showAsDropDown(
            incomingView,
            incomingView!!.width / 2,
            -incomingView.height * 2
        )
    }

    private fun saveTextToRAM(text: String) {
        val myClipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        myClipboard.setPrimaryClip(ClipData.newPlainText("Label", text))
    }

    private fun initEmptyUpcomingMessage(visibility: Boolean) {
        with(binding) {
            progressBarHistoryEvents.visibilityIf(false)
            eventsBookedInTheLast10DaysTitle.visibilityIf(!visibility)
            historyEventsRecyclerView.visibilityIf(!visibility)
            noHistoryEventsMessage.visibilityIf(visibility)
        }
    }
}