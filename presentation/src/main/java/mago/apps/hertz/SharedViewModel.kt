package mago.apps.hertz

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mago.apps.data.constants.HEADER_AUTH_VALUE
import mago.apps.hertz.firebase.FCMUtil
import javax.annotation.Nonnull
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val fcmUtilInstance: FCMUtil
) : ViewModel() {

    fun updateToken(@Nonnull token: String) {
        HEADER_AUTH_VALUE = token
        fcmUtilInstance.registerToken()
    }

    fun removeToken() {
        HEADER_AUTH_VALUE = ""
    }
}