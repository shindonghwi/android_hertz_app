package mago.apps.hertz.ui.utils.recorder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.core.app.ActivityCompat
import mago.apps.hertz.ui.utils.scope.coroutineScopeOnIO
import java.io.File
import java.io.FileFilter
import java.io.FileOutputStream
import java.io.IOException


class PcmRecorder {
    val TAG = "MEDIA_RECORDER"

    private val mAudioSource: Int = MediaRecorder.AudioSource.MIC
    private val mSampleRate = 16000
    private val mChannelCount: Int = AudioFormat.CHANNEL_IN_MONO
    private val mAudioFormat: Int = AudioFormat.ENCODING_PCM_16BIT
    private val mBufferSize =
        AudioRecord.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat) * 2
    private var mAudioRecord: AudioRecord? = null
    private var fos: FileOutputStream? = null

    private var file: File? = null
    private var isRecording = false
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
    }

    fun start() {
        mAudioRecord?.run {
            isRecording = true
            startRecording()
            getFrame()
            countUpTimer.start()
        }
    }

    fun stop() {
        mAudioRecord?.run {
            isRecording = false
            countUpTimer.remove()
            stop()
            release()
            null
        }
        closeFos()
    }

    fun getCurrentTime() = countUpTimer.getTime()

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

        removeFileFromFilePath("wav") // filePath에 생성되었던 wav 파일 삭제
        removeFileFromFilePath("zip") // filePath에 생성되었던 zip 파일 삭제

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

    private fun removeFileFromFilePath(ext: String){
        val path = defaultFilePath
        val file = File(path)

        file.listFiles()?.let { fList ->
            repeat(fList.size){
                if (fList[it].path.endsWith(".${ext}")){
                    fList[it].delete()
                }
            }
        }
    }

}
