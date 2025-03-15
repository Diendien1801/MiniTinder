package com.hd.minitinder.navigation


enum class Screen{
    HOME,
    REGISTER,
    LOGIN,
    PROFILE,
    MAIN,
    PAYMENT,
    PAYMENTQR,
    PAYMENTSUCCESS,
    SWIPE
}

sealed class NavigationItem (val route: String)
{
    object Home: NavigationItem(Screen.HOME.name)
    object Register: NavigationItem(Screen.REGISTER.name)
    object Login: NavigationItem(Screen.LOGIN.name)
    object Profile: NavigationItem(Screen.PROFILE.name)
    object Main: NavigationItem(Screen.MAIN.name)
    object Payment: NavigationItem(Screen.PAYMENT.name)
    object PaymentQR : NavigationItem("${Screen.PAYMENTQR.name}/{payment}") {
        fun createRoute(payment: String) = "${Screen.PAYMENTQR.name}/$payment"
    }
    object PaymentSuccess: NavigationItem(Screen.PAYMENTSUCCESS.name)
    object Swipe: NavigationItem(Screen.SWIPE.name)

}