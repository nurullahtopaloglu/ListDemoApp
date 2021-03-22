package com.uren.listdemoapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import androidx.paging.LoadState
import com.uren.listdemoapp.R
import com.uren.listdemoapp.databinding.ActivityMainBinding
import com.uren.listdemoapp.source.FetchResultCallback
import com.uren.listdemoapp.source.PeoplePagingSource
import com.uren.listdemoapp.ui.adapter.PeopleLoadStateAdapter
import com.uren.listdemoapp.ui.adapter.PeopleAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

open class BaseActivity : AppCompatActivity() {

    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes resId: Int): Lazy<T> = lazy {
        DataBindingUtil.setContentView<T>(this, resId)
    }

}

class MainActivity : BaseActivity(), FetchResultCallback {

    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: PeopleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = this.viewModel

        initView()
    }

    private fun initView() {
        viewModel.callback = this
        adapter = PeopleAdapter()
        adapter.hasStableIds()

        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PeopleLoadStateAdapter(adapter),
            footer = PeopleLoadStateAdapter(adapter)
        )

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.swiperefresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launch {
            viewModel.getPeopleStream().collectLatest {
                    adapter.submitData(it)
                }
        }

        viewModel.retryClick.observe(this, Observer {
            adapter.retry()
        })

        binding.swiperefresh.setOnRefreshListener {
            adapter.refresh()
            adapter.retry()
        }
    }

    override fun onFetchResult(result: PeoplePagingSource.FetchResult, message: String) {
        Log.e("NT>> result :", result.name)
        viewModel.setUI(result, message)
    }
}