package com.uren.listdemoapp.ui.adapter

import Person
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.uren.listdemoapp.databinding.ItemPersonBinding


class PeopleAdapter : PagingDataAdapter<Person, PeopleAdapter.PersonViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Person>() {
            // ID is fixed.
            override fun areItemsTheSame(oldPerson: Person, newPerson: Person) = oldPerson.id == newPerson.id

            override fun areContentsTheSame(oldPerson: Person, newPerson: Person) : Boolean {
                Log.e("areContentsTheSame", "true")
                return oldPerson == newPerson
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPersonBinding.inflate(inflater, parent, false)
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    class PersonViewHolder(private val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(person: Person, position: Int) {
            binding.labelNumber.text = (position + 1).toString() + ". "
            binding.labelName.text = person.fullName + " (" + person.id + ")"
            binding.executePendingBindings()
        }
    }
}