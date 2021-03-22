package com.uren.listdemoapp.source

/**
 * Created by nurullaht on 3/22/21.
 */

interface FetchResultCallback {
    fun onFetchResult(result: PeoplePagingSource.FetchResult, message: String)
}