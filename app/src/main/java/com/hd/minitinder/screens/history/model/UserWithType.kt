package com.hd.minitinder.screens.history.model

import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.screens.history.viewmodel.HistoryViewModel.NotificationType

data class UserWithType(
    val user: UserModel,
    val type: NotificationType,
    val chatId: String? = null,
)