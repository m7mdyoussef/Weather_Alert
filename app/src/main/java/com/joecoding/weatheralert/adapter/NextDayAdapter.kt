package com.joecoding.weatheralert.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.joecoding.weatheralert.R
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.DailyItem
import kotlinx.android.synthetic.main.list_item_next_days.view.*
import java.text.SimpleDateFormat
import java.util.*


class NextDayAdapter(private val mContext: Context,
                     private val items: List<DailyItem?>?) : RecyclerView.Adapter<NextDayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_next_days, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items?.get(position)

        val simpleDateFormat = SimpleDateFormat("EEE, MMM dd. yyyy.", Locale.ENGLISH)
        val format = simpleDateFormat.format(data?.dt?.times(1000L))

        holder.dailyDatetv.text = format
        holder.dailytvtemp.text = String.format(Locale.getDefault(), "%.0f°C", data?.temp?.day)
        holder.dailyHumidity.text =data?.humidity.toString()+" %"
        holder.dailyPressure.text =data?.pressure.toString()+" hpa"
        holder.dailyWindSpeed.text =data?.windSpeed.toString()+" m/s"
        holder.dailyCloud.text =data?.clouds.toString()+" %"
        val description = data?.weather?.get(0)?.description
        holder.dailyDesc.text =description


        when (description) {
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
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cvListNextDays: CardView = itemView.cvDailyItems
        var dailyDatetv: TextView = itemView.dailyDatetv
        var dailytvtemp: TextView = itemView.dailytvtemp
        var dailyHumidity: TextView = itemView.dailyHumidity
        var dailyPressure: TextView = itemView.dailyPressure
        var iconTemp: LottieAnimationView = itemView.iconTemp
        var dailyWindSpeed: TextView = itemView.dailyWindSpeed
        var dailyCloud: TextView = itemView.dailyCloud
        var dailyDesc: TextView = itemView.dailyDesc


    }
}