package com.kho.beerpaginglivedata.unit_test.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import com.kho.beerpaginglivedata.features.beerlist.data.model.BeerResult
import com.kho.beerpaginglivedata.features.domain.BeerListPagedRepository
import com.kho.beerpaginglivedata.features.domain.GroupActionLoadBeer
import com.kho.beerpaginglivedata.base_domain.NetworkState
import com.kho.beerpaginglivedata.unit_test.BeerReultMook
import com.kho.beerpaginglivedata.unit_test.FakeBeerApi
import com.kho.beerpaginglivedata.unit_test.RxImmediateSchedulerRule
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner




@RunWith(MockitoJUnitRunner::class)
class BeerRepositoryTest {
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()
    val beerResulFactory = BeerReultMook()

    lateinit var fakeBeerApi : FakeBeerApi
    lateinit var repositoryTest : BeerListPagedRepository


    @Before
    fun initTest(){
        fakeBeerApi = FakeBeerApi()
        repositoryTest = BeerListPagedRepository(fakeBeerApi)
    }

    @Test
    fun load_initial_success_and_empty_data() {
        fakeBeerApi.addResponse(200, "")
        val resultBeer = repositoryTest.loadBeer()
        val pagedList = getPagingList(resultBeer)
        assertThat(pagedList.size, `is`(0))
    }

    @Test
    fun load_initial_error() {
        fakeBeerApi.addResponse(400, "error")
        val resultBeer = repositoryTest.loadBeer()
        val pagedList = getPagingList(resultBeer)
        val network = getStateNetWork(resultBeer)
        assertThat(pagedList.size, `is`(0))
        assertThat(network, `is`(NetworkState.error("error")))

    }

    @Test
    fun load_initial_and_after_success() {
        fakeBeerApi.addResponse(200, "")
        val posts = (0..10).map { beerResulFactory.createBeerReulst() }
        posts.forEach { fakeBeerApi.addListBeersResponse(it) }
        val resultBeer = repositoryTest.loadBeer()
        val pagedList = getPagingList(resultBeer)
        pagedList.loadAround(2)
        assertThat(pagedList.size, `is`(22))
        assertThat(getStateNetWork(resultBeer), `is`(NetworkState.LOADED))
    }

    @Test
    fun load_initial_success_and_after_error() {
        fakeBeerApi.addResponse(200, "")
        val posts = (0..10).map { beerResulFactory.createBeerReulst() }
        posts.forEach { fakeBeerApi.addListBeersResponse(it) }
        val resultBeer = repositoryTest.loadBeer()
        val pagedList = getPagingList(resultBeer)
        assertThat(getStateNetWork(resultBeer), `is`(NetworkState.LOADED))
        fakeBeerApi.addResponse(400, "error")
        pagedList.loadAround(2)
        assertThat(getStateNetWork(resultBeer), `is`(NetworkState.error("error")))
    }

    @Test
    fun load_refresh_success() {
        fakeBeerApi.addResponse(200, "")
        val posts = (0..10).map { beerResulFactory.createBeerReulst() }
        posts.forEach { fakeBeerApi.addListBeersResponse(it) }
        val resultBeer = repositoryTest.loadBeer()

        //trigger load
        assertThat(getPagingList(resultBeer), `is`(posts))
        assertThat(getStateNetWork(resultBeer), `is`(NetworkState.LOADED))

        val networkObserver = Mockito.mock(Observer::class.java) as Observer<NetworkState>
        resultBeer.networkState?.observeForever(networkObserver)
        repositoryTest.loadRefresh()
        val inOrder = Mockito.inOrder(networkObserver)

        //trigger load
        assertThat(getPagingList(resultBeer), `is`(posts))

        inOrder.verify(networkObserver).onChanged(NetworkState.LOADING)
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADED)
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun load_refresh_error() {
        fakeBeerApi.addResponse(200, "")
        val posts = (0..10).map { beerResulFactory.createBeerReulst() }
        posts.forEach { fakeBeerApi.addListBeersResponse(it) }
        val resultBeer = repositoryTest.loadBeer()

        //trigger load
        assertThat(getPagingList(resultBeer), `is`(posts))
        assertThat(getStateNetWork(resultBeer), `is`(NetworkState.LOADED))

        val networkObserver = Mockito.mock(Observer::class.java) as Observer<NetworkState>
        resultBeer.networkState?.observeForever(networkObserver)
        fakeBeerApi.addResponse(400, "error")
        repositoryTest.loadRefresh()
        val inOrder = Mockito.inOrder(networkObserver)
        //trigger load

        inOrder.verify(networkObserver).onChanged(NetworkState.LOADING)
        inOrder.verify(networkObserver).onChanged(NetworkState.error("error"))
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun load_retry_when_load_after_error(){
        fakeBeerApi.addResponse(200, "")
        val posts = (0..10).map { beerResulFactory.createBeerReulst() }
        posts.forEach { fakeBeerApi.addListBeersResponse(it) }
        val resultBeer = repositoryTest.loadBeer()
        val pagedList = getPagingList(resultBeer)
        assertThat(getStateNetWork(resultBeer), `is`(NetworkState.LOADED))
        fakeBeerApi.addResponse(400, "error")
        pagedList.loadAround(2)
        assertThat(getStateNetWork(resultBeer), `is`(NetworkState.error("error")))

        fakeBeerApi.addResponse(200, "")
        repositoryTest.loadRetry()
        assertThat(pagedList.size, `is`(22))
        assertThat(getStateNetWork(resultBeer), `is`(NetworkState.LOADED))

    }

    private fun getPagingList(result: GroupActionLoadBeer<BeerResult>): PagedList<BeerResult> {
        val observer = LogginObserver<PagedList<BeerResult>>()
        result.pagedList.observeForever(observer)
        MatcherAssert.assertThat(observer.value, `is`(notNullValue()))
        return observer.value!!

    }

    private fun getStateNetWork(result: GroupActionLoadBeer<BeerResult>): NetworkState {
        val observer = LogginObserver<NetworkState>()
        result.networkState?.observeForever(observer)
        MatcherAssert.assertThat(observer.value, `is`(notNullValue()))
        return observer.value!!
    }

    private class LogginObserver<T> : Observer<T> {
        var value: T? = null
        override fun onChanged(t: T?) {
            this.value = t
        }
    }
}