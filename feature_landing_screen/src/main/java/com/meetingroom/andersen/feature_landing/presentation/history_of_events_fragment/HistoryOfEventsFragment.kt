package com.meetingroom.andersen.feature_landing.presentation.history_of_events_fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core_module.component_manager.XInjectionManager
import com.meeringroom.ui.view.base_classes.BaseFragment
import com.meeringroom.ui.view_utils.visibilityIf
import com.meetingroom.andersen.feature_landing.R
import com.meetingroom.andersen.feature_landing.databinding.FragmentHistoryOfEventsBinding
import com.meetingroom.andersen.feature_landing.databinding.PopoverCopyBinding
import com.meetingroom.andersen.feature_landing.presentation.di.LandingComponent
import javax.inject.Inject

class HistoryOfEventsFragment :
    BaseFragment<FragmentHistoryOfEventsBinding>(FragmentHistoryOfEventsBinding::inflate) {

    private val historyEventsLayoutManager by lazy {
        LinearLayoutManager(context)
    }

    private val eventAdapter by lazy {
        HistoryEventAdapter(historyEventsLayoutManager, { view, text ->
            showCopyPrompt(view, text)
        },
            { text -> openSkypeDialog(text) })
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
        with(viewModel) {
            historyEvents.observe(viewLifecycleOwner) {
                eventAdapter.setData(it)
                initEmptyUpcomingMessage(it.isEmpty())
            }

            isSkypeInstallData.observe(viewLifecycleOwner) {
                initSkype(it.nickname)
            }

            skypeNotInstall.observe(viewLifecycleOwner) {
                goToMarket()
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            historyEventsRecyclerView.apply {
                setHasFixedSize(true)
                adapter = eventAdapter
                layoutManager = historyEventsLayoutManager
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

    private fun openSkypeDialog(nickname: String) {
        viewModel.openSkypeDialog(nickname)
    }

    private fun initSkype(skyNickname: String) {
        val skyIntent = Intent(Intent.ACTION_VIEW)
        skyIntent.data =
            Uri.parse(
                getString(R.string.skype_for_URI) + skyNickname +
                        getString(R.string.chat_skype_forURI)
            )
        requireContext().startActivity(skyIntent)
    }

    private fun goToMarket() {
        val marketUri = Uri.parse(getString(R.string.skype_marketUri))
        val myIntent = Intent(Intent.ACTION_VIEW, marketUri)
        myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireContext().startActivity(myIntent)
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