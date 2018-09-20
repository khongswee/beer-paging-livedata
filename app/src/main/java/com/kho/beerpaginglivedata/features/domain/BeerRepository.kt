package com.kho.beerpaginglivedata.features.domain

import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult

interface BeerRepository {
    fun loadBeer(): GroupActionLoadBeer<BeerResult>
    fun loadRetry()
    fun loadRefresh ()
}