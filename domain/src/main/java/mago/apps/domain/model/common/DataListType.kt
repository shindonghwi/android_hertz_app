package mago.apps.domain.model.common

data class DataListType<T>(
    val page: PageInfo,
    val count: Int,
    val items: List<T>
)

data class PageInfo(
    val offsetTime: Long? = null,
    val size: Int,
    val totalItems: Int,
    val totalPages: Int,
    val currentPage: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val isFirst: Boolean,
    val isLast: Boolean,
)