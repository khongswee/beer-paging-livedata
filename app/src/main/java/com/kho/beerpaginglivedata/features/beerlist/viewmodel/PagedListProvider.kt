package pl.marchuck.pagingexample.data

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

interface PagedListProvider<Value> {

    fun provide() : LiveData<PagedList<Value>>
}