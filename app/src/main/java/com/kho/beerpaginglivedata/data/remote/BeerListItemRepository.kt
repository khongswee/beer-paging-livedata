package com.kho.beerpaginglivedata.data.remote

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.kho.beerpaginglivedata.base_domain.NetworkState
import com.kho.beerpaginglivedata.features.beerlist.data.datasource.itemkey.BeerItemKeyDataDataFactory
import com.kho.beerpaginglivedata.features.beerlist.data.datasource.itemkey.BeerItemKeyDataSource
import com.kho.beerpaginglivedata.data.local.ServiceLocal
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult
import com.kho.beerpaginglivedata.features.domain.BeerRepository
import com.kho.beerpaginglivedata.features.domain.GroupActionLoadBeer

class BeerListItemRepository( service: ServiceLocal) : BeerRepository {
    val factoryItemKey = BeerItemKeyDataDataFactory(service)
    override fun loadRetry() {
    }

    override fun loadRefresh() {
        factoryItemKey.datasource.value?.invalidate()
    }

    private val pageSize = 10

    override fun loadBeer(): GroupActionLoadBeer<BeerResult> {
        val beersList: LiveData<PagedList<BeerResult>>
        val initialState = Transformations.switchMap<BeerItemKeyDataSource, NetworkState>(
                factoryItemKey.datasource,{it.initialState})
        val networkState = Transformations.switchMap<BeerItemKeyDataSource, NetworkState>(
                factoryItemKey.datasource, { it.networkState })
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build()
        beersList = LivePagedListBuilder<Int, BeerResult>(factoryItemKey, config).build()
        return GroupActionLoadBeer(beersList, networkState, initialState)
    }
}