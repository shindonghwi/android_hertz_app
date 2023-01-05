package mago.apps.hertz.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import mago.apps.domain.model.common.Resource
import mago.apps.domain.usecases.auth.PostLoginUseCase
import mago.apps.domain.usecases.my.PostDeviceUseCase
import mago.apps.hertz.firebase.FCMUtil
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postLoginUseCase: PostLoginUseCase,
    private val postDeviceUseCase: PostDeviceUseCase
) : ViewModel() {

    private val _login = MutableStateFlow(HomeState())
    val login: StateFlow<HomeState> = _login

    suspend fun requestLogin(id: String, password: String) {
        postLoginUseCase(id, password).onEach {
            when (it) {
                is Resource.Loading -> {
                    _login.value = HomeState(isLoading = true)
                }
                is Resource.Error -> {
                    _login.value = HomeState(error = it.message.toString())
                }
                is Resource.Success -> {
                    _login.value = HomeState(data = it.data)
                    postDevice()
                }
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

    private fun postDevice() {
        FCMUtil.getToken { token ->
            coroutineScopeOnDefault {
                postDeviceUseCase(token).collect()
            }
        }
    }
}