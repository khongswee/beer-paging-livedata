package com.kho.beerpaginglivedata.data.remote

import com.kho.beerpaginglivedata.data.model.BeerModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerApi {

    @GET("products")
    fun getBeersList(@Query("page")page:Int):Single<BeerModel>

    @GET("products")
    fun getBeersListAfter(@Query("page")page:Int):Single<BeerModel>

}