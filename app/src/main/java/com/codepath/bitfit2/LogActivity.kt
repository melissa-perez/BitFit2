package com.codepath.bitfit2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.codepath.bitfit2.databinding.SleepLogActivityBinding
import com.google.android.material.slider.Slider
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class LogActivity : AppCompatActivity() {
    private var binding: SleepLogActivityBinding? = null
    private lateinit var sleptHoursSlider: Slider
    private lateinit var feelingRatingSlider: Slider
    private lateinit var notesEditText: EditText
    private lateinit var datePicker: DatePicker
    private lateinit var saveBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SleepLogActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        sleptHoursSlider = findViewById(R.id.sleptSlider)
        feelingRatingSlider = findViewById(R.id.feelingSlider)
        datePicker = findViewById(R.id.datePicker)
        notesEditText = findViewById(R.id.notes)
        saveBtn = findViewById(R.id.save_entry_btn)



        saveBtn.setOnClickListener {
            val hours = sleptHoursSlider.value
            val feelings = feelingRatingSlider.value.toInt()
            val notes = notesEditText.text.toString()
            val month = datePicker.month + 1
            val day = datePicker.dayOfMonth
            val year = datePicker.year
            Log.d("date", ("$day $month $year").toString())

            val date = ("$month/$day/$year").toString()

            let {
                lifecycleScope.launch(IO) {

                    (application as SleepEntryApplication).db.sleepEntryDao().insert(
                        SleepEntryEntity(
                            sleptHours = hours,
                            feelingRating = feelings,
                            sleepNotes = notes,
                            sleepDate = date
                        )
                    )
                }

            }

            // reset data
            sleptHoursSlider.value = 0.0f
            feelingRatingSlider.value = 0f
            notesEditText.text.clear()

            finish()
        }
    }

}
