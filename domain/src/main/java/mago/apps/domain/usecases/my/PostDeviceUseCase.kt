package mago.apps.domain.usecases.my

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mago.apps.domain.model.common.Resource
import mago.apps.domain.repository.MyRepository
import javax.inject.Inject

class PostDeviceUseCase @Inject constructor(private val myRepository: MyRepository) {

    suspend operator fun invoke(deviceToken: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val response = myRepository.postDevice(deviceToken)
            when (response.status) {
                200 -> emit(Resource.Success(response.message, response.data))
                else -> emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.toString()))
        }
    }

}