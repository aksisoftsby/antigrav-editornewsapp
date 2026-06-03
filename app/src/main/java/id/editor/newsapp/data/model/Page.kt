package id.editor.newsapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Page(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("date") val date: String = "",
    @SerializedName("modified") val modified: String = "",
    @SerializedName("slug") val slug: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("link") val link: String = "",
    @SerializedName("title") val title: RenderedContent = RenderedContent(),
    @SerializedName("content") val content: RenderedContent = RenderedContent(),
    @SerializedName("excerpt") val excerpt: RenderedContent = RenderedContent(),
    @SerializedName("featured_media") val featuredMediaId: Int = 0,
    @SerializedName("parent") val parent: Int = 0,
    @SerializedName("menu_order") val menuOrder: Int = 0,
    @SerializedName("_embedded") val embedded: Embedded? = null
) : Parcelable {
    fun getFeaturedImageUrl(): String? {
        return embedded?.featuredMedia?.firstOrNull()?.firstOrNull()?.sourceUrl
    }
}
