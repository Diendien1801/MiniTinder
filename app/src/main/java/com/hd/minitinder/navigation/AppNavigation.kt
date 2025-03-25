package com.hd.minitinder.navigation


enum class Screen{
    HOME,
    REGISTER,
    LOGIN,
    PROFILE,
    EDITPROFILE,
    EDITINTEREST,
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
    HISTORY,
    WELCOME,
    FIRSTNAME,
    BIRTHDAY,
    GENDERSELECTION,
    HOMETOWN,
    BIOSELECTION,
    INTERESTSELECTION,
}

sealed class NavigationItem (val route: String)
{
    object Home: NavigationItem(Screen.HOME.name)
    object Register: NavigationItem(Screen.REGISTER.name)
    object Login: NavigationItem(Screen.LOGIN.name)
    object Profile: NavigationItem(Screen.PROFILE.name)
    object EditProfile: NavigationItem(Screen.EDITPROFILE.name)
    object EditInterest: NavigationItem(Screen.EDITINTEREST.name)
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
    object Welcome: NavigationItem(Screen.WELCOME.name)
    object FirstName: NavigationItem(Screen.FIRSTNAME.name)
    object Birthday: NavigationItem(Screen.BIRTHDAY.name)
    object GenderSelection: NavigationItem(Screen.GENDERSELECTION.name)
    object HomeTown: NavigationItem(Screen.HOMETOWN.name)
    object BioSelection: NavigationItem(Screen.BIOSELECTION.name)
    object InterestSelection: NavigationItem(Screen.INTERESTSELECTION.name)
}