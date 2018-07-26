package com.kho.beerpaginglivedata.data.remote

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.kho.beerpaginglivedata.data.datasource.pagekey.BeerPageKeyDataFactory
import com.kho.beerpaginglivedata.data.datasource.pagekey.BeerPageKeyDatasource
import com.kho.beerpaginglivedata.data.model.BeerResult

class BeerListPagedRepository( api: BeerApi) : BeerRepository {
    val factoryPage = BeerPageKeyDataFactory(api)
    override fun loadRetry() {
        factoryPage.datasource.value?.loadRetry()
    }

    override fun loadRefresh() {
        factoryPage.datasource.value?.invalidate()
    }

    private val pageSize = 10

    override fun loadBeer(): GroupActionLoadBeer<BeerResult> {
        var beersList: LiveData<PagedList<BeerResult>>
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build()

        val networkState = Transformations.switchMap<BeerPageKeyDatasource, NetworkState>(
                factoryPage.datasource, { it.networkState })
        val initialState = Transformations.switchMap<BeerPageKeyDatasource, NetworkState>(
                factoryPage.datasource, { it.initialState })


        beersList = LivePagedListBuilder<Int, BeerResult>(factoryPage, config).build()

        return GroupActionLoadBeer(beersList, networkState, initialState)
    }
}