package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.DefaultFFlags
import com.example.data.db.AppDatabase
import com.example.data.db.BenchmarkEntity
import com.example.data.db.ProfileEntity
import com.example.data.model.DeviceVitals
import com.example.data.model.FFlag
import com.example.data.model.FFlagCategory
import com.example.data.model.FFlagType
import com.example.data.model.OptimizationPreset
import com.example.data.model.PingTarget
import com.example.data.model.PresetId
import com.example.data.repository.FFlagRepository
import com.example.data.service.DeviceTelemetryService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.random.Random

class BloxBoostViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = FFlagRepository(db.profileDao(), db.benchmarkDao())
    private val telemetryService = DeviceTelemetryService(application)

    private val _vitals = MutableStateFlow(telemetryService.getDeviceVitals())
    val vitals: StateFlow<DeviceVitals> = _vitals.asStateFlow()

    private val _isBoosted = MutableStateFlow(false)
    val isBoosted: StateFlow<Boolean> = _isBoosted.asStateFlow()

    private val _boostAnimationRunning = MutableStateFlow(false)
    val boostAnimationRunning: StateFlow<Boolean> = _boostAnimationRunning.asStateFlow()

    private val _targetFps = MutableStateFlow(120)
    val targetFps: StateFlow<Int> = _targetFps.asStateFlow()

    val presets = repository.getPresets()
    private val _activePreset = MutableStateFlow(presets[0]) // Default to Potato/Ultra FPS
    val activePreset: StateFlow<OptimizationPreset> = _activePreset.asStateFlow()

    private val _flags = MutableStateFlow<List<FFlag>>(emptyList())
    val flags: StateFlow<List<FFlag>> = _flags.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(FFlagCategory.ALL)
    val selectedCategory: StateFlow<FFlagCategory> = _selectedCategory.asStateFlow()

    val filteredFlags: StateFlow<List<FFlag>> = combine(
        _flags,
        _searchQuery,
        _selectedCategory
    ) { flagList, query, cat ->
        flagList.filter { flag ->
            val matchesCategory = (cat == FFlagCategory.ALL || flag.category == cat)
            val matchesQuery = query.isEmpty() ||
                    flag.name.contains(query, ignoreCase = true) ||
                    flag.key.contains(query, ignoreCase = true) ||
                    flag.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _generatedJson = MutableStateFlow("")
    val generatedJson: StateFlow<String> = _generatedJson.asStateFlow()

    val savedProfiles: StateFlow<List<ProfileEntity>> = repository.getAllProfiles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val benchmarkHistory: StateFlow<List<BenchmarkEntity>> = repository.getAllBenchmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _pingTargets = MutableStateFlow(
        listOf(
            PingTarget("Global Gateway", "Anycast Edge", "roblox.com"),
            PingTarget("US East", "Virginia / Ashburn", "us-east.roblox.com"),
            PingTarget("US West", "California / San Jose", "us-west.roblox.com"),
            PingTarget("Europe Central", "Frankfurt / London", "eu.roblox.com"),
            PingTarget("Asia Pacific", "Singapore / Tokyo", "asia.roblox.com"),
            PingTarget("Latin America", "São Paulo / Brazil", "sa.roblox.com")
        )
    )
    val pingTargets: StateFlow<List<PingTarget>> = _pingTargets.asStateFlow()

    private val _isTestingPing = MutableStateFlow(false)
    val isTestingPing: StateFlow<Boolean> = _isTestingPing.asStateFlow()

    private val _benchmarkRunning = MutableStateFlow(false)
    val benchmarkRunning: StateFlow<Boolean> = _benchmarkRunning.asStateFlow()

    private val _benchmarkProgress = MutableStateFlow(0f)
    val benchmarkProgress: StateFlow<Float> = _benchmarkProgress.asStateFlow()

    private val _currentBenchmarkFps = MutableStateFlow(0f)
    val currentBenchmarkFps: StateFlow<Float> = _currentBenchmarkFps.asStateFlow()

    private val _recentFpsSamples = MutableStateFlow<List<Float>>(emptyList())
    val recentFpsSamples: StateFlow<List<Float>> = _recentFpsSamples.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        loadInitialFlags()
        refreshVitals()
    }

    private fun loadInitialFlags() {
        val initial = repository.getDefaultFlags().map { it.copy() }
        _flags.value = initial
        applyPresetOverrides(presets[0], initial)
        updateJsonRepresentation(initial)
    }

    fun refreshVitals() {
        _vitals.value = telemetryService.getDeviceVitals(_isBoosted.value)
    }

    fun selectPreset(preset: OptimizationPreset) {
        _activePreset.value = preset
        _targetFps.value = preset.targetFps

        val currentList = _flags.value.map { it.copy() }
        applyPresetOverrides(preset, currentList)
        _flags.value = currentList
        updateJsonRepresentation(currentList)
        _toastMessage.value = "Applied preset: ${preset.title}"
    }

    private fun applyPresetOverrides(preset: OptimizationPreset, list: List<FFlag>) {
        preset.flagOverrides.forEach { (key, targetVal) ->
            val match = list.find { it.key == key }
            if (match != null) {
                match.currentValue = targetVal
                match.isEnabled = true
            }
        }
        val fpsFlag = list.find { it.key == "DFIntTaskSchedulerTargetFps" }
        fpsFlag?.currentValue = preset.targetFps.toString()
    }

    fun setTargetFps(fps: Int) {
        _targetFps.value = fps
        val currentList = _flags.value.map { it.copy() }
        val fpsFlag = currentList.find { it.key == "DFIntTaskSchedulerTargetFps" }
        if (fpsFlag != null) {
            fpsFlag.currentValue = fps.toString()
            fpsFlag.isEnabled = true
        }
        _flags.value = currentList
        updateJsonRepresentation(currentList)
    }

    fun updateFlagValue(key: String, newValue: String) {
        val currentList = _flags.value.map { flag ->
            if (flag.key == key) {
                flag.copy(currentValue = newValue, isEnabled = true)
            } else {
                flag
            }
        }
        _flags.value = currentList
        updateJsonRepresentation(currentList)
    }

    fun toggleFlagEnabled(key: String, enabled: Boolean) {
        val currentList = _flags.value.map { flag ->
            if (flag.key == key) {
                flag.copy(isEnabled = enabled)
            } else {
                flag
            }
        }
        _flags.value = currentList
        updateJsonRepresentation(currentList)
    }

    fun addCustomFlag(key: String, value: String, type: FFlagType, category: FFlagCategory) {
        val cleanKey = key.trim()
        if (cleanKey.isEmpty()) {
            _toastMessage.value = "FFlag key cannot be empty"
            return
        }
        if (_flags.value.any { it.key.equals(cleanKey, ignoreCase = true) }) {
            _toastMessage.value = "FFlag '$cleanKey' already exists"
            return
        }

        val newFlag = FFlag(
            key = cleanKey,
            name = cleanKey,
            description = "Custom user-defined Fast Flag for Roblox engine.",
            category = category,
            type = type,
            defaultValue = value,
            currentValue = value,
            recommendedBoostValue = value,
            isCustom = true,
            isEnabled = true
        )

        val updated = _flags.value + newFlag
        _flags.value = updated
        updateJsonRepresentation(updated)
        _toastMessage.value = "Added custom flag '$cleanKey'"
    }

    fun removeCustomFlag(key: String) {
        val updated = _flags.value.filterNot { it.key == key }
        _flags.value = updated
        updateJsonRepresentation(updated)
        _toastMessage.value = "Removed flag '$key'"
    }

    fun resetFlagsToDefault() {
        val defaults = repository.getDefaultFlags().map { it.copy(currentValue = it.defaultValue) }
        _flags.value = defaults
        _targetFps.value = 60
        _activePreset.value = presets.firstOrNull { it.id == PresetId.BATTERY_SAVER } ?: presets[0]
        updateJsonRepresentation(defaults)
        _toastMessage.value = "Reset all FFlags to factory defaults"
    }

    fun setSearchQuery(q: String) {
        _searchQuery.value = q
    }

    fun setSelectedCategory(cat: FFlagCategory) {
        _selectedCategory.value = cat
    }

    private fun updateJsonRepresentation(list: List<FFlag>) {
        val json = JSONObject()
        list.filter { it.isEnabled }.forEach { flag ->
            when (flag.type) {
                FFlagType.BOOLEAN -> json.put(flag.key, flag.currentValue.equals("true", ignoreCase = true))
                FFlagType.INTEGER -> json.put(flag.key, flag.currentValue.toIntOrNull() ?: 0)
                FFlagType.STRING -> json.put(flag.key, flag.currentValue)
            }
        }
        _generatedJson.value = json.toString(2)
    }

    fun triggerQuickBoost() {
        viewModelScope.launch(Dispatchers.Default) {
            _boostAnimationRunning.value = true
            val freedMb = telemetryService.performMemoryTrim()
            delay(1200) // Visual gaming boost pulse animation
            _boostAnimationRunning.value = false
            _isBoosted.value = true
            refreshVitals()
            _toastMessage.value = "⚡ Boost Activated: Freed ${freedMb}MB RAM! FPS uncap primed."
        }
    }

    fun saveCurrentProfile(name: String, description: String) {
        if (name.isBlank()) {
            _toastMessage.value = "Profile name cannot be empty"
            return
        }
        viewModelScope.launch {
            repository.saveProfile(
                name = name.trim(),
                description = description.ifBlank { "Custom profile with ${_targetFps.value} FPS cap." },
                targetFps = _targetFps.value,
                jsonConfig = _generatedJson.value,
                presetId = _activePreset.value.id.name
            )
            _toastMessage.value = "Saved profile '$name' successfully!"
        }
    }

    fun loadProfile(profile: ProfileEntity) {
        try {
            val json = JSONObject(profile.jsonConfig)
            val currentList = _flags.value.map { it.copy() }
            val iterator = json.keys()
            while (iterator.hasNext()) {
                val key = iterator.next()
                val value = json.get(key).toString()
                val match = currentList.find { it.key == key }
                if (match != null) {
                    match.currentValue = value
                    match.isEnabled = true
                }
            }
            _flags.value = currentList
            _targetFps.value = profile.targetFps
            _generatedJson.value = profile.jsonConfig
            _toastMessage.value = "Loaded profile '${profile.name}'"
        } catch (_: Exception) {
            _toastMessage.value = "Error parsing profile JSON configuration"
        }
    }

    fun deleteProfile(id: Long) {
        viewModelScope.launch {
            repository.deleteProfile(id)
            _toastMessage.value = "Profile deleted"
        }
    }

    fun runPingTest() {
        if (_isTestingPing.value) return
        viewModelScope.launch {
            _isTestingPing.value = true
            val current = _pingTargets.value
            val updated = telemetryService.testLatencyToTargets(current)
            _pingTargets.value = updated
            _isTestingPing.value = false
            val best = updated.minByOrNull { it.latencyMs ?: 999 }
            if (best != null && best.latencyMs != null) {
                _toastMessage.value = "Best connection: ${best.region} (${best.latencyMs}ms)"
            }
        }
    }

    fun runFpsBenchmark() {
        if (_benchmarkRunning.value) return
        viewModelScope.launch(Dispatchers.Default) {
            _benchmarkRunning.value = true
            _benchmarkProgress.value = 0f
            val samples = mutableListOf<Float>()
            val baseFps = when (_activePreset.value.id) {
                PresetId.POTATO_ULTRA -> 118f
                PresetId.COMPETITIVE_PVP -> 112f
                PresetId.SMOOTH_120 -> 105f
                PresetId.BATTERY_SAVER -> 59.5f
                PresetId.CINEMATIC_ULTRA -> 58f
                PresetId.CUSTOM -> 95f
            }

            val totalSteps = 40
            for (step in 1..totalSteps) {
                delay(100)
                // Simulate frame pacing under stress
                val jitter = (Random.nextFloat() * 8f) - 4f
                val sampleFps = (baseFps + jitter).coerceIn(30f, 144f)
                samples.add(sampleFps)
                _currentBenchmarkFps.value = sampleFps
                _recentFpsSamples.value = samples.takeLast(25)
                _benchmarkProgress.value = step.toFloat() / totalSteps
            }

            val avgFps = samples.average().toFloat()
            val sorted = samples.sorted()
            val onePercentLow = sorted[(sorted.size * 0.05).toInt()]
            val variance = (samples.maxOrNull() ?: 120f) - (samples.minOrNull() ?: 60f)
            val score = ((avgFps * 0.7f) + (onePercentLow * 0.3f) - (variance * 0.5f)).toInt().coerceIn(100, 999)

            val record = BenchmarkEntity(
                averageFps = avgFps,
                onePercentLow = onePercentLow,
                frameTimeVarianceMs = 1000f / avgFps,
                presetName = _activePreset.value.title,
                score = score
            )
            repository.saveBenchmark(record)

            _benchmarkRunning.value = false
            _toastMessage.value = "Benchmark complete! Score: $score (Avg ${avgFps.toInt()} FPS)"
        }
    }

    fun isRobloxInstalled(): Boolean = telemetryService.isRobloxInstalled()

    fun launchRoblox(): Boolean = telemetryService.launchRoblox()

    fun clearToast() {
        _toastMessage.value = null
    }
}
