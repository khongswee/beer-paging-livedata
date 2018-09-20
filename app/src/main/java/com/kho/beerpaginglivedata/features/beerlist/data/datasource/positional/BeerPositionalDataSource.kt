package com.kho.beerpaginglivedata.features.beerlist.data.datasource.positional

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PositionalDataSource
import com.kho.beerpaginglivedata.data.local.ServiceLocal
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerModel
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult
import com.kho.beerpaginglivedata.base_domain.NetworkState
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class BeerPositionalDataSource(val serviceLocal: ServiceLocal) : PositionalDataSource<BeerResult>() {
    val networkState = MutableLiveData<NetworkState>()
    val initialState = MutableLiveData<NetworkState>()
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<BeerResult>) {
        networkState.postValue(NetworkState.LOADING)
        Observable.timer(3000,TimeUnit.MILLISECONDS).subscribe {
            loadData(params.startPosition, params.loadSize)
                    .subscribe { t1, t2 ->
                        networkState.postValue(NetworkState.LOADED)
                        callback.onResult(t1.result) }
        }

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<BeerResult>) {
        networkState.postValue(NetworkState.LOADING)
        initialState.postValue(NetworkState.LOADING)
        Observable.timer(2000, TimeUnit.MILLISECONDS).subscribe {
            loadData(0, params.pageSize)
                    .subscribe { t1, t2 ->
                        networkState.postValue(NetworkState.LOADED)
                        initialState.postValue(NetworkState.LOADED)
                        callback.onResult(t1.result, params.requestedStartPosition) }
        }

    }


    private fun loadData(offset: Int, limit: Int): Single<BeerModel> {
        return serviceLocal.getBeerList(offset, limit)
    }

}