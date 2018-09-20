package com.kho.beerpaginglivedata.features.beerlist.data.remote_service

import com.kho.beerpaginglivedata.base_data.ServiceProvider
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerModel
import io.reactivex.Single

/**
 * Created by Administrator on 9/20/18.
 */
class BeerService : BeerApi {
    private val api = ServiceProvider.get().provideRetrofit().create(BeerApi::class.java)
    override fun getBeersList(page: Int): Single<BeerModel> = api.getBeersList(page)
    override fun getBeersListAfter(page: Int): Single<BeerModel> = api.getBeersListAfter(page)

}