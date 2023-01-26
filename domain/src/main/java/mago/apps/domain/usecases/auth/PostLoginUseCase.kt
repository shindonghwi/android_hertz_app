package mago.apps.domain.usecases.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mago.apps.domain.model.auth.Login
import mago.apps.domain.model.common.Resource
import mago.apps.domain.repository.AuthRepository
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(id: String, password: String, deviceToken: String?): Flow<Resource<Login>> = flow {
        emit(Resource.Loading())
        try {
            val response = authRepository.postLogin(id, password, deviceToken)
            when (response.status) {
                200 -> emit(Resource.Success(response.message, response.data))
                else -> emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.toString()))
        }
    }

}