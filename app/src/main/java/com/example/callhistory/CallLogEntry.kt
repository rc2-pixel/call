package com.example.callhistory

data class CallLogEntry(
    val number: String,
    val name: String,
    val type: CallType,
    val date: String,
    val duration: String
)

enum class CallType {
    INCOMING,   // 着信
    OUTGOING,   // 発信
    MISSED,     // 不在着信
    REJECTED,   // 拒否
    UNKNOWN     // 不明
}
