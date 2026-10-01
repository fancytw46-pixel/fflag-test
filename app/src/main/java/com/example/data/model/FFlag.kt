package com.example.data.model

enum class FFlagCategory(val displayName: String, val iconName: String) {
    ALL("All Flags", "tune"),
    PERFORMANCE("Performance & FPS", "speed"),
    GRAPHICS("Graphics & Lighting", "wb_sunny"),
    PHYSICS_LATENCY("Physics & Latency", "bolt"),
    HUD_DEBUG("HUD & Diagnostics", "query_stats"),
    TEXTURES_TERRAIN("Textures & Terrain", "terrain"),
    NETWORK("Network & Ping", "wifi")
}

enum class FFlagType {
    BOOLEAN,
    INTEGER,
    STRING
}

enum class ImpactLevel(val label: String, val scoreImpact: String) {
    MAX_FPS("Massive FPS Boost", "+40-80% FPS"),
    LATENCY_REDUCTION("Ultra Low Latency", "-30ms Ping/Input"),
    MEMORY_SAVING("Memory & Heat Saver", "Cuts RAM/VRAM"),
    VISUAL_TWEAK("Clarity & Visual", "Clean Sightlines")
}

data class FFlag(
    val key: String,
    val name: String,
    val description: String,
    val category: FFlagCategory,
    val type: FFlagType,
    val defaultValue: String,
    var currentValue: String,
    val recommendedBoostValue: String,
    val minInt: Int? = null,
    val maxInt: Int? = null,
    val intStep: Int? = null,
    val options: List<String>? = null,
    val impact: ImpactLevel = ImpactLevel.MAX_FPS,
    val isCustom: Boolean = false,
    var isEnabled: Boolean = true
) {
    fun getFormattedJsonValue(): Any {
        return when (type) {
            FFlagType.BOOLEAN -> currentValue.equals("true", ignoreCase = true)
            FFlagType.INTEGER -> currentValue.toIntOrNull() ?: 0
            FFlagType.STRING -> currentValue
        }
    }
}

enum class PresetId(val title: String, val subtitle: String, val targetFps: Int) {
    POTATO_ULTRA("Potato / Ultra FPS", "Max FPS unlock, removes shadows, wind, grass & post-fx", 240),
    COMPETITIVE_PVP("Competitive PvP", "144 FPS, zero input delay, high physics tick rate", 144),
    SMOOTH_120("Smooth 120 FPS", "Balanced 120 FPS unlock with intact textures & UI", 120),
    BATTERY_SAVER("Cool Battery Saver", "60 FPS capped with low GPU load to avoid throttling", 60),
    CINEMATIC_ULTRA("Cinematic Graphics", "High fidelity voxel lighting, softening & max draw", 60),
    CUSTOM("Custom Profile", "Personalized custom FFlag configuration", 120)
}

data class OptimizationPreset(
    val id: PresetId,
    val title: String,
    val subtitle: String,
    val targetFps: Int,
    val flagOverrides: Map<String, String>,
    val estimatedFpsBoost: String,
    val latencyReduction: String,
    val isGameSpecific: Boolean = false,
    val gameTag: String? = null
)

data class DeviceVitals(
    val totalRamMb: Long = 6144,
    val availableRamMb: Long = 2840,
    val ramUsagePercent: Int = 54,
    val batteryTempCelsius: Float = 31.5f,
    val batteryLevelPercent: Int = 85,
    val displayRefreshRateHz: Int = 120,
    val cpuCoreCount: Int = 8,
    val deviceModel: String = "Android Gaming Device",
    val isGameModeActive: Boolean = false,
    val isBoostActive: Boolean = false
)

data class PingTarget(
    val region: String,
    val location: String,
    val host: String,
    var latencyMs: Int? = null,
    var status: String = "Ready"
)

data class BenchmarkRecord(
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val averageFps: Float,
    val onePercentLowFps: Float,
    val frameTimeVarianceMs: Float,
    val presetUsed: String,
    val performanceScore: Int
)
