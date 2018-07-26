package com.kho.beerpaginglivedata.presentation.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kho.beerpaginglivedata.R
import com.kho.beerpaginglivedata.data.local.ServiceLocal
import com.kho.beerpaginglivedata.data.model.BeerResult
import com.kho.beerpaginglivedata.data.model.PageListMode
import com.kho.beerpaginglivedata.data.remote.BeerListPositionalRepository
import com.kho.beerpaginglivedata.data.remote.NetworkState
import com.kho.beerpaginglivedata.data.remote.Status
import com.kho.beerpaginglivedata.presentation.viewmodel.BeerViewModelImpl
import kotlinx.android.synthetic.main.fragment_main.*

class BeerPositionalFragment : Fragment() {
    private lateinit var viewModel: BeerViewModelImpl


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_non_refresh, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity!!)
        val adapter = BeersListAdapter()
        val serviceLocal = context?.let { ServiceLocal(it) }
        serviceLocal?.let {
            val factoryViewModel = BeerViewModelImpl.Factory(PageListMode.POSITIONAL, BeerListPositionalRepository(serviceLocal))
            viewModel = ViewModelProviders.of(activity!!, factoryViewModel).get(BeerViewModelImpl::class.java)
        }

        rcBeers.layoutManager = layoutManager
        rcBeers.adapter = adapter

        viewModel.getBeerList().observe(this, Observer<PagedList<BeerResult>> {
            adapter.submitList(it)
        })

        viewModel.getStateNetwork().observe(this, Observer<NetworkState> {
            adapter.setNetworkState(it)
        })

    }

}