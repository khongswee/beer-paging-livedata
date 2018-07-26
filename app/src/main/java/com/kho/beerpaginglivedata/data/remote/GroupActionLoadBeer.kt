package com.kho.beerpaginglivedata.data.remote

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

data class GroupActionLoadBeer<T>(
        val pagedList: LiveData<PagedList<T>>,
        val networkState: LiveData<NetworkState>?=null,
        val initialState: LiveData<NetworkState>?=null
)