package com.kho.beerpaginglivedata.data.datasource.pagekey

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.kho.beerpaginglivedata.data.model.BeerResult
import com.kho.beerpaginglivedata.data.remote.BeerApi
import com.kho.beerpaginglivedata.data.remote.ServiceProvider
import io.reactivex.schedulers.Schedulers

class BeerPageKeyDataFactory(val api: BeerApi): DataSource.Factory<Int, BeerResult>() {
    val datasource = MutableLiveData<BeerPageKeyDatasource>()

    override fun create(): DataSource<Int, BeerResult> {
        val beerDatasource = BeerPageKeyDatasource(api, Schedulers.io())
        datasource.postValue(beerDatasource)
        return beerDatasource
    }
}