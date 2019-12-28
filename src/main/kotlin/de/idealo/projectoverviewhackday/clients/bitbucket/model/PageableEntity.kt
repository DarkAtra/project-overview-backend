package de.idealo.projectoverviewhackday.clients.bitbucket.model

data class PageableEntity<T>(
	val size: Long,
	val limit: Long,
	val isLastPage: Boolean,
	val start: Long,
	val nextPageStart: Long,
	val values: List<T>
) {
	fun <R> map(transform: (T) -> R): PageableEntity<R> {
		val (size, limit, isLastPage, start, nextPageStart, _) = this
		return PageableEntity(
			size = size,
			limit = limit,
			isLastPage = isLastPage,
			start = start,
			nextPageStart = nextPageStart,
			values = values.map(transform)
		)
	}
}
