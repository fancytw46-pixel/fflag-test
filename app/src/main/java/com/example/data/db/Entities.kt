package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val targetFps: Int,
    val jsonConfig: String,
    val presetId: String,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "benchmarks")
data class BenchmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val averageFps: Float,
    val onePercentLow: Float,
    val frameTimeVarianceMs: Float,
    val presetName: String,
    val score: Int
)
