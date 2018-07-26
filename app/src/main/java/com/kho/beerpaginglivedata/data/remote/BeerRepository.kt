package com.kho.beerpaginglivedata.data.remote

import com.kho.beerpaginglivedata.data.model.BeerResult

interface BeerRepository {
    fun loadBeer():GroupActionLoadBeer<BeerResult>
    fun loadRetry()
    fun loadRefresh ()
}