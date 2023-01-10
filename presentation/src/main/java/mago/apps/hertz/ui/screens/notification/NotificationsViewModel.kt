package mago.apps.hertz.ui.screens.notification

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import mago.apps.domain.model.my.Notification
import mago.apps.domain.usecases.my.GetNotificationsUseCase
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel() {

    lateinit var lazyListState: LazyListState

    fun initLazyListState(state: LazyListState){
        if (!::lazyListState.isInitialized){
            lazyListState = state
        }
    }

    val notifications: Flow<PagingData<Notification>> =
        getNotificationsUseCase().cachedIn(viewModelScope)

}