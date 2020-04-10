package vn.vistark.oscarassistant.core.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitClient {
    companion object {
        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit? {
            val httpClient = OkHttpClient.Builder()
//            httpClient.addInterceptor { chain ->
//                val request = chain.request().newBuilder().addHeader(
//                    "Authorization",
//                    Constants.token
//                ).build()
//                chain.proceed(request)
//            }
            httpClient.connectTimeout(60, TimeUnit.SECONDS)
            httpClient.readTimeout(60, TimeUnit.SECONDS)
            retrofit = Retrofit.Builder()
                .baseUrl("http://149.28.145.107:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            return retrofit
        }
    }
}