package com.perf

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class HorizontalArtwork(
  val url: String,
  val key: String
)

@JsonClass(generateAdapter = true)
class Video (
  val id: Int,
  val kind: String,
  val title: String
)

enum class Algorithm {
  CLOSEST_MATCH,
  BIGGEST,
  SMALLEST,
}

@JsonClass(generateAdapter = true)
class Recipe (
  val artworkclass: String,
  val width: Int,
  val height: Int,
  val sizeMatchAlgorithm: Algorithm,
  val recipePreferences: List<String>,
  val secureImageUrl: Boolean
)

@JsonClass(generateAdapter = true)
class VideoEntity (
  val videoId: Int,
  val kind: String,
  val title: String,
  val artwork: HorizontalArtwork
)

@JsonClass(generateAdapter = true)
class EntityId (
  val id: Int,
  val kind: String
)

@JsonClass(generateAdapter = true)
class SearchPageVideo (
  val displayString: String,
  val video: Video,
  val entityId: EntityId
)

@JsonClass(generateAdapter = true)
class Edges (
  val node: SearchPageVideo
)

@JsonClass(generateAdapter = true)
class Entities (
  val length: Int,
  val edges: List<Edges>
)

@JsonClass(generateAdapter = true)
class SearchVideoListSection (
  val displayTitle: String,
  val feature: String,
  val kind: String,
  val sectionId: String,
  val trackId: Int,
  val entities: Entities
)

@JsonClass(generateAdapter = true)
class Edges1 (
  val cursor: String,
  val node: SearchVideoListSection
)

@JsonClass(generateAdapter = true)
class PageInfo (
  val endCursor: String,
  val hasNextPage: Boolean
)

@JsonClass(generateAdapter = true)
class Sections (
  val length: Int,
  val edges: List<Edges1>,
  val pageInfo: PageInfo
)

@JsonClass(generateAdapter = true)
class PrequerySearchPage (
  val requestId: String,
  val pageId: String,
  val sessionId: String,
  val expires: Double,
  val searchPageKind: String,
  val sections: Sections
)

@JsonClass(generateAdapter = true)
class Query (
  val prequerySearchPage: PrequerySearchPage
)

@JsonClass(generateAdapter = true)
class Data(val data: Query)