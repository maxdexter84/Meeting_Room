package com.meetingroom.andersen.feature_landing.history_of_events_fragment.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.meeringroom.ui.view.base_fragment.BaseFragment
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentHistoryOfEventsBinding
import com.meetingroom.andersen.feature_landing.databinding.PopoverCopyBinding
import com.meetingroom.andersen.feature_landing.di.history_of_events_fragment.DaggerHistoryOfEventsFragmentComponent
import com.meetingroom.andersen.feature_landing.history_of_events_fragment.presentation.HistoryOfEventsFragmentViewModel
import javax.inject.Inject


class HistoryOfEventsFragment :
    BaseFragment<FragmentHistoryOfEventsBinding>(FragmentHistoryOfEventsBinding::inflate) {

    private val eventAdapter by lazy {
        HistoryEventAdapter { view, text ->
            showCopyPrompt(view, text)
        }
    }

    @Inject
    lateinit var viewModel: HistoryOfEventsFragmentViewModel

    override fun onAttach(context: Context) {
        DaggerHistoryOfEventsFragmentComponent.builder()
            .build()
            .inject(this)
        super.onAttach(context)
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

        popupWindow.showAsDropDown(incomingView, incomingView!!.width / 2 -   resources.getDrawable(R.drawable.ic_popover_icon).intrinsicWidth /2, -incomingView!!.height * 2)
    }

    private fun saveTextToRAM(text: String) {
        val myClipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip: ClipData = ClipData.newPlainText("Label", text)
        myClipboard.setPrimaryClip(myClip)
    }

    private fun initEmptyUpcomingMessage(visibility: Boolean) {
        with(binding) {
            progressBarHistoryEvents.visibility = View.GONE
            eventsBookedInTheLast10DaysTitle.visibilityIf(!visibility)
            historyEventsRecyclerView.visibilityIf(!visibility)
            noHistoryEventsMessage.visibilityIf(visibility)
        }
    }
}