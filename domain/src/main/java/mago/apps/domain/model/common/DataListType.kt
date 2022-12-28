package mago.apps.domain.model.common

data class DataListType<T>(
    val count: Int,
    val items: List<T>
)