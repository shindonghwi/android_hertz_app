package mago.apps.hertz

import androidx.lifecycle.ViewModel
import javax.annotation.Nonnull

data class Token(
    val value: String?
)

class SharedViewModel : ViewModel() {

    private var token: Token = Token(null)

    fun updateToken(@Nonnull token: String) {
        this.token = this.token.copy(value = token)
    }

    fun removeToken() {
        this.token = this.token.copy(value = null)
    }

}