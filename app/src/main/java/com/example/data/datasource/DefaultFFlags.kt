package com.example.data.datasource

import com.example.data.model.FFlag
import com.example.data.model.FFlagCategory
import com.example.data.model.FFlagType
import com.example.data.model.ImpactLevel
import com.example.data.model.OptimizationPreset
import com.example.data.model.PresetId

object DefaultFFlags {

    fun getDefaultFlags(): List<FFlag> {
        return listOf(
            FFlag(
                key = "DFIntTaskSchedulerTargetFps",
                name = "Target Framerate (FPS Unlocker)",
                description = "Unlocks the hardcoded 60 FPS cap to match your high refresh rate screen (90Hz, 120Hz, 144Hz, 240Hz).",
                category = FFlagCategory.PERFORMANCE,
                type = FFlagType.INTEGER,
                defaultValue = "60",
                currentValue = "120",
                recommendedBoostValue = "240",
                minInt = 30,
                maxInt = 360,
                intStep = 30,
                impact = ImpactLevel.MAX_FPS
            ),
            FFlag(
                key = "FFlagDebugDisableShadowCasting",
                name = "Disable Shadow Casting",
                description = "Disables real-time dynamic shadow mapping across all parts and characters, saving massive GPU shader fillrate.",
                category = FFlagCategory.GRAPHICS,
                type = FFlagType.BOOLEAN,
                defaultValue = "false",
                currentValue = "true",
                recommendedBoostValue = "true",
                impact = ImpactLevel.MAX_FPS
            ),
            FFlag(
                key = "FIntRenderShadowIntensity",
                name = "Shadow Rendering Intensity",
                description = "Sets shadow darkness and render passes. 0 completely disables shadow calculation in the rendering pipeline.",
                category = FFlagCategory.GRAPHICS,
                type = FFlagType.INTEGER,
                defaultValue = "100",
                currentValue = "0",
                recommendedBoostValue = "0",
                minInt = 0,
                maxInt = 100,
                intStep = 10,
                impact = ImpactLevel.MAX_FPS
            ),
            FFlag(
                key = "FFlagDisablePostFx",
                name = "Disable Post-Processing FX",
                description = "Turns off bloom, sun rays, depth of field, color correction, and blur passes for maximum clarity and raw speed.",
                category = FFlagCategory.GRAPHICS,
                type = FFlagType.BOOLEAN,
                defaultValue = "false",
                currentValue = "true",
                recommendedBoostValue = "true",
                impact = ImpactLevel.MAX_FPS
            ),
            FFlag(
                key = "FFlagGlobalWindRendering",
                name = "Global Wind Simulation",
                description = "Controls wind-driven bending on leaves, foliage, and particle emitters. Disabling stops continuous physics simulations.",
                category = FFlagCategory.PERFORMANCE,
                type = FFlagType.BOOLEAN,
                defaultValue = "true",
                currentValue = "false",
                recommendedBoostValue = "false",
                impact = ImpactLevel.MAX_FPS
            ),
            FFlag(
                key = "DFIntDebugFRMMaxGrassDistance",
                name = "Max Grass Draw Distance",
                description = "Maximum distance at which animated 3D terrain grass renders. 0 completely strips grass, boosting FPS in large open worlds.",
                category = FFlagCategory.TEXTURES_TERRAIN,
                type = FFlagType.INTEGER,
                defaultValue = "400",
                currentValue = "0",
                recommendedBoostValue = "0",
                minInt = 0,
                maxInt = 800,
                intStep = 50,
                impact = ImpactLevel.MAX_FPS
            ),
            FFlag(
                key = "FFlagRenderEnableLowFx",
                name = "Enable Low-FX Particles",
                description = "Enforces lightweight particle and explosion shaders, preventing extreme frame drops during intense combat and raids.",
                category = FFlagCategory.GRAPHICS,
                type = FFlagType.BOOLEAN,
                defaultValue = "false",
                currentValue = "true",
                recommendedBoostValue = "true",
                impact = ImpactLevel.MAX_FPS
            ),
            FFlag(
                key = "FFlagDebugDisableDeferredPhysics",
                name = "Disable Deferred Physics",
                description = "Executes player physics calculations immediately each frame, drastically reducing perceived hit registration and input lag.",
                category = FFlagCategory.PHYSICS_LATENCY,
                type = FFlagType.BOOLEAN,
                defaultValue = "false",
                currentValue = "true",
                recommendedBoostValue = "true",
                impact = ImpactLevel.LATENCY_REDUCTION
            ),
            FFlag(
                key = "DFIntMaxFrameBufferSize",
                name = "Max Frame Buffer Queue",
                description = "Sets GPU frame queuing depth. 1 delivers instant reaction time with minimal render-ahead latency for competitive PvP.",
                category = FFlagCategory.PHYSICS_LATENCY,
                type = FFlagType.INTEGER,
                defaultValue = "2",
                currentValue = "1",
                recommendedBoostValue = "1",
                minInt = 1,
                maxInt = 3,
                intStep = 1,
                impact = ImpactLevel.LATENCY_REDUCTION
            ),
            FFlag(
                key = "DFFlagDisableDPIScale",
                name = "Disable DPI Oversampling",
                description = "Prevents device DPI scaling overhead, lowering internal rendering resolution slightly for a huge boost on dense phone displays.",
                category = FFlagCategory.PERFORMANCE,
                type = FFlagType.BOOLEAN,
                defaultValue = "false",
                currentValue = "true",
                recommendedBoostValue = "true",
                impact = ImpactLevel.MEMORY_SAVING
            ),
            FFlag(
                key = "FFlagFastGPULightBugsFix",
                name = "Fast GPU Light Bugs Fix",
                description = "Enables hardware-level optimizations for mobile GPU shader dispatch, reducing sudden stutter in illuminated zones.",
                category = FFlagCategory.PERFORMANCE,
                type = FFlagType.BOOLEAN,
                defaultValue = "false",
                currentValue = "true",
                recommendedBoostValue = "true",
                impact = ImpactLevel.MAX_FPS
            ),
            FFlag(
                key = "FFlagDebugDisplayFPS",
                name = "Show Native Engine FPS & Ping",
                description = "Displays the built-in Roblox engine framerate and frame latency counter overlay in real-time.",
                category = FFlagCategory.HUD_DEBUG,
                type = FFlagType.BOOLEAN,
                defaultValue = "false",
                currentValue = "true",
                recommendedBoostValue = "true",
                impact = ImpactLevel.VISUAL_TWEAK
            ),
            FFlag(
                key = "DFIntTextureQualityOverride",
                name = "Texture Quality Override",
                description = "Overrides texture resolution downsampling. 1 uses low-res textures (drastically saving RAM & VRAM), 3 is standard quality.",
                category = FFlagCategory.TEXTURES_TERRAIN,
                type = FFlagType.INTEGER,
                defaultValue = "3",
                currentValue = "1",
                recommendedBoostValue = "1",
                minInt = 1,
                maxInt = 3,
                intStep = 1,
                impact = ImpactLevel.MEMORY_SAVING
            ),
            FFlag(
                key = "FFlagPreloadAllFonts",
                name = "Preload UI Fonts",
                description = "Preloads custom game typography into memory at startup to eliminate micro-freezes whenever new HUD dialogs open.",
                category = FFlagCategory.PERFORMANCE,
                type = FFlagType.BOOLEAN,
                defaultValue = "false",
                currentValue = "true",
                recommendedBoostValue = "true",
                impact = ImpactLevel.VISUAL_TWEAK
            ),
            FFlag(
                key = "FFlagGameNetUsePacketCompressor",
                name = "Network Packet Compression",
                description = "Enforces compressed network payloads between client and server, smoothing out high-ping lobby packet loss.",
                category = FFlagCategory.NETWORK,
                type = FFlagType.BOOLEAN,
                defaultValue = "true",
                currentValue = "true",
                recommendedBoostValue = "true",
                impact = ImpactLevel.LATENCY_REDUCTION
            ),
            FFlag(
                key = "DFIntPhysicsSendRate",
                name = "Physics Packet Send Rate (Hz)",
                description = "Client physics update frequency. 60Hz ensures snappy projectile trajectories and collision checks.",
                category = FFlagCategory.NETWORK,
                type = FFlagType.INTEGER,
                defaultValue = "30",
                currentValue = "60",
                recommendedBoostValue = "60",
                minInt = 30,
                maxInt = 120,
                intStep = 15,
                impact = ImpactLevel.LATENCY_REDUCTION
            ),
            FFlag(
                key = "FFlagDebugSkyGray",
                name = "Minimal Flat Sky",
                description = "Replaces heavy volumetric atmosphere skyboxes with simple lightweight gradient shading.",
                category = FFlagCategory.GRAPHICS,
                type = FFlagType.BOOLEAN,
                defaultValue = "false",
                currentValue = "false",
                recommendedBoostValue = "true",
                impact = ImpactLevel.MAX_FPS
            ),
            FFlag(
                key = "DFIntInterpolationNumTicks",
                name = "Camera Movement Smoothing",
                description = "Interpolation ticks for camera panning. 1 makes camera response instantaneous without visual lag.",
                category = FFlagCategory.PHYSICS_LATENCY,
                type = FFlagType.INTEGER,
                defaultValue = "2",
                currentValue = "1",
                recommendedBoostValue = "1",
                minInt = 1,
                maxInt = 4,
                intStep = 1,
                impact = ImpactLevel.LATENCY_REDUCTION
            ),
            FFlag(
                key = "FFlagVoiceChatLowBandwidth",
                name = "Voice Chat Low Bandwidth Mode",
                description = "Reduces voice audio stream buffer size to prevent network congestion from draining in-game framerate.",
                category = FFlagCategory.NETWORK,
                type = FFlagType.BOOLEAN,
                defaultValue = "false",
                currentValue = "true",
                recommendedBoostValue = "true",
                impact = ImpactLevel.MEMORY_SAVING
            ),
            FFlag(
                key = "FIntFRMMinGrassDistance",
                name = "Min Grass Distance",
                description = "Near-field grass rendering threshold. Setting to 0 prevents nearby grass clustering.",
                category = FFlagCategory.TEXTURES_TERRAIN,
                type = FFlagType.INTEGER,
                defaultValue = "20",
                currentValue = "0",
                recommendedBoostValue = "0",
                minInt = 0,
                maxInt = 50,
                intStep = 10,
                impact = ImpactLevel.MAX_FPS
            )
        )
    }

