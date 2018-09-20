package com.kho.beerpaginglivedata.features.beerlist.data.datasource.positional

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.kho.beerpaginglivedata.data.local.ServiceLocal
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult

class BeerPositionDataFactory(val serviceLocal: ServiceLocal): DataSource.Factory<Int, BeerResult>() {
    val datasource = MutableLiveData<BeerPositionalDataSource>()

    override fun create(): DataSource<Int, BeerResult> {
        val beerDatasource = BeerPositionalDataSource(serviceLocal)
        datasource.postValue(beerDatasource)
        return beerDatasource
    }
}