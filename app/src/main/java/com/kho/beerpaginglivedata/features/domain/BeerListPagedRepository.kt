package com.kho.beerpaginglivedata.features.domain

import android.arch.lifecycle.Transformations
import com.kho.beerpaginglivedata.base_domain.NetworkState
import com.kho.beerpaginglivedata.features.beerlist.data.datasource.pagekey.BeerPageKeyDataFactory
import com.kho.beerpaginglivedata.features.beerlist.data.datasource.pagekey.BeerPageKeyDatasource
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult
import com.kho.beerpaginglivedata.features.beerlist.data.remote_service.BeerApi
import pl.marchuck.pagingexample.data.BeerPagedListProvider

class BeerListPagedRepository(api: BeerApi) : BeerRepository {
    val factoryPage = BeerPageKeyDataFactory(api)
    override fun loadRetry() {
        factoryPage.datasource.value?.loadRetry()
    }

    override fun loadRefresh() {
        factoryPage.datasource.value?.invalidate()
    }

    private val pageSize = 10

    override fun loadBeer(): GroupActionLoadBeer<BeerResult> {
        val networkState = Transformations.switchMap<BeerPageKeyDatasource, NetworkState>(
                factoryPage.datasource, { it.networkState })
        val initialState = Transformations.switchMap<BeerPageKeyDatasource, NetworkState>(
                factoryPage.datasource, { it.initialState })
        val paged = BeerPagedListProvider(factoryPage)
        return GroupActionLoadBeer(paged.provide(), networkState, initialState)
    }
}