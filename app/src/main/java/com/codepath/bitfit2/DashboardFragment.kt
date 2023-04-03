package com.codepath.bitfit2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
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
        var formatter = DateTimeFormatter.ofPattern("M/d/yyyy")
        formatter = formatter.withLocale(Locale.US)
        lifecycleScope.launch {
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
            Log.d("dash", hours.toString())
            Log.d("dash", feelings.toString())
        }
        val chartEntries = hours.map { hour -> Entry(hour.first, hour.second) }
        val dataSet = LineDataSet(chartEntries, "Label")
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate() // refresh
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }
}