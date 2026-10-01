package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.OptimizationPreset
import com.example.ui.components.CyberCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatGauge
import com.example.ui.theme.BlazeOrange
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberDark
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.HyperGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloxBoostViewModel

@Composable
fun DashboardScreen(
    viewModel: BloxBoostViewModel,
    onNavigateToFFlags: () -> Unit,
    onNavigateToExport: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val vitals by viewModel.vitals.collectAsState()
    val isBoosted by viewModel.isBoosted.collectAsState()
    val isBoosting by viewModel.boostAnimationRunning.collectAsState()
    val targetFps by viewModel.targetFps.collectAsState()
    val activePreset by viewModel.activePreset.collectAsState()
    val presets = viewModel.presets

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    fun triggerHaptic() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(50)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Hero Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, CyberBorder, RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_booster_banner),
                    contentDescription = "Roblox Booster Cockpit",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    CyberDark.copy(alpha = 0.85f),
                                    CyberDark
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isBoosted) HyperGreen else BlazeOrange)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isBoosted) "BOOST ACTIVE" else "READY TO TUNE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${targetFps} FPS Target",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = NeonCyan,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                    Text(
                        text = "Roblox FPS Booster & FFlag Hub",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                }
            }
        }

        // Quick Boost & Launch Actions Card
        item {
            CyberCard(
                borderColor = if (isBoosted) HyperGreen.copy(alpha = 0.6f) else NeonCyan.copy(alpha = 0.4f),
                backgroundColor = CyberCard
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBoosted) "Engine Optimized ⚡" else "One-Tap FPS Boost",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = if (isBoosted)
                                "RAM trimmed. FFlags set to ${activePreset.title}."
                            else
                                "Trims background memory & unlocks FPS target cap.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Boost Button
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(76.dp)
                            .scale(if (isBoosting) pulseScale else 1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isBoosted) HyperGreen else NeonCyan,
                            modifier = Modifier
                                .size(64.dp)
                                .testTag("quick_boost_button")
                                .clickable(enabled = !isBoosting) {
                                    triggerHaptic()
                                    viewModel.triggerQuickBoost()
                                },
                            shadowElevation = 8.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (isBoosting) {
                                    CircularProgressIndicator(
                                        color = Color.Black,
                                        modifier = Modifier.size(32.dp),
                                        strokeWidth = 3.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (isBoosted) Icons.Default.CheckCircle else Icons.Default.Bolt,
                                        contentDescription = "Quick Boost",
                                        tint = Color.Black,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Launch Roblox Button
                Button(
                    onClick = {
                        triggerHaptic()
                        viewModel.launchRoblox()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("launch_roblox_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Launch",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Launch Roblox Client",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }

        // Target FPS Selector
        item {
            CyberCard {
                SectionHeader(
                    title = "Target Framerate (FPS)",
                    subtitle = "Adjusts DFIntTaskSchedulerTargetFps client flag",
                    badgeText = "${targetFps} FPS"
                )
                Spacer(modifier = Modifier.height(12.dp))
                val fpsOptions = listOf(60, 90, 120, 144, 240, 360)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(fpsOptions) { fps ->
                        val isSelected = targetFps == fps
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) NeonCyan else CyberSurfaceVariant
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyanVariant else CyberBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    triggerHaptic()
                                    viewModel.setTargetFps(fps)
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("fps_pill_$fps")
                        ) {
                            Text(
                                text = if (fps == 360) "MAX (360)" else "$fps FPS",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.Black else TextPrimary
                                )
                            )
                        }
                    }
                }
            }
        }

        // Live Device Telemetry
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionHeader(
                    title = "Live Device Telemetry",
                    subtitle = "${vitals.deviceModel} • ${vitals.cpuCoreCount} Cores",
                    badgeText = "Real-Time"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatGauge(
                        icon = Icons.Default.Memory,
                        label = "RAM Usage",
                        value = "${vitals.ramUsagePercent}%",
                        subValue = "${vitals.availableRamMb} MB Free",
                        accentColor = if (vitals.ramUsagePercent > 80) BlazeOrange else NeonCyan,
                        modifier = Modifier.weight(1f)
                    )
                    StatGauge(
                        icon = Icons.Default.Speed,
                        label = "Display Rate",
                        value = "${vitals.displayRefreshRateHz} Hz",
                        subValue = "Hardware Refresh",
                        accentColor = ElectricViolet,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatGauge(
                        icon = Icons.Default.Thermostat,
                        label = "Battery Temp",
                        value = "${vitals.batteryTempCelsius}°C",
                        subValue = if (vitals.batteryTempCelsius > 40f) "Thermal Warning" else "Cool / Optimal",
                        accentColor = if (vitals.batteryTempCelsius > 38f) BlazeOrange else HyperGreen,
                        modifier = Modifier.weight(1f)
                    )
                    StatGauge(
                        icon = Icons.Default.BatteryChargingFull,
                        label = "Battery Level",
                        value = "${vitals.batteryLevelPercent}%",
                        subValue = "Power Status",
                        accentColor = HyperGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Optimization Presets
        item {
            SectionHeader(
                title = "Optimization Presets",
                subtitle = "Pre-configured FFlag profiles tailored for gameplay styles"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                presets.forEach { preset ->
                    val isSelected = activePreset.id == preset.id
                    PresetItemCard(
                        preset = preset,
                        isSelected = isSelected,
                        onSelect = {
                            triggerHaptic()
                            viewModel.selectPreset(preset)
                        }
                    )
                }
            }
        }

        // Quick Navigation Shortcuts
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateToFFlags,
                    border = BorderStroke(1.dp, CyberBorder),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("nav_to_fflag_studio")
                ) {
                    Text("FFlag Studio", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onNavigateToExport,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyanVariant),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("nav_to_export_hub")
                ) {
                    Text("Export JSON", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PresetItemCard(
    preset: OptimizationPreset,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (isSelected) NeonCyan else CyberBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable { onSelect() }
            .testTag("preset_card_${preset.id.name}"),
        color = if (isSelected) CyberCard else CyberSurfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = preset.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) NeonCyan else TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    if (preset.gameTag != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(ElectricViolet.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = preset.gameTag,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ElectricViolet,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
                Text(
                    text = preset.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    modifier = Modifier.padding(top = 2.dp)
                )
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Est. Boost: ${preset.estimatedFpsBoost}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = HyperGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "• Latency: ${preset.latencyReduction}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyanVariant,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = NeonCyan,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
