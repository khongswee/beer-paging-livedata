package com.kho.beerpaginglivedata.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult
import io.reactivex.Single

/**
 * Created by Administrator on 7/21/18.
 */
@Dao
interface BeerDao  {

    @Query("SELECT * FROM BeerResult LIMIT :limit OFFSET :offset")
    fun loadBeersPosition(offset:Int, limit:Int): Single<List<BeerResult>>

    @Query("SELECT * FROM BeerResult where id>= :id LIMIT :limit")
    fun loadBeersWithId(id:Int, limit:Int): Single<List<BeerResult>>

    @Query("SELECT * FROM BeerResult LIMIT :limit")
    fun loadBeersWithIdInit(limit:Int): Single<List<BeerResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBeerItem(beer:BeerResult)
}

