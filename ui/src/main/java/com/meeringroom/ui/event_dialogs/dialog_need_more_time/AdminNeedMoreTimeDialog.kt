package com.meeringroom.ui.event_dialogs.dialog_need_more_time

import com.meetingroom.ui.R

class AdminNeedMoreTimeDialog: NeedMoreTimeDialog() {
    override fun setExpiredDialogMessage(messageId: Int) {
        super.setExpiredDialogMessage(R.string.session_expired_admin_dialog_message)
    }
}