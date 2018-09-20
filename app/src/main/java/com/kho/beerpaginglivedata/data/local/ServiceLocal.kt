package com.kho.beerpaginglivedata.data.local

import android.content.Context
import com.google.gson.Gson
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerModel
import com.kho.beerpaginglivedata.features.beerlist.data.model.Pager
import io.reactivex.Single
import java.io.IOException

class ServiceLocal(private val context: Context) : BeerLocal {
    override fun getBeerListBySinceIdInit(limit: Int): Single<BeerModel> {
        return getBeerDao().loadBeersWithIdInit(limit).map {
            BeerModel(200, "", Pager(), it)
        }

    }

    private fun getBeerDao(): BeerDao {
        return AppDatabase.getInMemmoryDB(context).beerDao()
    }

    override fun getBeerListBySinceId(since_id: Int, limit: Int): Single<BeerModel> {
        return getBeerDao().loadBeersWithId(since_id, limit).map {
            BeerModel(200, "", Pager(), it)
        }
    }

    override fun getBeerList(offset: Int, limit: Int): Single<BeerModel> {
        return getBeerDao().loadBeersPosition(offset, limit).map {
            BeerModel(200, "", Pager(), it)
        }
    }

    private fun loadJSONFromAsset(path: String): String? {
        var json: String
        try {
            val inputStream = context.assets.open("$path.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    fun fakeData() {
        val list = Gson()
                .fromJson(loadJSONFromAsset("beer"), BeerModel::class.java).result
        val mdb = getBeerDao()
        list.forEach {
            mdb.insertBeerItem(it)
        }
    }

}