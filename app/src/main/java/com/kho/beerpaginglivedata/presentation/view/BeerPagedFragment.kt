package com.kho.beerpaginglivedata.presentation.view

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kho.beerpaginglivedata.R
import com.kho.beerpaginglivedata.data.model.BeerResult
import com.kho.beerpaginglivedata.data.model.PageListMode
import com.kho.beerpaginglivedata.data.remote.BeerListPagedRepository
import com.kho.beerpaginglivedata.data.remote.NetworkState
import com.kho.beerpaginglivedata.data.remote.ServiceProvider
import com.kho.beerpaginglivedata.data.remote.Status
import com.kho.beerpaginglivedata.presentation.viewmodel.BeerViewModelImpl
import kotlinx.android.synthetic.main.fragment_main.*

class BeerPagedFragment : Fragment() {
    private lateinit var viewModel: BeerViewModelImpl
    private lateinit var adapter: BeersListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initFetchData()
        initRefreshData()

    }


    private fun initAdapter() {
        adapter = BeersListAdapter()
        val layoutManager = LinearLayoutManager(activity)
        rcBeers.layoutManager = layoutManager
        rcBeers.adapter = adapter
    }

    private fun initFetchData() {
        val api = ServiceProvider.get().provideBeerService()
        val factoryViewModel = BeerViewModelImpl.Factory(PageListMode.PAGED, BeerListPagedRepository(api))
        viewModel = ViewModelProviders.of(activity!!, factoryViewModel).get(BeerViewModelImpl::class.java)

        viewModel.getBeerList().observe(this, Observer<PagedList<BeerResult>> {
            adapter.submitList(it)
        })

        viewModel.getStateNetwork().observe(this, Observer<NetworkState> {
            adapter.setNetworkState(it)
            if (it?.status == Status.FAILED) {
                showFaliledDialog(it.message ?: "")
            }
        })
    }

    private fun initRefreshData() {
        viewModel.getStateInitial().observe(this, Observer<NetworkState> {
            swBeers.isRefreshing = it?.status == NetworkState.LOADING.status
        })

        swBeers.setOnRefreshListener {
            viewModel.onRefresh()
        }
    }

    private fun showFaliledDialog(message: String) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                    viewModel.onRetry()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert = builder.create()
        alert.show()
    }
}