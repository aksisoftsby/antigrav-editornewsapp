package id.editor.newsapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("count") val count: Int = 0,
    @SerializedName("description") val description: String = "",
    @SerializedName("link") val link: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("slug") val slug: String = "",
    @SerializedName("taxonomy") val taxonomy: String = ""
) : Parcelable
