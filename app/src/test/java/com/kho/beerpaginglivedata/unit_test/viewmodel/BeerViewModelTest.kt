package com.kho.beerpaginglivedata.unit_test.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import com.kho.beerpaginglivedata.data.model.BeerResult
import com.kho.beerpaginglivedata.data.model.PageListMode
import com.kho.beerpaginglivedata.data.remote.BeerRepository
import com.kho.beerpaginglivedata.data.remote.GroupActionLoadBeer
import com.kho.beerpaginglivedata.data.remote.NetworkState
import com.kho.beerpaginglivedata.presentation.viewmodel.BeerViewModel
import com.kho.beerpaginglivedata.presentation.viewmodel.BeerViewModelImpl
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BeerViewModelTest {
    @Rule
    @JvmField
    val instantTaskRule = InstantTaskExecutorRule()
    @Mock private lateinit var observerBeerList: Observer<PagedList<BeerResult>>
    @Mock private lateinit var observerNetwork: Observer<NetworkState>

    private var result: GroupActionLoadBeer<BeerResult>? = null
    private val beerList: MutableLiveData<PagedList<BeerResult>> = MutableLiveData()
    private val network: MutableLiveData<NetworkState> = MutableLiveData()
    private val initail: MutableLiveData<NetworkState> = MutableLiveData()


    lateinit var viewmodel: BeerViewModelImpl
    @Mock lateinit var repository: BeerRepository

    @Before
    fun initViewmodel() {
        setUpMook()
        viewmodel = BeerViewModelImpl(PageListMode.PAGED, repository)
    }

    @Test
    fun fectch_beer(){
        viewmodel.getBeerList().observeForever(observerBeerList)
        Mockito.verify(repository).loadBeer()
        Mockito.verifyNoMoreInteractions(repository)
    }

    @Test
    fun fectch_beer_refresh() {
        viewmodel.getBeerList().observeForever(observerBeerList)
        then(observerBeerList).shouldHaveNoMoreInteractions()
        viewmodel.onRefresh()
        Mockito.verify(repository).loadBeer()
        Mockito.verify(repository).loadRefresh()
        Mockito.verifyNoMoreInteractions(repository)
    }

    @Test
    fun fectch_beer_retry() {
    }

    fun setUpMook() {
        result = GroupActionLoadBeer(beerList, network, initail)
        given(repository.loadBeer()).willReturn(result)
    }
}