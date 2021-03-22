package com.uren.listdemoapp.ui

import Person
import androidx.lifecycle.*
import androidx.paging.*
import com.uren.listdemoapp.source.FetchResultCallback
import com.uren.listdemoapp.source.PeoplePagingSource
import kotlinx.coroutines.flow.Flow

/**
 * Created by nurullaht on 3/20/21.
 */

class MainViewModel(var callback: FetchResultCallback? = null) : ViewModel() {

    private var _fetchResult = MutableLiveData<PeoplePagingSource.FetchResult>()
    var fetchResult: LiveData<PeoplePagingSource.FetchResult> = _fetchResult

    private var _errorText = MutableLiveData<String?>()
    val errorText: LiveData<String?> = _errorText

    private var _retryClick = MutableLiveData<Boolean>()
    val retryClick: LiveData<Boolean> = _retryClick

    fun getPeopleStream(): Flow<PagingData<Person>> {
        return Pager(PagingConfig(20)) {
            PeoplePagingSource(callback)
        }.flow.cachedIn(viewModelScope)
    }

    fun setUI(result: PeoplePagingSource.FetchResult, message: String) {
        _fetchResult.value = result
        _errorText.value = message
    }

    fun onRetryClick() {
        _retryClick.value = true
    }
}