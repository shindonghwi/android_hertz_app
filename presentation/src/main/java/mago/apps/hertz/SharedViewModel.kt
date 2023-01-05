package mago.apps.hertz

import androidx.lifecycle.ViewModel
import mago.apps.data.constants.HEADER_AUTH_VALUE
import javax.annotation.Nonnull

class SharedViewModel : ViewModel() {

    fun updateToken(@Nonnull token: String) {
        HEADER_AUTH_VALUE = token
    }

    fun removeToken() {
        HEADER_AUTH_VALUE = ""
    }

}