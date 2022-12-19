package mago.apps.domain.model

data class Res<T>(
    val status: Int,
    val message: String,
    val data: T
)