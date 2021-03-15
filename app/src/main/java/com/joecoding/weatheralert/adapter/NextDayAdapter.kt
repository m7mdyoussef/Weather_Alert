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
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.joecoding.weatheralert.R
import com.joecoding.weatheralert.model.currentWeatherModel.db.remoteSourceDB.response.DailyItem
import com.joecoding.weatheralert.network.ApiUnits
import com.joecoding.weatheralert.providers.SharedPreferencesProvider
import kotlinx.android.synthetic.main.list_item_next_days.view.*
import java.text.SimpleDateFormat
import java.util.*


class NextDayAdapter(private val mContext: Context, private val items: List<DailyItem?>?) : RecyclerView.Adapter<NextDayAdapter.ViewHolder>() {

    lateinit var sharedPref: SharedPreferencesProvider

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_next_days, parent, false)
        return ViewHolder(view)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items?.get(position)

        sharedPref=SharedPreferencesProvider(mContext)

        val generator = ColorGenerator.MATERIAL

        // generate random color
        val color = generator.randomColor
        holder.cvListNextDays.setCardBackgroundColor(color)


        val simpleDateFormat = SimpleDateFormat("EEE, MMM dd. yyyy.", Locale(sharedPref.getLanguage.toString()))
        val format = simpleDateFormat.format(data?.dt?.times(1000L))

        holder.dailyDatetv.text = format
        holder.dailytvtemp.text = String.format(Locale.getDefault(), "%.0f°${ApiUnits.tempUnit}", data?.temp?.day)
        holder.dailyHumidity.text =data?.humidity.toString()+" %"
        holder.dailyPressure.text =data?.pressure.toString()+mContext.getString(R.string.hpa)
        holder.dailyWindSpeed.text =data?.windSpeed.toString()+ ApiUnits.WindSpeedUnit
        holder.dailyCloud.text =data?.clouds.toString()+" %"
        val description = data?.weather?.get(0)?.description
        holder.dailyDesc.text =description


        when (data?.weather?.get(0)?.icon) {
            "04d" -> {
                holder.iconTemp.setAnimation(R.raw.broken_clouds)
            }
            "04n" -> {
                holder.iconTemp.setAnimation(R.raw.broken_clouds)
            }
            "10d" -> {
                holder.iconTemp.setAnimation(R.raw.light_rain)
            }
            "10n" -> {
                holder.iconTemp.setAnimation(R.raw.light_rain)
            }
            "09d" -> {
                holder.iconTemp.setAnimation(R.raw.heavy_intentsity)
            }
            "09n" -> {
                holder.iconTemp.setAnimation(R.raw.heavy_intentsity)
            }
            "03d" -> {
                holder.iconTemp.setAnimation(R.raw.overcast_clouds)
            }
            "03n" -> {
                holder.iconTemp.setAnimation(R.raw.overcast_clouds)
            }

            "02d" -> {
                holder.iconTemp.setAnimation(R.raw.few_clouds)
            }
            "02n" -> {
                holder.iconTemp.setAnimation(R.raw.few_clouds)
            }

            "01d" -> {
                holder.iconTemp.setAnimation(R.raw.clear_sky)
            }
            "01n" -> {
                holder.iconTemp.setAnimation(R.raw.clear_sky)
            }
            "11d" -> {
                holder.iconTemp.setAnimation(R.raw.thunderstorm)
            }
            "11n" -> {
                holder.iconTemp.setAnimation(R.raw.thunderstorm)
            }
            "13d" -> {
                holder.iconTemp.setAnimation(R.raw.snow)
            }
            "13n" -> {
                holder.iconTemp.setAnimation(R.raw.snow)
            }
            "50d" -> {
                holder.iconTemp.setAnimation(R.raw.mist)
            }
            "50n" -> {
                holder.iconTemp.setAnimation(R.raw.mist)
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