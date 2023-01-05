package mago.apps.hertz

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import mago.apps.data.constants.HEADER_AUTH_VALUE
import mago.apps.domain.usecases.my.PostDeviceUseCase
import mago.apps.hertz.firebase.FCMUtil
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault
import javax.annotation.Nonnull
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val postDeviceUseCase: PostDeviceUseCase
) : ViewModel() {

    fun updateToken(@Nonnull token: String) {
        HEADER_AUTH_VALUE = token
        postDeviceToken()
    }

    fun removeToken() {
        HEADER_AUTH_VALUE = ""
    }

    /** @feature: fcm 토큰을 가져와서 디바이스 등록하는 기능
     * @author: 2023/01/05 7:27 PM donghwishin
    */
    private fun postDeviceToken() {
        FCMUtil.getToken { token ->
            coroutineScopeOnDefault {
                postDeviceUseCase(token).collect()
            }
        }
    }

}