package mago.apps.hertz.firebase

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.collect
import mago.apps.domain.usecases.my.PostDeviceUseCase
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault
import javax.inject.Inject

class FCMUtil @Inject constructor(
    private val postDeviceUseCase: PostDeviceUseCase
) {
    fun registerToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            coroutineScopeOnDefault { postDeviceUseCase(task.result).collect() }
        })
    }

}