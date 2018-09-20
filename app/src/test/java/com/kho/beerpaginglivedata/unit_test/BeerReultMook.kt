package com.kho.beerpaginglivedata.unit_test

import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult
import java.util.concurrent.atomic.AtomicInteger

class BeerReultMook {
    private val counter = AtomicInteger(0)
    fun createBeerReulst() : BeerResult {
        val id = counter.incrementAndGet()
        val post = BeerResult(id,"Beer","IBA",2000,"","")
        return post
    }
}