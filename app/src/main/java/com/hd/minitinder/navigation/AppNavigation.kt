package com.hd.minitinder.navigation


enum class Screen{
    HOME,
    REGISTER,
    LOGIN,
    PROFILE,
    MAIN,
    RESETPASS,
    AUTHENOPTION,
    CHAT,
    DETAILCHAT,
    SWIPE,
    PAYMENTQR,
    PAYMENTSUCCESS,
    PAYMENTOPTION,
    TINDERGOLD,
}

sealed class NavigationItem (val route: String)
{
    object Home: NavigationItem(Screen.HOME.name)
    object Register: NavigationItem(Screen.REGISTER.name)
    object Login: NavigationItem(Screen.LOGIN.name)
    object Profile: NavigationItem(Screen.PROFILE.name)
    object Main: NavigationItem(Screen.MAIN.name)
    object ResetPass: NavigationItem(Screen.RESETPASS.name)
    object AuthenOption: NavigationItem(Screen.AUTHENOPTION.name)
    object Chat: NavigationItem(Screen.CHAT.name)
    object DetailChat: NavigationItem(Screen.DETAILCHAT.name)
    object Swipe: NavigationItem(Screen.SWIPE.name)
    object PaymentQR: NavigationItem(Screen.PAYMENTQR.name)
    object PaymentSuccess: NavigationItem(Screen.PAYMENTSUCCESS.name)
    object PaymentOption: NavigationItem(Screen.PAYMENTOPTION.name)
    object TinderGold: NavigationItem(Screen.TINDERGOLD.name)



}