package com.kho.beerpaginglivedata.data.local

import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerModel
import io.reactivex.Single

interface BeerLocal {
    fun getBeerList(offset: Int, limit: Int): Single<BeerModel>
    fun getBeerListBySinceId(since_id: Int, limit: Int): Single<BeerModel>
    fun getBeerListBySinceIdInit(limit: Int): Single<BeerModel>

}