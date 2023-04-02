package com.codepath.bitfit2

import android.app.Application

class SleepEntryApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this) }
}