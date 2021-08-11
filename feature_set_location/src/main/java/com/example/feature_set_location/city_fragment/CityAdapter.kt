package com.example.feature_set_location.city_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core_network.location_responses.City
import com.example.feature_set_location.databinding.CityItemBinding

class CityAdapter : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    private var selectedPosition = -1
    var selectedCity = "default"
    var onItemClick: () -> Unit = {}

    var cities = emptyList<City>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = cities.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding = CityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.binding.selectCityRadioButton.isChecked = position == selectedPosition
        holder.bind(cities[position])
    }

    inner class CityViewHolder(val binding: CityItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val clickHandler: (View) -> Unit = {
            selectedPosition = adapterPosition
            notifyDataSetChanged()

            selectedCity = binding.cityName.text.toString()
//          onItemClick(binding.cityName.text.toString())
        }

        init {
            binding.selectCityRadioButton.setOnClickListener(clickHandler)
        }

        fun bind(city: City) {
            binding.cityName.text = city.name
        }
    }
}