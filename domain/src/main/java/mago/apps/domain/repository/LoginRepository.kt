package mago.apps.domain.repository

import mago.apps.domain.model.Res
import mago.apps.domain.model.login.Login

interface LoginRepository {
    fun postLogin(id: String, password: String): Result<Res<Login>>
}