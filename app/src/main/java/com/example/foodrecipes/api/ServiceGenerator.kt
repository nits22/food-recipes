package com.example.foodrecipes.api

import android.os.Build
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


const val BASE_URL = "https://recipesapi.herokuapp.com/"
const val API_KEY = ""
const val FIRST_PAGE = 1
const val POST_PER_PAGE = 30

object ServiceGenerator {


    private val logger  = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    // Create a Custom Interceptor to apply Headers application wide
    val headerInterceptor = object: Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {

            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("key", API_KEY)
                .build()

            var request = chain.request()

            request = request.newBuilder()
                .url(url)
                .addHeader("x-device-type", Build.DEVICE)
                .addHeader("Accept-Language", Locale.getDefault().language)
                .build()

            val response = chain.proceed(request)
            return response
        }
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(logger)
        .callTimeout(30, TimeUnit.SECONDS)

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build()).build()

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }
}