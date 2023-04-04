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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class DashboardFragment : Fragment() {
    private var hours: List<Pair<Float, Float>> = mutableListOf()
    private var feelings: List<Pair<Float, Int>> = mutableListOf()
    var MAX_X: Long? = null
    var MIN_X: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chart = view.findViewById(R.id.chart) as LineChart
        val hoursTV = view.findViewById(R.id.average_hours_tv) as TextView
        val feelsTV = view.findViewById(R.id.average_feelings_tv) as TextView

        var formatter = DateTimeFormatter.ofPattern("M/d/yyyy")
        formatter = formatter.withLocale(Locale.US)

        lifecycleScope.launch(Dispatchers.IO) {
            // get average feeling for textview
            val feelingAverage =
                (requireActivity().application as SleepEntryApplication).db.sleepEntryDao()
                    .getFeelingAverage()
            // get sleeping average for textview
            val sleepAverage =
                (requireActivity().application as SleepEntryApplication).db.sleepEntryDao()
                    .getHoursAverage()
            Log.d("dashboard", "$feelingAverage,$sleepAverage")


            (requireActivity().application as SleepEntryApplication).db.sleepEntryDao()
                .getChartEntries().collect { databaseList ->
                    databaseList.map { entity ->
                        ChartEntryEntity(
                            entity.sleptHours,
                            entity.feelingRating,
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
                    val hourEntries =
                        LineDataSet(
                            hours.map { hour -> Entry(hour.first, hour.second) },
                            "Slept hours"
                        )
                    hourEntries.setColors(resources.getColor(R.color.space_cadet))

                    val feelEntries =
                        LineDataSet(
                            feelings.map { feel -> Entry(feel.first, feel.second.toFloat()) },
                            "Feeling"
                        )
                    feelEntries.setColors(resources.getColor(R.color.tekhelet))
                    val sets: ArrayList<ILineDataSet> = ArrayList()
                    sets.add(hourEntries)
                    sets.add(feelEntries)
                    chart.notifyDataSetChanged()

                    val lineData = LineData(sets)
                    chart.data = lineData
                    setUpChart(chart)
                    chart.invalidate()
                    requireActivity().runOnUiThread {
                        hoursTV.text =
                            resources.getString(R.string.averageHoursText) + " " + sleepAverage.toString()
                        feelsTV.text =
                            resources.getString(R.string.averageFeelingText) + " " + feelingAverage.toString()
                    }

                }

        }


    }

    private fun setUpChart(chart: LineChart) {
        chart.description.text = "Log entries"
        chart.description.typeface = resources.getFont(R.font.poppins)
        chart.description.textSize = 10.0f
        chart.setNoDataText("No data entered yet.")
        chart.setBackgroundColor(resources.getColor(R.color.thistle))
        val xAxis = chart.xAxis
        //xAxis.position = XAxis.XAxisPosition.BOTTOM
        //xAxis.granularity = 1.0f

        val rightYAxis = chart.axisRight
        rightYAxis.setEnabled(false)

        /* val dateFormat: ValueFormatter = object : ValueFormatter() {
             override fun getFormattedValue(value: Float): String {

                 val simpleDateFormat = SimpleDateFormat("M/dd/yyyy", Locale.US)
                 Log.d("format", simpleDateFormat.format(value))
                 return simpleDateFormat.format(value).toString()
             }
         }
         xAxis.valueFormatter = dateFormat*/
        xAxis.isEnabled = false
        xAxis.axisMinimum = (MIN_X!!.toFloat())
        xAxis.axisMaximum = (MAX_X!!.toFloat())
    }


    companion object {
        @JvmStatic
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }
}