package com.kho.beerpaginglivedata.presentation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.kho.beerpaginglivedata.data.model.BeerResult
import com.kho.beerpaginglivedata.data.remote.NetworkState

/**
 * Created by Administrator on 7/7/18.
 */
abstract class BeerViewModel: ViewModel (){
    abstract fun getBeerList(): LiveData<PagedList<BeerResult>>
    abstract fun getStateNetwork():LiveData<NetworkState>
    abstract fun getStateInitial(): LiveData<NetworkState>
    abstract fun onRefresh()
    abstract fun onRetry()
}