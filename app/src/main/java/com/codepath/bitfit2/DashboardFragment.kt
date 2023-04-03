package com.codepath.bitfit2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet


class DashboardFragment : Fragment() {
    private var entries = mutableListOf<SleepEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // val chartEntries = entries.map { Entry(it.xValue, it.yValue) }

       // val dataSet = LineDataSet(chartEntries, "Label") // add entries to dataset


        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }
}