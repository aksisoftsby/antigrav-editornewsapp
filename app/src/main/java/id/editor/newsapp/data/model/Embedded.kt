package id.editor.newsapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Embedded(
    @SerializedName("wp:featuredmedia") val featuredMedia: List<List<MediaItem>>? = null,
    @SerializedName("author") val authors: List<Author>? = null,
    @SerializedName("wp:term") val terms: List<List<WpTerm>>? = null
) : Parcelable

@Parcelize
data class MediaItem(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("source_url") val sourceUrl: String = "",
    @SerializedName("alt_text") val altText: String = "",
    @SerializedName("media_details") val mediaDetails: MediaDetails? = null
) : Parcelable

@Parcelize
data class MediaDetails(
    @SerializedName("width") val width: Int = 0,
    @SerializedName("height") val height: Int = 0,
    @SerializedName("sizes") val sizes: MediaSizes? = null
) : Parcelable

@Parcelize
data class MediaSizes(
    @SerializedName("thumbnail") val thumbnail: MediaSize? = null,
    @SerializedName("medium") val medium: MediaSize? = null,
    @SerializedName("medium_large") val mediumLarge: MediaSize? = null,
    @SerializedName("large") val large: MediaSize? = null,
    @SerializedName("full") val full: MediaSize? = null
) : Parcelable

@Parcelize
data class MediaSize(
    @SerializedName("source_url") val sourceUrl: String = "",
    @SerializedName("width") val width: Int = 0,
    @SerializedName("height") val height: Int = 0
) : Parcelable

@Parcelize
data class Author(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("slug") val slug: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("link") val link: String = "",
    @SerializedName("avatar_urls") val avatarUrls: AvatarUrls? = null
) : Parcelable

@Parcelize
data class AvatarUrls(
    @SerializedName("24") val url24: String = "",
    @SerializedName("48") val url48: String = "",
    @SerializedName("96") val url96: String = ""
) : Parcelable

@Parcelize
data class WpTerm(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("link") val link: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("slug") val slug: String = "",
    @SerializedName("taxonomy") val taxonomy: String = ""
) : Parcelable
