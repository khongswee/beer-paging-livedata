package com.kho.beerpaginglivedata.features.domain

import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult

/**
 * Created by Administrator on 9/22/18.
 */
class BeerUseCase(private val repository: BeerRepository) {
    fun excute(): GroupActionLoadBeer<BeerResult> {
        return repository.loadBeer()
    }

    fun loadRefresh(){
        repository.loadRefresh()
    }
    fun loadRetry(){
        repository.loadRetry()
    }
}