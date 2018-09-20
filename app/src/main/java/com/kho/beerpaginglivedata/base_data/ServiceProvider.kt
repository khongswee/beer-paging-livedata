package com.kho.beerpaginglivedata.base_data

import com.kho.beerpaginglivedata.BuildConfig
import com.kho.beerpaginglivedata.features.beerlist.data.remote_service.BeerApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceProvider {

    companion object {
        var serviceProvider: ServiceProvider? = null
        fun get(): ServiceProvider {
            if (serviceProvider == null) {
                serviceProvider = ServiceProvider()
            }
            return serviceProvider!!
        }
    }

    fun provideBeerService(): BeerApi = provideRetrofit().create(BeerApi::class.java)


    fun provideRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("http://lcboapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(provideClient())
            .build()


    private fun provideClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = (if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        return OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
    }
}