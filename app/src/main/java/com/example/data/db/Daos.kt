package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles ORDER BY updatedAt DESC")
    fun getAllProfiles(): Flow<List<ProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity): Long

    @Delete
    suspend fun deleteProfile(profile: ProfileEntity)

    @Query("DELETE FROM profiles WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface BenchmarkDao {
    @Query("SELECT * FROM benchmarks ORDER BY timestamp DESC")
    fun getAllBenchmarks(): Flow<List<BenchmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBenchmark(benchmark: BenchmarkEntity): Long

    @Query("DELETE FROM benchmarks")
    suspend fun clearHistory()
}
