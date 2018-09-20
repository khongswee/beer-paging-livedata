package com.kho.beerpaginglivedata.features.beerlist.data.datasource.pagekey

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import android.util.Log
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerModel
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult
import com.kho.beerpaginglivedata.features.beerlist.data.remote_service.BeerApi
import com.kho.beerpaginglivedata.base_domain.NetworkState
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action

class BeerPageKeyDatasource(val api: BeerApi, val scheduler: Scheduler) : PageKeyedDataSource<Int, BeerResult>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, BeerResult>) {

        print("loadInitial")
        networkState.postValue(NetworkState.LOADING)
        initialState.postValue(NetworkState.LOADING)
        load(1)
                .subscribe({
                    setRetry(null)
                    networkState.postValue(NetworkState.LOADED)
                    initialState.postValue(NetworkState.LOADED)
                    callback.onResult(it.result, it.pager.previousPage, it.pager.nextPage)
                }, {
                    setRetry(Action { loadInitial(params, callback) })
                    val error = NetworkState.error(it.message)
                    networkState.postValue(error)
                    initialState.postValue(error)
                })

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, BeerResult>) {

        networkState.postValue(NetworkState.LOADING)
        loadAfter(params.key)
                .subscribe({
                    setRetry(null)
                    networkState.postValue(NetworkState.LOADED)
                    if (params.key < it.pager.totalPages) {
                        callback.onResult(it.result, it.pager.nextPage)
                    }

                }, {
                    setRetry(Action { loadAfter(params, callback) })
                    networkState.postValue(NetworkState.error(it.localizedMessage))
                })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, BeerResult>) {
    }

    fun loadRetry() {
        retryCompletable
                ?.subscribeOn(scheduler)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({}, { Log.e("loadRetry", it.localizedMessage) })
    }

    val networkState = MutableLiveData<NetworkState>()
    val initialState = MutableLiveData<NetworkState>()
    private var retryCompletable: Completable? = null

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }

    private fun load(id: Int): Single<BeerModel> {
        return api.getBeersList(id)

    }


    private fun loadAfter(id: Int): Single<BeerModel> {
        return api.getBeersListAfter(id)

    }

}