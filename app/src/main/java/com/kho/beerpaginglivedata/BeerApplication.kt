package com.kho.beerpaginglivedata

import android.app.Application
import com.kho.beerpaginglivedata.data.local.ServiceLocal

/**
 * Created by Administrator on 7/21/18.
 */
class BeerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val serviceLocal  = ServiceLocal(applicationContext)
        serviceLocal.fakeData()
    }
}