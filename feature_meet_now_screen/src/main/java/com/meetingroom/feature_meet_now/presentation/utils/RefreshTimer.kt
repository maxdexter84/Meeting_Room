package com.meetingroom.feature_meet_now.presentation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val REFRESH_TIME: Long = 60000

class RefreshTimer(private val scope: CoroutineScope) {
    private var refreshTimerJob: Job? = null

    fun start(onRefresh: () -> Unit) {
        stop()
        refreshTimerJob = scope.launch {
            while (true) {
                delay(REFRESH_TIME)
                onRefresh()
            }
        }
    }

    fun stop() {
        refreshTimerJob?.cancel()
        refreshTimerJob = null
    }
}