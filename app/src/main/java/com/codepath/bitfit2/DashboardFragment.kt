package com.codepath.bitfit2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class DashboardFragment : Fragment() {
    private var hours: List<Pair<Float, Float>> = mutableListOf()
    private var feelings: List<Pair<Float, Int>> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val chart = view.findViewById(R.id.chart) as LineChart
        val hoursTV = view.findViewById(R.id.average_hours_tv) as TextView
        val feelsTV = view.findViewById(R.id.average_feelings_tv) as TextView

        chart.description.text = "Log entries"
        chart.description.typeface = resources.getFont(R.font.poppins)
        chart.description.textSize = 10.0f
        chart.setNoDataText("No data entered yet.")
        chart.setBackgroundColor(resources.getColor(R.color.thistle))
        val xAxis = chart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.granularity = 1.0f
        var MAX_X: Long? = null
        var MIN_X: Long? = null


        val rightYAxis = chart.axisRight
        rightYAxis.setEnabled(false)

        var formatter = DateTimeFormatter.ofPattern("M/d/yyyy")
        formatter = formatter.withLocale(Locale.US)

        lifecycleScope.launch(IO) {
            val feelingAverage =
                (requireActivity().application as SleepEntryApplication).db.sleepEntryDao()
                    .getFeelingAverage()
            val sleepAverage =
                (requireActivity().application as SleepEntryApplication).db.sleepEntryDao()
                    .getHoursAverage()
            Log.d("dashbord", "$feelingAverage,$sleepAverage")

            withContext(Dispatchers.Main) {
                hoursTV.text =
                    resources.getString(R.string.averageHoursText) + " " + sleepAverage.toString()
                feelsTV.text =
                    resources.getString(R.string.averageFeelingText) + " " + feelingAverage.toString()
            }

        }

        lifecycleScope.launch(IO) {
            (requireActivity().application as SleepEntryApplication).db.sleepEntryDao()
                .getAllSleepEntries().collect { databaseList ->
                    databaseList.map { entity ->
                        SleepEntryEntity(
                            entity.id,
                            entity.sleptHours,
                            entity.feelingRating,
                            entity.sleepNotes,
                            entity.sleepDate
                        )
                    }.also { mappedList ->
                        hours = mappedList.map { pair ->
                            val millis = LocalDate.parse(pair.sleepDate, formatter)
                                .toEpochDay() * 24 * 60 * 60 * 1000
                            if (MAX_X == null || MAX_X!! < millis) MAX_X = millis
                            if (MIN_X == null || MIN_X!! > millis) MIN_X = millis

                            Pair(
                                millis.toFloat(),
                                pair.sleptHours
                            )


                        }

                        feelings = mappedList.map { pair ->
                            val millis = LocalDate.parse(pair.sleepDate, formatter)
                                .toEpochDay() * 24 * 60 * 60 * 1000
                            Pair(
                                millis.toFloat(),
                                pair.feelingRating
                            )
                        }
                    }
                }
            xAxis.axisMinimum = (MIN_X!!.toFloat())
            xAxis.axisMaximum = (MAX_X!!.toFloat())

        }
        val hourEntries =
            LineDataSet(hours.map { hour -> Entry(hour.first, hour.second) }, "Slept hours")
        hourEntries.setColors(resources.getColor(R.color.space_cadet))

        val feelEntries =
            LineDataSet(
                feelings.map { feel -> Entry(feel.first, feel.second.toFloat()) },
                "Feeling"
            )
        feelEntries.setColors(resources.getColor(R.color.tekhelet))

        val dateFormat: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {

                val simpleDateFormat = SimpleDateFormat("M/dd/yyyy", Locale.US)
                Log.d("format", simpleDateFormat.format(value))
                return simpleDateFormat.format(value).toString()
            }
        }


        val sets: ArrayList<ILineDataSet> = ArrayList()
        sets.add(hourEntries)
        sets.add(feelEntries)


        xAxis.valueFormatter = dateFormat
        xAxis.granularity = 1f
        xAxis.isEnabled = false
        val lineData = LineData(sets)
        chart.data = lineData
        chart.invalidate()
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }
}