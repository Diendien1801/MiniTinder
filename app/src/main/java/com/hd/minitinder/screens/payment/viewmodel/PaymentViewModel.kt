package com.hd.minitinder.screens.payment.viewmodel


import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class PaymentViewModel : ViewModel() {
    val currentUser = FirebaseAuth.getInstance().currentUser

}