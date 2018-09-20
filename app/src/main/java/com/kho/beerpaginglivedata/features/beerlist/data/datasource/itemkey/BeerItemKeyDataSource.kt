package com.kho.beerpaginglivedata.features.beerlist.data.datasource.itemkey

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.ItemKeyedDataSource
import com.kho.beerpaginglivedata.data.local.ServiceLocal
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerModel
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult
import com.kho.beerpaginglivedata.base_domain.NetworkState
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class BeerItemKeyDataSource(val serviceLocal: ServiceLocal) : ItemKeyedDataSource<Int, BeerResult>() {

    val networkState = MutableLiveData<NetworkState>()
    val initialState = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<BeerResult>) {
        networkState.postValue(NetworkState.LOADING)
        initialState.postValue(NetworkState.LOADING)
        Observable.timer(3000, TimeUnit.MILLISECONDS).subscribe {
            loadDataInit(params.requestedLoadSize)
                    .subscribe({ it ->
                        networkState.postValue(NetworkState.LOADED)
                        initialState.postValue(NetworkState.LOADED)
                        callback.onResult(it.result)
                    }, { it ->
                        networkState.postValue(NetworkState.error(it.localizedMessage))
                        initialState.postValue(NetworkState.error(it.localizedMessage))
                    })
        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<BeerResult>) {
        networkState.postValue(NetworkState.LOADING)
        Observable.timer(2000, TimeUnit.MILLISECONDS).subscribe {
            loadData(params.key, params.requestedLoadSize).subscribe({ it ->
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(it.result)
            }, { it ->
                networkState.postValue(NetworkState.error(it.localizedMessage))
            })
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<BeerResult>) {
    }

    override fun getKey(item: BeerResult): Int = item.id

    private fun loadData(sinceId: Int, perPage: Int): Single<BeerModel> {
        return serviceLocal.getBeerListBySinceId(sinceId, perPage)
    }

    private fun loadDataInit(perPage: Int): Single<BeerModel> {
        return serviceLocal.getBeerListBySinceIdInit(perPage)
    }

}