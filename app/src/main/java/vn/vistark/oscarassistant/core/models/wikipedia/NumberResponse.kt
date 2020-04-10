package vn.vistark.oscarassistant.core.models.wikipedia

import android.icu.text.CaseMap
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class NumberResponse(
    @SerializedName("ns") var ns: Long,
    @SerializedName("pageid") var pageid: Long?,
    @SerializedName("title") var title: String,
    @SerializedName("missing") var missing: String,
    @SerializedName("extract") var extract: String?,
    @SerializedName("thumbnail") var thumbnail: WikiThumbnail?
) {
    companion object {
        fun extract(jse: JsonElement): NumberResponse {
            return GsonBuilder().create().fromJson(jse, NumberResponse::class.java)
        }
    }
}