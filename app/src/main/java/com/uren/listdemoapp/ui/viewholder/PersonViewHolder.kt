package com.uren.listdemoapp.ui.viewholder

import Person
import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.uren.listdemoapp.databinding.ItemPersonBinding

/**
 * Created by nurullaht on 3/22/21.
 */

class PersonViewHolder(private val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(person: Person, position: Int) {
        binding.labelNumber.text = (position + 1).toString() + ". "
        binding.labelName.text = person.fullName + " (" + person.id + ")"
        binding.executePendingBindings()
    }
}