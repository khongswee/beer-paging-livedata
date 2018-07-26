package com.kho.beerpaginglivedata.unit_test

import com.kho.beerpaginglivedata.data.model.BeerModel
import com.kho.beerpaginglivedata.data.model.BeerResult
import com.kho.beerpaginglivedata.data.model.Pager
import com.kho.beerpaginglivedata.data.remote.BeerApi
import io.reactivex.Single
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

class FakeBeerApi : BeerApi {
    override fun getBeersListAfter(page: Int): Single<BeerModel> {
        if (responseStatusCode != 200) {
            return Single.error(IOException(responseMessage))
        }
        return Single.just(BeerModel(responseStatusCode, responseMessage!!, responsePage!!, responseBeer))
    }

    var responseBeer = arrayListOf<BeerResult>()
    private var responsePage: Pager? = null
    var responseStatusCode: Int = 200
    var responseMessage: String? = null

    override fun getBeersList(page: Int): Single<BeerModel> {
        if (responseStatusCode != 200) {
            return Single.error(IOException(responseMessage))
        }
        return Single.just(BeerModel(responseStatusCode, responseMessage!!, responsePage!!, responseBeer))
    }

    fun addResponse(code: Int, message: String?) {
        responseStatusCode = code
        responseMessage = message
        responsePage = createPage()
    }

    fun addListBeersResponse(beer: BeerResult) {
        responseBeer.add(beer)
    }

    private fun createPage(): Pager {
        if (responsePage == null) {
            responsePage = Pager(20
                    , 0, 0, true, false,
                    1, "", 2, "",
                    1, "", 20, "")
            return responsePage!!
        } else {
            responsePage.apply {
                this?.nextPage = responsePage?.currentPage!!.plus(1)
                this?.currentPage = responsePage?.nextPage!!.minus(1)
                this?.previousPage = responsePage?.currentPage!!.minus(1)
            }

            return responsePage!!
        }
    }

    fun createBeerResult(): BeerResult {
        val counter = AtomicInteger(0)
        val id = counter.incrementAndGet()
        return BeerResult(id,"Beer","IBA",2000,"","")
    }


}