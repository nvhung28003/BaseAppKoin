package com.example.baseappkoin.di


import com.example.baseappkoin.BuildConfig
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.example.baseappkoin.data.authorization.AuthorizationService
import com.example.baseappkoin.domain.remote.interceptor.AccessTokenInterceptor
import com.example.baseappkoin.domain.remote.interceptor.AccessTokenProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


const val TIME_OUT = 60L
const val BASE_URL = "BASE_URL"

val networkModule = module {
    single(createdAtStart = true) { createOkHttpClient(get()) }
    single(createdAtStart = true) { AccessTokenProvider(get()) }
    factory { createConverterFactory() }
    single { Gson() }
    factory { createAccessTokenInterceptor(get()) }
    factory { createService<AuthorizationService>(get(), get()) }

}

private fun createOkHttpClient(accessTokenInterceptor: AccessTokenInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addNetworkInterceptor(createHeaderInterceptor())
        .addInterceptor(createHttpLoggingInterceptor())
        .addInterceptor(accessTokenInterceptor)
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .build()
}

private fun createHttpLoggingInterceptor(): Interceptor {
    return HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }
}

private fun createAccessTokenInterceptor(
    accessTokenProvider: AccessTokenProvider
): AccessTokenInterceptor {
    return AccessTokenInterceptor(accessTokenProvider)
}

private fun createHeaderInterceptor(): Interceptor {
    return Interceptor { chain ->
        val timezone = TimeZone
            .getDefault()
            .getDisplayName(false, TimeZone.SHORT, Locale.ENGLISH)
            .substring(3)
        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("Accept", "text/plain")
            .addHeader("Accept", "*/*")
            .addHeader("Timezone", timezone)
            .build()
        chain.proceed(newRequest)
    }
}

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private fun createConverterFactory(): MoshiConverterFactory {
    return MoshiConverterFactory.create(moshi)
}

private inline fun <reified T> createService(
    okHttpClient: OkHttpClient,
    converterFactory: MoshiConverterFactory,
    baseUrl: String = BASE_URL
): T {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()
        .create(T::class.java)
}