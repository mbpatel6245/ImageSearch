package com.mbpatel.imagesearch.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


/**
 * Imgur API communication setup via Retrofit.
 */
interface ServiceGenerator {
    /**
     * Get Images from search
     */
    @GET("3/gallery/search/{page}")
    suspend fun searchImages(
        @Path(value = "page", encoded = true) page:Int, @Query("q") keyword:String,@Query("q_type") type:String
    ): SearchResponse

    companion object {
        private const val BASE_URL = "https://api.imgur.com/"
        private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        fun create(): ServiceGenerator {
            httpClient.connectTimeout(60, TimeUnit.SECONDS)
            httpClient.writeTimeout(60, TimeUnit.SECONDS)
            httpClient.readTimeout(90, TimeUnit.SECONDS)

            httpClient.addInterceptor {
                val original: Request = it.request()

                val request: Request =
                    original.newBuilder().header("Authorization", "Client-ID 137cda6b5008a7c")
                        .build()

                it.proceed(request)
            }

            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logger)

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ServiceGenerator::class.java)
        }
    }
}
