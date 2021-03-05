package com.joecoding.weatheralert.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.joecoding.weatheralert.R
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.HourlyItem
import kotlinx.android.synthetic.main.list_item_main.view.*
import java.text.SimpleDateFormat
import java.util.*


class MainAdapter(private val items: List<HourlyItem?>?) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items?.get(position)
        val generator = ColorGenerator.MATERIAL

        // generate random color
        val color = generator.randomColor
        holder.cvListWeather.setCardBackgroundColor(color)


        val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val format = simpleDateFormat.format(data?.dt?.times(1000L))

        holder.tvTime.text =format
        holder.tvTemp.text = String.format(Locale.getDefault(), "%.0f°C", data?.temp)
        holder.tvHumidity.text = data?.humidity.toString()
        holder.tvPressure.text = data?.pressure.toString()
        holder.tvWindSpeed.text = data?.windSpeed.toString()

        when (data?.weather?.get(0)?.description) {
            "broken clouds" -> {
                holder.iconTemp.setAnimation(R.raw.broken_clouds)
            }
            "light rain" -> {
                holder.iconTemp.setAnimation(R.raw.light_rain)
            }
            "overcast clouds" -> {
                holder.iconTemp.setAnimation(R.raw.overcast_clouds)
            }
            "moderate rain" -> {
                holder.iconTemp.setAnimation(R.raw.moderate_rain)
            }
            "few clouds" -> {
                holder.iconTemp.setAnimation(R.raw.few_clouds)
            }
            "heavy intensity rain" -> {
                holder.iconTemp.setAnimation(R.raw.heavy_intentsity)
            }
            "clear sky" -> {
                holder.iconTemp.setAnimation(R.raw.clear_sky)
            }
            "scattered clouds" -> {
                holder.iconTemp.setAnimation(R.raw.scattered_clouds)
            }
            else -> {
                holder.iconTemp.setAnimation(R.raw.unknown)
            }
        }
    }

    override fun getItemCount(): Int {
        return items!!.size

        Log.d("itemSizeeeeeeeeeeeee", items!!.size.toString())
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cvListWeather: CardView = itemView.cvListWeather
        var tvTime: TextView = itemView.tvTime
        var tvTemp: TextView = itemView.tvTemp
        var tvHumidity: TextView = itemView.tvHumidity
        var tvPressure: TextView = itemView.tvPressure
        var tvWindSpeed: TextView = itemView.tvWindSpeed
        var iconTemp: LottieAnimationView = itemView.iconTemp

    }
}