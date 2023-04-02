package com.codepath.bitfit2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


private const val TAG = "SleepEntryAdapter/"

class SleepEntryRecyclerAdapter(

    private val entries: List<SleepEntry>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<SleepEntryRecyclerAdapter.SleepEntryViewHolder>() {
    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepEntryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_sleep_entry, parent, false)
        return SleepEntryViewHolder(view)
    }

    inner class SleepEntryViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mEntryDate: TextView = mView.findViewById<View>(R.id.date_log) as TextView
        val mFeelingRating: TextView = mView.findViewById<View>(R.id.number_feeling_tv) as TextView
        val mHours: TextView = mView.findViewById<View>(R.id.number_slept_hours_tv) as TextView
        val mNotes: TextView = mView.findViewById<View>(R.id.sleep_log_notes_tv) as TextView

        override fun toString(): String {
            return mNotes.toString()
        }
    }

    override fun onBindViewHolder(holder: SleepEntryViewHolder, position: Int) {
        val sleepEntry = entries[position]
        holder.mHours.text = sleepEntry.hours.toString()
        holder.mNotes.text = sleepEntry.notes
        holder.mEntryDate.text = sleepEntry.logDate
        holder.mFeelingRating.text = sleepEntry.feeling.toString()

    }


}
