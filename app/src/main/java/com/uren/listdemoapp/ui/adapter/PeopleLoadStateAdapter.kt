package com.uren.listdemoapp.ui.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.uren.listdemoapp.ui.viewholder.DbStateItemViewHolder

class PeopleLoadStateAdapter(
        private val adapter: PeopleAdapter
) : LoadStateAdapter<DbStateItemViewHolder>() {
    override fun onBindViewHolder(holder: DbStateItemViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            loadState: LoadState
    ): DbStateItemViewHolder {
        return DbStateItemViewHolder(parent) { adapter.retry() }
    }
}