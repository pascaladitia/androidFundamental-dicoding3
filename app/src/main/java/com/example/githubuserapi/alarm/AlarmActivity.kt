package com.example.githubuserapi.alarm

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuserapi.R
import kotlinx.android.synthetic.main.activity_alarm.*

class AlarmActivity: AppCompatActivity() {

    lateinit var alarmReceiver: AlarmReceiver
    companion object {
        const val SHARED_PREFERENCE = "sharedpreference"
        const val BOOLEAN = "boolean"
        internal val TAG = AlarmActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        supportActionBar?.title = "Alarm"

        alarmReceiver = AlarmReceiver()
        setData()
    }

    private fun setData() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)

        val getBoolean = sharedPreferences.getBoolean(BOOLEAN, false)
        cb_reminder.isChecked = getBoolean

        cb_reminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val editor = sharedPreferences.edit()
                editor.apply {
                    putBoolean(BOOLEAN, true)
                }.apply()

                alarmReceiver.setRepeatAlarm(this, AlarmReceiver.EXTRA_TYPE, "09:00")
                Log.d(TAG, "Alarm On")
            } else {
                val editor = sharedPreferences.edit()
                editor.apply {
                    putBoolean(BOOLEAN, false)
                }.apply()

                alarmReceiver.cancelAlarm(this, AlarmReceiver.ALARM_REPEATING)
                Log.d(TAG, "Alarm Off")
            }

        }
    }
}