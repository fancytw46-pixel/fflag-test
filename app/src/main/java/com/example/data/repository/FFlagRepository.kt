package com.example.data.repository

import com.example.data.datasource.DefaultFFlags
import com.example.data.db.BenchmarkDao
import com.example.data.db.BenchmarkEntity
import com.example.data.db.ProfileDao
import com.example.data.db.ProfileEntity
import com.example.data.model.FFlag
import com.example.data.model.OptimizationPreset
import kotlinx.coroutines.flow.Flow

class FFlagRepository(
    private val profileDao: ProfileDao,
    private val benchmarkDao: BenchmarkDao
) {
    fun getAllProfiles(): Flow<List<ProfileEntity>> = profileDao.getAllProfiles()

    suspend fun saveProfile(
        name: String,
        description: String,
        targetFps: Int,
        jsonConfig: String,
        presetId: String
    ): Long {
        val entity = ProfileEntity(
            name = name,
            description = description,
            targetFps = targetFps,
            jsonConfig = jsonConfig,
            presetId = presetId,
            updatedAt = System.currentTimeMillis()
        )
        return profileDao.insertProfile(entity)
    }

    suspend fun deleteProfile(id: Long) = profileDao.deleteById(id)

    fun getAllBenchmarks(): Flow<List<BenchmarkEntity>> = benchmarkDao.getAllBenchmarks()

    suspend fun saveBenchmark(benchmark: BenchmarkEntity) = benchmarkDao.insertBenchmark(benchmark)

    suspend fun clearBenchmarks() = benchmarkDao.clearHistory()

    fun getDefaultFlags(): List<FFlag> = DefaultFFlags.getDefaultFlags()

    fun getPresets(): List<OptimizationPreset> = DefaultFFlags.getPresets()
}
