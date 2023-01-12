package com.example.testexomind.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testexomind.App
import com.example.testexomind.BuildConfig
import com.example.testexomind.R
import com.example.testexomind.databinding.WeatherItemBinding
import com.example.testexomind.model.WeatherResponse


class AdapterWeather(private val onClickWeatherItem: (WeatherResponse) -> Unit) : ListAdapter<WeatherResponse, AdapterWeather.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClickWeatherItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(val binding: WeatherItemBinding, private val onClickWeatherItem: (WeatherResponse) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: WeatherResponse) {
            binding.root.setOnClickListener {
                onClickWeatherItem(item)
            }
            Glide.with(App.appContext)
                .load(BuildConfig.IMAGE_URL+item.weather[0].icon+"@2x.png")
                .placeholder(R.drawable.placeholder)
                .into(binding.weatherImg)
            binding.weatherTemp.text = "${item.main.temp.toInt()} °C"
            binding.weatherCity.text = item.name
        }
    }
}


private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WeatherResponse>() {

    override fun areContentsTheSame(
        oldItem: WeatherResponse,
        newItem: WeatherResponse
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: WeatherResponse,
        newItem: WeatherResponse
    ): Boolean {
        return oldItem.id == newItem.id
    }
}