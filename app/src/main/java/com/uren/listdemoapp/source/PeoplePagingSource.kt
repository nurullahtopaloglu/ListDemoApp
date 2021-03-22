package com.uren.listdemoapp.source

import DataSource
import FetchError
import FetchResponse
import Person
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by nurullaht on 3/21/21.
 */

class PeoplePagingSource(var callback: FetchResultCallback?) : PagingSource<String, Person>() {

    enum class FetchResult {
        INITIAL_ERROR,
        LATER_ERROR,
        NO_USER,
        SUCCESSFUL
    }

    companion object {
        private var fetchedPeople = ArrayList<Person>()
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Person> {
        try {
            val responsePair = getData(params.key)
            var fetchResponse = responsePair.first
            val errorResponse = responsePair.second
            val result = getFetchResult(params.key, fetchResponse, errorResponse)
            var message = ""


            @Suppress("NON_EXHAUSTIVE_WHEN")
            when (result) {
                FetchResult.INITIAL_ERROR -> {
                    message = responsePair.second?.errorDescription!!
                    callback?.onFetchResult(result, message)
                }
                FetchResult.LATER_ERROR -> {
                    // callback?.onFetchResult(result, message)
                    return LoadResult.Error(Exception(responsePair.second?.errorDescription))
                }
                FetchResult.NO_USER -> {
                    message = "There is no one for Listing"
                    callback?.onFetchResult(result, message)
                    return LoadResult.Error(Exception(message))
                }
                FetchResult.SUCCESSFUL -> {
                    val newList = checkConcurrent(fetchResponse)
                    fetchedPeople.addAll(fetchResponse?.people!!)
                    if(!newList.isNullOrEmpty()) fetchResponse = FetchResponse(newList, fetchResponse.next)

                    callback?.onFetchResult(result, message)
                }
            }

            return LoadResult.Page(
                data = fetchResponse?.people!!,
                prevKey = null,
                nextKey = fetchResponse.next)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Person>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    private suspend fun getData(key: String?): Pair<FetchResponse?, FetchError?>  {
        return suspendCoroutine { continuation ->
            DataSource().fetch(
                next = key,
                completionHandler = { resp, error ->
                    continuation.resume(Pair(resp, error))
                })
        }
    }

    private fun getFetchResult(key: String?, response: FetchResponse?, error: FetchError?): FetchResult {
        if(key == null && error != null)  return FetchResult.INITIAL_ERROR
        if (error != null) return FetchResult.LATER_ERROR
        if (response != null && response.people.isEmpty()) return FetchResult.NO_USER
        if (response != null && response.people.isNotEmpty()) return FetchResult.SUCCESSFUL

        return FetchResult.LATER_ERROR
    }

    /**
     * It can be traversed all data items to eliminate duplicates but it is bad performance issue.
     * Error must be handled in server side
     * Just for test, only first item of fetched compared.
     */
    private fun checkConcurrent(fetchResponse: FetchResponse?): List<Person>? {
        if (fetchedPeople.isNotEmpty() && fetchResponse != null && fetchResponse.people.isNotEmpty()) {
            if (fetchedPeople.last().id == fetchResponse.people.first().id) {
                Log.e("NT>> concurrent error", "id : " + fetchResponse.people.first().id.toString())
                return fetchResponse.people.subList(1, fetchResponse.people.lastIndex)
            }
        }

        return null
    }
}