package id.editor.newsapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Post(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("date") val date: String = "",
    @SerializedName("date_gmt") val dateGmt: String = "",
    @SerializedName("modified") val modified: String = "",
    @SerializedName("slug") val slug: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("link") val link: String = "",
    @SerializedName("title") val title: RenderedContent = RenderedContent(),
    @SerializedName("content") val content: RenderedContent = RenderedContent(),
    @SerializedName("excerpt") val excerpt: RenderedContent = RenderedContent(),
    @SerializedName("author") val authorId: Int = 0,
    @SerializedName("featured_media") val featuredMediaId: Int = 0,
    @SerializedName("comment_status") val commentStatus: String = "",
    @SerializedName("categories") val categories: List<Int> = emptyList(),
    @SerializedName("tags") val tags: List<Int> = emptyList(),
    @SerializedName("_embedded") val embedded: @RawValue Embedded? = null
) : Parcelable {
    fun getFeaturedImageUrl(): String? {
        return embedded?.featuredMedia?.firstOrNull()?.sourceUrl
    }

    fun getAuthorName(): String {
        return embedded?.authors?.firstOrNull()?.name ?: "Unknown"
    }

    fun getAuthorAvatarUrl(): String? {
        return embedded?.authors?.firstOrNull()?.avatarUrls?.url96
    }

    fun getCategoryNames(): List<String> {
        return embedded?.terms?.flatMap { termList ->
            termList.filter { it.taxonomy == "category" }.map { it.name }
        } ?: emptyList()
    }

    fun getTagNames(): List<String> {
        return embedded?.terms?.flatMap { termList ->
            termList.filter { it.taxonomy == "post_tag" }.map { it.name }
        } ?: emptyList()
    }
}

@Parcelize
data class RenderedContent(
    @SerializedName("rendered") val rendered: String = "",
    @SerializedName("protected") val protected_: Boolean = false
) : Parcelable
