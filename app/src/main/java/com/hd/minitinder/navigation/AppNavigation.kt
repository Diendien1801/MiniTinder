package com.hd.minitinder.navigation


enum class Screen{
    HOME,
    REGISTER,
    LOGIN,
    PROFILE,
    EDITPROFILE,
    PREVIEW,
    ADDIMAGE,
    VIEWPROFILE,
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
    HISTORY
}

sealed class NavigationItem (val route: String)
{
    object Home: NavigationItem(Screen.HOME.name)
    object Register: NavigationItem(Screen.REGISTER.name)
    object Login: NavigationItem(Screen.LOGIN.name)
    object Profile: NavigationItem(Screen.PROFILE.name)
    object EditProfile: NavigationItem(Screen.EDITPROFILE.name)
    object Preview: NavigationItem(Screen.PREVIEW.name)
    object AddImage: NavigationItem(Screen.ADDIMAGE.name)
    object ViewProfile : NavigationItem("viewProfile/{receiverId}") {
        fun createRoute(receiverId: String) = "ViewProfile/$receiverId"
    }
    object Main: NavigationItem(Screen.MAIN.name)
    object ResetPass: NavigationItem(Screen.RESETPASS.name)
    object AuthenOption: NavigationItem(Screen.AUTHENOPTION.name)
    object Chat: NavigationItem(Screen.CHAT.name)
    object DetailChat : NavigationItem("detail_chat/{chatId}/{receiverId}") {
        fun createRoute(chatId: String, receiverId: String) = "detail_chat/$chatId/$receiverId"
    }
    object Swipe: NavigationItem(Screen.SWIPE.name)
    object PaymentQR : NavigationItem("${Screen.PAYMENTQR.name}/{payment}") {
        fun createRoute(payment: String) = "${Screen.PAYMENTQR.name}/$payment"
    }
    object PaymentSuccess: NavigationItem(Screen.PAYMENTSUCCESS.name)
    object PaymentOption: NavigationItem(Screen.PAYMENTOPTION.name)
    object TinderGold: NavigationItem(Screen.TINDERGOLD.name)
    object History: NavigationItem(Screen.HISTORY.name)



}