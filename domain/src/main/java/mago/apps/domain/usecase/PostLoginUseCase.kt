package mago.apps.domain.usecase

import mago.apps.domain.model.Res
import mago.apps.domain.model.login.Login
import mago.apps.domain.repository.LoginRepository
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(private val repository: LoginRepository) {
    fun execute(id: String, password: String): Result<Res<Login>> = repository.postLogin(id, password)
}