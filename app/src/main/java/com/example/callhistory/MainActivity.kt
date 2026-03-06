package com.example.callhistory

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_READ_CALL_LOG = 1001
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CallLogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CallLogAdapter(emptyList())
        recyclerView.adapter = adapter

        checkAndRequestPermission()
    }

    private fun checkAndRequestPermission() {
        val permission = Manifest.permission.READ_CALL_LOG
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            loadCallHistory()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_READ_CALL_LOG)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_READ_CALL_LOG) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadCallHistory()
            } else {
                Toast.makeText(this, "通話履歴の権限が必要です", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadCallHistory() {
        val callLogs = mutableListOf<CallLogEntry>()
        val projection = arrayOf(
            CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION,
            CallLog.Calls.CACHED_NAME
        )

        val cursor: Cursor? = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            null,
            null,
            "${CallLog.Calls.DATE} DESC"
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
            val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)
            val nameIndex = it.getColumnIndex(CallLog.Calls.CACHED_NAME)

            while (it.moveToNext()) {
                val number = it.getString(numberIndex) ?: "不明"
                val type = it.getInt(typeIndex)
                val date = it.getLong(dateIndex)
                val duration = it.getLong(durationIndex)
                val name = it.getString(nameIndex) ?: ""

                val callType = when (type) {
                    CallLog.Calls.INCOMING_TYPE -> CallType.INCOMING
                    CallLog.Calls.OUTGOING_TYPE -> CallType.OUTGOING
                    CallLog.Calls.MISSED_TYPE -> CallType.MISSED
                    CallLog.Calls.REJECTED_TYPE -> CallType.REJECTED
                    else -> CallType.UNKNOWN
                }

                val formattedDate = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN).format(Date(date))
                val formattedDuration = formatDuration(duration)

                callLogs.add(
                    CallLogEntry(
                        number = number,
                        name = name,
                        type = callType,
                        date = formattedDate,
                        duration = formattedDuration
                    )
                )
            }
        }

        adapter.updateData(callLogs)
    }

    private fun formatDuration(seconds: Long): String {
        return when {
            seconds == 0L -> "-"
            seconds < 60 -> "${seconds}秒"
            else -> "${seconds / 60}分${seconds % 60}秒"
        }
    }
}
