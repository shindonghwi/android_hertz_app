package mago.apps.data.mappers

import mago.apps.data.network.model.LoginDTO
import mago.apps.domain.model.login.Login

fun LoginDTO.toDomain(): Login {
    return Login(token)
}