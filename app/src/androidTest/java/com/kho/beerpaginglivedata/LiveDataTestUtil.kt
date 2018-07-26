package com.kho.beerpaginglivedata

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class LiveDataTestUtil {
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayListOf<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(t: T?) {
                t?.let {
                    data[0] = t
                    latch.countDown()
                    liveData.removeObserver(this)
                }
            }

        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        return data[0] as T
    }
}