package mago.apps.hertz.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var fcmUtilInstance: FCMUtil

    /** @feature: 메세지가 수신되면 호출됨
     * @author: 2023/01/06 11:47 AM donghwishin
     * @description{
     *
     *  백그라운드 데이터 수신 방법
     *  1. // key 값이 "data" 로 오게되면 메세지에 대한 제어권이 주어진다.
     *     data : {
            title : ""
            body : ""
           }
        2. P version 이상에서 isBackgroundRestricted() 체크
           앱 시스템이 백그라운드 모드로 설정되어 있는경우에는 알림 수신이 불가능 하다.

     * }
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.w("ASdasdasd", "onMessageReceived: $message")
    }

    /** @feature: 등록된 토큰 수신
     * @author: 2023/01/06 11:46 AM donghwishin
     * @description{
     *  - 앱이 새기기에서 복원되는 경우
     *  - 앱 제거/재설치를 한 경우
     *  - 앱 데이터를 지우는 경우에 수신
     *  - (참고) Mago App 에서는 로그인 후에 토큰 등록이 가능하다.
     * }
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.w("ASdasdasd", "onNewToken: $token")
        fcmUtilInstance.registerToken()
    }
}