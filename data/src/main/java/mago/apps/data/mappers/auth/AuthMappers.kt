package mago.apps.data.mappers.auth

import mago.apps.data.network.model.auth.LoginDTO
import mago.apps.domain.model.auth.Login

fun LoginDTO.toDomain(): Login {
    return Login(token)
}