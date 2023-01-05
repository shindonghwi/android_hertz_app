package mago.apps.hertz.firebase

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.collect
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault

object FCMUtil {

    fun getToken(postAction: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            coroutineScopeOnDefault {
                postAction(task.result)
            }
        })
    }

}