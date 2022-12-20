package mago.apps.hertz.ui.screens.home

import mago.apps.domain.model.login.Login

data class HomeState(
    val isLoading: Boolean = false,
    var data: Login? = null,
    val error: String = ""
)
