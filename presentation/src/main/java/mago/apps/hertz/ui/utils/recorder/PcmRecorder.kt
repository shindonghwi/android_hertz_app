package mago.apps.hertz.ui.utils.recorder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.*
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import mago.apps.hertz.ui.utils.permission.PermissionsHandler
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnDefault
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnIO
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/** @link https://aroundck.tistory.com/7993
 * 오디오 관련 글 정리 잘되어있음. */

class PcmRecorder {
    val TAG = "MEDIA_RECORDER"

    var audioFocusAuth: MutableStateFlow<AudioFocusEvent> =
        MutableStateFlow(AudioFocusEvent.IDLE)
    var isRecording = false
    private val mAudioSource: Int = MediaRecorder.AudioSource.MIC
    private val mSampleRate = 16000
    private val mChannelCount: Int = AudioFormat.CHANNEL_IN_MONO
    private val mAudioFormat: Int = AudioFormat.ENCODING_PCM_16BIT
    private val mBufferSize =
        AudioRecord.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat) * 2
    private var mAudioRecord: AudioRecord? = null

    private var fos: FileOutputStream? = null

    private var file: File? = null
    private val countUpTimer = CountUpTimer()

    private var saveAudioPath: String = ""
    private var saveZipPath: String = ""
    private var timeStamp: String = ""
    private var defaultFilePath: String = ""

    fun createRecorder(context: Context) {

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "createRecorder: not granted permission")
            return
        }

        defaultFilePath = context.filesDir.path
        timeStamp = System.currentTimeMillis().toString()
        saveAudioPath = "${defaultFilePath}/${timeStamp}.wav"
        saveZipPath = "${defaultFilePath}/${timeStamp}.zip"
        createFos()

        mAudioRecord = AudioRecord(
            mAudioSource,
            mSampleRate,
            mChannelCount,
            mAudioFormat,
            mBufferSize
        )

        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
            setAudioAttributes(AudioAttributes.Builder().run {
                setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                build()
            })
            setAcceptsDelayedFocusGain(false)
            setOnAudioFocusChangeListener(
                { p0 ->
                    if (p0 == -1) { // 다른앱에서 포커스를 가져간 경우
                        if (isRecording) {
                            coroutineScopeOnDefault {
                                audioFocusAuth.emit(AudioFocusEvent.UNFOCUSED)
                            }
                        }
                    }
                },
                Handler(Looper.getMainLooper())
            )
            build()
        }

        coroutineScopeOnDefault {
            audioFocusAuth.emit(
                when (audioManager.requestAudioFocus(focusRequest)) {
                    AudioManager.AUDIOFOCUS_REQUEST_FAILED -> AudioFocusEvent.UNFOCUSED
                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> AudioFocusEvent.FOCUSED
                    AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> AudioFocusEvent.UNFOCUSED
                    else -> AudioFocusEvent.UNFOCUSED
                }
            )
        }
    }

    fun start() {
        if (audioFocusAuth.value != AudioFocusEvent.UNFOCUSED) {
            mAudioRecord?.run {
                isRecording = true
                startRecording()
                getFrame()
                countUpTimer.start()
            }
        }
    }

    fun stop() {
        mAudioRecord?.run {
            isRecording = false
            countUpTimer.remove()
            stop()
            release()
        }
        mAudioRecord = null
        closeFos()
    }

    fun getMaxTime() = countUpTimer.MAX_TIME
    fun getZipFile() = saveZipPath
    fun getCurrentTime() = countUpTimer.getTime()
    fun convertTimeToString(time: Int) = countUpTimer.timeToString(time)

    private fun getFrame() {
        coroutineScopeOnIO {
            try {
                do {
                    val bufferInfo = getFrameBuffer()
                    writeFile(bufferInfo.first)
                    bufferInfo.second?.let { if (it < -1) return@let }
                } while (isRecording)
            } catch (e: Exception) {
                Log.e(TAG, "getFrame error: ${e.message}")
            }
        }
    }

    private fun getFrameBuffer(): Pair<ByteArray, Int?> {
        val buf = ByteArray(mBufferSize)

        val byteCount = try {
            mAudioRecord?.read(buf, 0, buf.size)
        } catch (e: Exception) {
            -1
        }
        return Pair(buf, byteCount)
    }

    private fun createFos() {

        removeFileFromFilePath(listOf("wav", "zip")) // filePath에 생성되었던 wav,zip 파일 삭제

        try {
            file = File(saveAudioPath)
            fos = FileOutputStream(file)
            fos?.let {
                PcmToWavConverter.writeWavHeader(
                    it,
                    mChannelCount,
                    mSampleRate,
                    mAudioFormat
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "createFos: error: ${e.message}")
        }
    }

    private fun closeFos() {
        try {
            fos!!.close()
            file?.let { PcmToWavConverter.updateWavHeader(it) }
            ZipUtil.zip(saveAudioPath, saveZipPath)
        } catch (e: IOException) {
            Log.e(TAG, "closeFos: error: ${e.message}")
        } finally {
            if (file?.exists() == true) {
                file?.delete()
            }
        }
    }

    private fun writeFile(readData: ByteArray) {
        try {
            fos?.write(readData, 0, readData.size)
        } catch (e: IOException) {
            Log.e(TAG, "writeFile: error: ${e.message}")
        }
    }

    fun removeFileFromFilePath(ext: List<String>) {
        val path = defaultFilePath
        val file = File(path)

        file.listFiles()?.let { fList ->
            repeat(fList.size) { idx ->

                ext.forEach {
                    if (fList[idx].path.endsWith(".${it}")) {
                        fList[idx].delete()
                    }
                }
            }
        }
    }
}

sealed class AudioFocusEvent {
    object IDLE : AudioFocusEvent()
    object FOCUSED : AudioFocusEvent()
    object UNFOCUSED : AudioFocusEvent()
}