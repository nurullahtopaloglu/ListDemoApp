/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uren.listdemoapp.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.recyclerview.widget.RecyclerView
import com.uren.listdemoapp.R
import com.uren.listdemoapp.databinding.NetworkStateItemBinding

/**
 * Created by nurullaht on 3/22/21.
 */

class DbStateItemViewHolder(parent: ViewGroup, private val retryCallback: () -> Unit) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
    .inflate(R.layout.network_state_item, parent, false)) {

    private val binding = NetworkStateItemBinding.bind(itemView)
    private val progressBar = binding.progressBar
    private val errorMsg = binding.errorMsg
    private val retry = binding.retryButton.also {
            it.setOnClickListener { retryCallback() }
        }

    fun bindTo(loadState: LoadState) {
        progressBar.isVisible = loadState is Loading
        retry.isVisible = loadState is Error
        errorMsg.isVisible = !(loadState as? Error)?.error?.message.isNullOrBlank()
        errorMsg.text = (loadState as? Error)?.error?.message
    }
}
