package com.hd.minitinder.navigation


enum class Screen{
    HOME,
    REGISTER,
    LOGIN,
    PROFILE,
    MAIN,
    CHAT,
    DETAILCHAT,
}

sealed class NavigationItem (val route: String)
{
    object Home: NavigationItem(Screen.HOME.name)
    object Register: NavigationItem(Screen.REGISTER.name)
    object Login: NavigationItem(Screen.LOGIN.name)
    object Profile: NavigationItem(Screen.PROFILE.name)
    object Main: NavigationItem(Screen.MAIN.name)
    object Chat: NavigationItem(Screen.CHAT.name)
    object DetailChat: NavigationItem(Screen.DETAILCHAT.name)


}