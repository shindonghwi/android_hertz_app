package mago.apps.hertz.ui.utils.recorder

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault

class CountUpTimer {

    private var startHTime = 0L
    private var updatedTime = 0L
    private var timeSwapBuff = 0L
    private var timeInMilliseconds = 0L
    private var _currentTime =  MutableStateFlow<String>("")
    var currentTime: StateFlow<String> = _currentTime
    private val customHandler: Handler = Handler(Looper.getMainLooper())

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startHTime
            updatedTime = timeSwapBuff + timeInMilliseconds
            var secs = (updatedTime / 1000).toInt()
            val mins = secs / 60
            secs %= 60
            coroutineScopeOnDefault {
                _currentTime.emit("${String.format("%02d", mins)}:${String.format("%02d", secs)}")
            }
            customHandler.postDelayed(this, 0)
        }
    }

    fun start(){
        startHTime = SystemClock.uptimeMillis()
        customHandler.postDelayed(updateTimerThread, 0)
    }

    fun remove(){
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }

    fun getTime() = currentTime

}