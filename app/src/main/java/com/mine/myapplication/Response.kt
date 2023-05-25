package com.mine.myapplication

data class Response(
	val response: List<ResponseItem?>? = null
)

data class Position(
	val latitude: Any? = null,
	val longitude: Any? = null
)

data class Links(
	val portfolio: String? = null,
	val self: String? = null,
	val html: String? = null,
	val photos: String? = null,
	val likes: String? = null,
	val download: String? = null,
	val downloadLocation: String? = null
)

data class Exif(
	val exposureTime: String? = null,
	val aperture: String? = null,
	val focalLength: String? = null,
	val iso: Int? = null,
	val model: String? = null,
	val make: String? = null
)

data class ResponseItem(
	val urls: Urls? = null,
	val id: String? = null
)

data class CurrentUserCollectionsItem(
	val coverPhoto: Any? = null,
	val updatedAt: String? = null,
	val lastCollectedAt: String? = null,
	val id: Int? = null,
	val title: String? = null,
	val publishedAt: String? = null,
	val user: Any? = null
)

data class Urls(
	val small: String? = null,
	val thumb: String? = null,
	val raw: String? = null,
	val regular: String? = null,
	val full: String? = null
)

data class Location(
	val country: String? = null,
	val city: String? = null,
	val name: String? = null,
	val position: Position? = null
)

data class User(
	val totalPhotos: Int? = null,
	val twitterUsername: String? = null,
	val bio: String? = null,
	val totalLikes: Int? = null,
	val portfolioUrl: String? = null,
	val updatedAt: String? = null,
	val name: String? = null,
	val location: String? = null,
	val totalCollections: Int? = null,
	val links: Links? = null,
	val id: String? = null,
	val instagramUsername: String? = null,
	val username: String? = null
)

