package com.kho.beerpaginglivedata.features.beerlist.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.paging.PagedList
import com.kho.beerpaginglivedata.base_domain.NetworkState
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult
import com.kho.beerpaginglivedata.features.beerlist.data.model.PageListMode
import com.kho.beerpaginglivedata.features.domain.BeerRepository

class BeerViewModelImpl(mode: PageListMode, val repository: BeerRepository) : BeerViewModel() {
    override fun getStateNetwork(): LiveData<NetworkState> = networkState

    override fun getBeerList(): LiveData<PagedList<BeerResult>> = beersList

    override fun getStateInitial(): LiveData<NetworkState> = initialState

    override fun onRefresh() {
        repository.loadRefresh()
    }

    override fun onRetry() {
        repository.loadRetry()
    }

    private lateinit var networkState: LiveData<NetworkState>
    private lateinit var initialState: LiveData<NetworkState>
    private lateinit var beersList: LiveData<PagedList<BeerResult>>

    init {
        repository.loadBeer().let {
            beersList = it.pagedList
            networkState = it.networkState!!
            initialState = it.initialState!!
        }

    }


    class Factory(val mode: PageListMode, val repository: BeerRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BeerViewModelImpl(mode, repository) as T

        }
    }
}