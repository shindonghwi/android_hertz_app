package mago.apps.hertz.ui.screens.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mago.apps.domain.model.auth.Login
import mago.apps.domain.model.common.Resource
import mago.apps.domain.usecases.auth.PostLoginUseCase
import mago.apps.hertz.firebase.FCMUtil
import mago.apps.hertz.ui.base.BaseViewModel
import mago.apps.hertz.ui.model.state.UiState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postLoginUseCase: PostLoginUseCase,
    private val fcmUtilInstance: FCMUtil
) : BaseViewModel<Login>() {

    suspend fun requestLogin(id: String, password: String) {
        postLoginUseCase(id, password, fcmUtilInstance.token).onEach {
            when (it) {
                is Resource.Loading -> _uiState.emit(UiState.Loading)
                is Resource.Error -> _uiState.emit(UiState.Error(it.message.toString()))
                is Resource.Success -> _uiState.emit(UiState.Success(data = it.data))
            }
        }.launchIn(viewModelScope)
    }

    private var id: String = ""
    private var pw: String = ""

    fun getId(): String {
        return this.id
    }

    fun getPw(): String {
        return this.pw
    }

    fun updateId(id: String) {
        this.id = id
    }

    fun updatePw(pw: String) {
        this.pw = pw
    }

    fun isExistIdPw(): Boolean = id.isNotEmpty() && pw.isNotEmpty()
}