package pl.marchuck.pagingexample.data

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult

class BeerPagedListProvider(private val factory: DataSource.Factory<Int, BeerResult>) : PagedListProvider<BeerResult> {

    override fun provide(): LiveData<PagedList<BeerResult>> {
        val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(10 * 2)
                .setEnablePlaceholders(false)
                .build()
        return LivePagedListBuilder(factory, config).build()
    }
}