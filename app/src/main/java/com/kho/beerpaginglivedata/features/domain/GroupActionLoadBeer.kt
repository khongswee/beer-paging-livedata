package com.kho.beerpaginglivedata.features.domain

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.kho.beerpaginglivedata.base_domain.NetworkState

data class GroupActionLoadBeer<T>(
        val pagedList: LiveData<PagedList<T>>,
        val networkState: LiveData<NetworkState>?=null,
        val initialState: LiveData<NetworkState>?=null
)