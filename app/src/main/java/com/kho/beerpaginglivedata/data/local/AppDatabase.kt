package com.kho.beerpaginglivedata.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.kho.beerpaginglivedata.data.model.BeerResult

/**
 * Created by Administrator on 7/21/18.
 */
@Database(entities = arrayOf(BeerResult::class),version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var INSTANST: AppDatabase? = null

        fun getInMemmoryDB(context: Context): AppDatabase {
            INSTANST?.let {
                return it
            }.run {
                val mdb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
                INSTANST = mdb
                return mdb
            }
        }
    }

    abstract fun beerDao():BeerDao
}