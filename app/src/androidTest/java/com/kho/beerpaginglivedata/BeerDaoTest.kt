package com.kho.beerpaginglivedata

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.kho.beerpaginglivedata.data.local.AppDatabase
import com.kho.beerpaginglivedata.data.local.BeerDao
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BeerDaoTest {
    private lateinit var beerDao: BeerDao
    private lateinit var mdb: AppDatabase
    private val LIMIT = 10
    private val FAKELIST = 30

    @Before
    fun createDB() {
        val context = InstrumentationRegistry.getTargetContext()
        mdb = AppDatabase.getInMemmoryDB(context)
        beerDao = mdb.beerDao()
    }

    @Test
    fun writeAndReadBeerByPosition() {
        // insert fake data beer
        insetDataBeerList()

        val OFFSET = 0
        beerDao.loadBeersPosition(OFFSET, LIMIT)
                .test()
                .assertValue {
                    it.forEach {
                        if (!(it.id >= OFFSET && it.id <= (OFFSET + LIMIT))) {
                            return@assertValue false
                        }
                    }
                    return@assertValue true
                }
    }

    @Test
    fun writeAndReadBeerListFirstById() {
        // insert fake data beer
        insetDataBeerList()

        beerDao.loadBeersWithIdInit(LIMIT)
                .test().assertValue {
                    return@assertValue it.size == LIMIT
                }
    }

    @Test
    fun writeAndReadBeerListById() {
        // insert fake data beer
        insetDataBeerList()

        val idRequest = 15
        beerDao.loadBeersWithId(idRequest, LIMIT)
                .test()
                .assertValue {
                    it.forEach {
                        if (!(it.id >= idRequest && it.id <= (idRequest + LIMIT))) {
                            return@assertValue false
                        }
                    }
                    return@assertValue true
                }
    }

    private fun createFakeListBeers(size: Int): List<BeerResult> {
        val listBeersFake = arrayListOf<BeerResult>()
        for (i in 0..size) {
            val data = BeerResult(i.plus(1), "Test", "A", 1000, "", "")
            listBeersFake.add(data)
        }

        return listBeersFake
    }

    private fun insetDataBeerList() {
        val fakeBeer = createFakeListBeers(FAKELIST)
        fakeBeer.forEach {
            beerDao.insertBeerItem(it)
        }
    }
}


