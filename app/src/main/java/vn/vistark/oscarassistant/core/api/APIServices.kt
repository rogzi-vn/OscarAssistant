package vn.vistark.oscarassistant.core.api

import WikipediaDefineResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

public interface APIServices {

    @GET("https://vi.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1")
    fun getWikiDefineDescriptionInfo(@Query("titles") titles: String): Call<WikipediaDefineResponse>

    @GET("http://vi.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&pithumbsize=512")
    fun getWikiDefineThumbnail(@Query("titles") titles: String): Call<WikipediaDefineResponse>
}