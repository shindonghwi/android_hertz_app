package mago.apps.hertz.ui.screens.episode_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import mago.apps.domain.model.answer.Answer
import mago.apps.domain.usecases.answer.GetAnswerListUseCase
import javax.inject.Inject

@HiltViewModel
class EpisodeListViewModel @Inject constructor(
    getAnswerListUseCase: GetAnswerListUseCase,
) : ViewModel() {

    val getAnswerMyList: Flow<PagingData<Answer>> =
        getAnswerListUseCase(false).cachedIn(viewModelScope)

    val getAnswerOurList: Flow<PagingData<Answer>> =
        getAnswerListUseCase(true).cachedIn(viewModelScope)

}