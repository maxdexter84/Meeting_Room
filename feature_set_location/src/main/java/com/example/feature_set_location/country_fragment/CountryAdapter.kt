package com.example.feature_set_location.country_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.feature_set_location.databinding.CountryItemBinding

class CountryAdapter: RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    var onItemClick: () -> Unit = {}

    var countries = emptyList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = countries.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = CountryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])

        holder.itemView.setOnClickListener { onItemClick() }
    }

    class CountryViewHolder(private val binding: CountryItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(country: String) {
            binding.countryName.text = country
        }
    }
}