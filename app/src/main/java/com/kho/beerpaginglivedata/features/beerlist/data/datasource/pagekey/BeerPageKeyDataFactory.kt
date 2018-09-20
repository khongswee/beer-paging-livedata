package com.kho.beerpaginglivedata.features.beerlist.data.datasource.pagekey

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult
import com.kho.beerpaginglivedata.features.beerlist.data.remote_service.BeerApi
import io.reactivex.schedulers.Schedulers

class BeerPageKeyDataFactory(val api: BeerApi): DataSource.Factory<Int, BeerResult>() {
    val datasource = MutableLiveData<BeerPageKeyDatasource>()

    override fun create(): DataSource<Int, BeerResult> {
        val beerDatasource = BeerPageKeyDatasource(api, Schedulers.io())
        datasource.postValue(beerDatasource)
        return beerDatasource
    }
}