    fun getPresets(): List<OptimizationPreset> {
        return listOf(
            OptimizationPreset(
                id = PresetId.POTATO_ULTRA,
                title = "Potato / Extreme FPS",
                subtitle = "Maximum performance unlock. Strips shadows, grass, wind & effects. Targets 240 FPS uncapped.",
                targetFps = 240,
                flagOverrides = mapOf(
                    "DFIntTaskSchedulerTargetFps" to "240",
                    "FFlagDebugDisableShadowCasting" to "true",
                    "FIntRenderShadowIntensity" to "0",
                    "FFlagDisablePostFx" to "true",
                    "FFlagGlobalWindRendering" to "false",
                    "DFIntDebugFRMMaxGrassDistance" to "0",
                    "FFlagRenderEnableLowFx" to "true",
                    "DFIntTextureQualityOverride" to "1",
                    "DFFlagDisableDPIScale" to "true",
                    "FFlagFastGPULightBugsFix" to "true",
                    "FFlagDebugDisplayFPS" to "true",
                    "FIntFRMMinGrassDistance" to "0",
                    "FFlagDebugSkyGray" to "true"
                ),
                estimatedFpsBoost = "+85%",
                latencyReduction = "-38ms"
            ),
            OptimizationPreset(
                id = PresetId.COMPETITIVE_PVP,
                title = "Competitive PvP (Arsenal & Rivals)",
                subtitle = "Optimized for lightning response, 144 FPS, zero input lag and clean bullet visibility.",
                targetFps = 144,
                flagOverrides = mapOf(
                    "DFIntTaskSchedulerTargetFps" to "144",
                    "FFlagDebugDisableDeferredPhysics" to "true",
                    "DFIntMaxFrameBufferSize" to "1",
                    "DFIntInterpolationNumTicks" to "1",
                    "DFIntPhysicsSendRate" to "60",
                    "FFlagDisablePostFx" to "true",
                    "FFlagRenderEnableLowFx" to "true",
                    "FFlagDebugDisplayFPS" to "true",
                    "FFlagGameNetUsePacketCompressor" to "true"
                ),
                estimatedFpsBoost = "+55%",
                latencyReduction = "-45ms"
            ),
            OptimizationPreset(
                id = PresetId.SMOOTH_120,
                title = "Smooth 120 FPS Balanced",
                subtitle = "Perfect for 90Hz/120Hz displays. Smooth gameplay while keeping graphics crisp and readable.",
                targetFps = 120,
                flagOverrides = mapOf(
                    "DFIntTaskSchedulerTargetFps" to "120",
                    "FFlagFastGPULightBugsFix" to "true",
                    "FFlagPreloadAllFonts" to "true",
                    "FFlagDebugDisplayFPS" to "true",
                    "FFlagGameNetUsePacketCompressor" to "true",
                    "DFIntTextureQualityOverride" to "2"
                ),
                estimatedFpsBoost = "+40%",
                latencyReduction = "-22ms"
            ),
            OptimizationPreset(
                id = PresetId.BATTERY_SAVER,
                title = "Battery & Thermal Saver",
                subtitle = "Capped at 60 FPS with reduced GPU workload to prevent thermal throttling and battery drain.",
                targetFps = 60,
                flagOverrides = mapOf(
                    "DFIntTaskSchedulerTargetFps" to "60",
                    "FFlagGlobalWindRendering" to "false",
                    "FFlagDisablePostFx" to "true",
                    "DFIntDebugFRMMaxGrassDistance" to "100",
                    "FFlagVoiceChatLowBandwidth" to "true"
                ),
                estimatedFpsBoost = "Cool & Stable",
                latencyReduction = "Low Heat"
            ),
            OptimizationPreset(
                id = PresetId.CUSTOM,
                title = "Blox Fruits Raid Special",
                subtitle = "Custom profile tuned for heavy fruit attacks, raid particle reduction & Sea Beast stability.",
                targetFps = 120,
                flagOverrides = mapOf(
                    "DFIntTaskSchedulerTargetFps" to "120",
                    "FFlagRenderEnableLowFx" to "true",
                    "FFlagDisablePostFx" to "true",
                    "FFlagDebugDisableShadowCasting" to "true",
                    "DFIntDebugFRMMaxGrassDistance" to "0",
                    "FFlagGameNetUsePacketCompressor" to "true",
                    "FFlagDebugDisplayFPS" to "true"
                ),
                estimatedFpsBoost = "+60%",
                latencyReduction = "-30ms",
                isGameSpecific = true,
                gameTag = "Blox Fruits"
            )
        )
    }
}
