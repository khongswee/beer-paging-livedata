package com.kho.beerpaginglivedata.features.beerlist.data.datasource.itemkey

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.kho.beerpaginglivedata.data.local.ServiceLocal
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult

class BeerItemKeyDataDataFactory (val serviceLocal: ServiceLocal): DataSource.Factory<Int,BeerResult>() {
    val datasource = MutableLiveData<BeerItemKeyDataSource>()
    override fun create(): DataSource<Int, BeerResult> {
        val beerDatasource =  BeerItemKeyDataSource(serviceLocal)
        datasource.postValue(beerDatasource)
        return beerDatasource
    }
}