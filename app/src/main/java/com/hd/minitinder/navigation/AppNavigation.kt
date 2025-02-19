package com.hd.minitinder.navigation


enum class Screen{
    HOME,
    REGISTER,
    LOGIN,

}

sealed class NavigationItem (val route: String)
{
    object Home: NavigationItem(Screen.HOME.name)
    object Register: NavigationItem(Screen.REGISTER.name)
    object Login: NavigationItem(Screen.LOGIN.name)
}