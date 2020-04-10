package vn.vistark.oscarassistant.core.models.wikipedia

import com.google.gson.annotations.SerializedName

data class WikiThumbnail(
    @SerializedName("source") var source: String
)