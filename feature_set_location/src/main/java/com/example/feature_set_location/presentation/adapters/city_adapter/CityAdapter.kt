package com.example.feature_set_location.presentation.adapters.city_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.feature_set_location.databinding.CityItemBinding
import com.example.feature_set_location.presentation.fragments.model.CityAdapterModel

class CityAdapter(private val onItemClick: (String, Int) -> Unit) :
    RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    var cities = listOf<CityAdapterModel>()
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
        holder.binding.selectCityRadioButton.isChecked = cities[position].isSelected
        holder.bind(cities[position])

        with(holder.binding) {
            root.setOnClickListener {
                userClickedOnItem(position)
            }
            selectCityRadioButton.setOnClickListener {
                userClickedOnItem(position)
            }
        }
    }

    inner class CityViewHolder(val binding: CityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: CityAdapterModel) {
            binding.cityName.text = city.cityName
        }
    }

    private fun userClickedOnItem(position: Int) {
        onItemClick.invoke(cities[position].cityName, cities[position].officeId)
        notifyDataSetChanged()
    }
}