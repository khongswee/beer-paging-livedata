package com.kho.beerpaginglivedata.data.remote

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.kho.beerpaginglivedata.data.datasource.pagekey.BeerPageKeyDataFactory
import com.kho.beerpaginglivedata.data.datasource.pagekey.BeerPageKeyDatasource
import com.kho.beerpaginglivedata.data.datasource.positional.BeerPositionDataFactory
import com.kho.beerpaginglivedata.data.datasource.positional.BeerPositionalDataSource
import com.kho.beerpaginglivedata.data.local.ServiceLocal
import com.kho.beerpaginglivedata.data.model.BeerResult

class BeerListPositionalRepository(val serviceLocal: ServiceLocal) : BeerRepository {
    val factoryPositional = BeerPositionDataFactory(serviceLocal)

    override fun loadRetry() {
    }

    override fun loadRefresh() {
        factoryPositional.datasource.value?.invalidate()
    }

    private val pageSize = 10

    override fun loadBeer(): GroupActionLoadBeer<BeerResult> {
        var beersList: LiveData<PagedList<BeerResult>>
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build()
        val networkState = Transformations.switchMap<BeerPositionalDataSource, NetworkState>(
                factoryPositional.datasource, { it.networkState })
        val initialState = Transformations.switchMap<BeerPositionalDataSource, NetworkState>(
                factoryPositional.datasource, { it.initialState })
        beersList = LivePagedListBuilder<Int, BeerResult>(factoryPositional, config).build()
        return GroupActionLoadBeer(beersList,networkState,initialState)
    }
}