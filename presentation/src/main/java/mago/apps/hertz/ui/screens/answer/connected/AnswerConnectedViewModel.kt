package mago.apps.hertz.ui.screens.answer.connected

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mago.apps.domain.model.common.Resource
import mago.apps.domain.usecases.answer.GetAnswerConnectedInfoUseCase
import mago.apps.domain.usecases.question.DelLikeUseCase
import mago.apps.domain.usecases.question.PostLikeUseCase
import mago.apps.hertz.ui.utils.recorder.CountUpTimer
import javax.inject.Inject


@HiltViewModel
class AnswerConnectedViewModel @Inject constructor(
    private val getAnswerConnectedInfoUseCase: GetAnswerConnectedInfoUseCase,
    private val postLikeUseCase: PostLikeUseCase,
    private val delLikeUseCase: DelLikeUseCase,
) : ViewModel() {

    private val _answerConnectedState = MutableStateFlow(AnswerConnectedState())
    val answerConnectedState: StateFlow<AnswerConnectedState> = _answerConnectedState

    private val _errorDialog = MutableStateFlow<String>("")
    val errorDialog: StateFlow<String> = _errorDialog

    suspend fun getAnswerConnectedInfo(answerSeq: Int) {
        getAnswerConnectedInfoUseCase(answerSeq).onEach {
            when (it) {
                is Resource.Loading -> {
                    _errorDialog.emit("")
                    _answerConnectedState.value = AnswerConnectedState(
                        isLoading = mutableStateOf(true),
                    )
                }
                is Resource.Error -> {
                    _errorDialog.emit(it.message.toString())
                    _answerConnectedState.value = AnswerConnectedState(
                        isLoading = mutableStateOf(false),
                        isErrorState = mutableStateOf(true),
                        error = it.message.toString()
                    )
                }
                is Resource.Success -> {
                    _answerConnectedState.value = AnswerConnectedState(
                        isLoading = mutableStateOf(false),
                        isSuccessState = mutableStateOf(true),
                        data = it.data
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    /** 좋아요, 좋아요 취소*/
    suspend fun postLike(questionSeq: Int) = postLikeUseCase(questionSeq).launchIn(viewModelScope)
    suspend fun delLike(questionSeq: Int) = delLikeUseCase(questionSeq).launchIn(viewModelScope)

    /** 음성 플레이, 정지 */
    var myMediaPlayer: MediaPlayer? = null
    var opponentMediaPlayer: MediaPlayer? = null

    private val _isMyPlaying = MutableStateFlow(false)
    val isMyPlaying = _isMyPlaying

    private val _isOpponentPlaying = MutableStateFlow(false)
    val isOpponentPlaying = _isOpponentPlaying

    private fun updatePlayingState(flag: Boolean, isMe: Boolean) {
        if (isMe) {
            _isMyPlaying.value = flag
        } else {
            _isOpponentPlaying.value = flag
        }
    }

    fun initPlayer(context: Context, audioUrl: Uri, isMe: Boolean) {
        if (isMe) {
            if (myMediaPlayer == null) {
                myMediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
                    )
                    setOnCompletionListener {
                        audioReset(isMe)
                    }
                    setDataSource(context, audioUrl)
                    prepare()
                }
            }
        } else {
            if (opponentMediaPlayer == null) {
                opponentMediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
                    )
                    setOnCompletionListener {
                        audioReset(isMe)
                    }
                    setDataSource(context, audioUrl)
                    prepare()
                }
            }
        }
    }

    fun audioReset(isMe: Boolean) {
        if (isMe) {
            myMediaPlayer?.run {
                if (this.isPlaying) {
                    pause()
                    seekTo(0)
                }
                updatePlayingState(false, isMe)
            }
        } else {
            opponentMediaPlayer?.run {
                if (this.isPlaying) {
                    pause()
                    seekTo(0)
                }
                updatePlayingState(false, isMe)
            }
        }
    }

    fun audioStart(isMe: Boolean) {
        if (isMe) {
            myMediaPlayer?.start()
            updatePlayingState(true, isMe)
        } else {
            opponentMediaPlayer?.start()
            updatePlayingState(true, isMe)
        }
    }

    fun audioClear() {
        myMediaPlayer?.run {
            stop()
            release()
            null
        }
        opponentMediaPlayer?.run {
            stop()
            release()
            null
        }
    }

    private val countUpTimer = CountUpTimer()
    fun getTime(duration: Int?) = countUpTimer.timeToString(duration ?: 0)

}