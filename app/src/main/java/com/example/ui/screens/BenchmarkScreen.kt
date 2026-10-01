package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.BenchmarkEntity
import com.example.data.model.PingTarget
import com.example.ui.components.CyberCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.BlazeOrange
import com.example.ui.theme.CrimsonAlert
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BenchmarkScreen(
    viewModel: BloxBoostViewModel,
    modifier: Modifier = Modifier
) {
    val pingTargets by viewModel.pingTargets.collectAsState()
    val isTestingPing by viewModel.isTestingPing.collectAsState()
    val isBenchmarking by viewModel.benchmarkRunning.collectAsState()
    val benchmarkProgress by viewModel.benchmarkProgress.collectAsState()
    val currentFps by viewModel.currentBenchmarkFps.collectAsState()
    val fpsSamples by viewModel.recentFpsSamples.collectAsState()
    val history by viewModel.benchmarkHistory.collectAsState()
    val activePreset by viewModel.activePreset.collectAsState()
    val targetFps by viewModel.targetFps.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            SectionHeader(
                title = "Benchmark & Latency Arena",
                subtitle = "Hardware stability stress test & Roblox data center ping check",
                badgeText = "Diagnostic"
            )
        }

        // Roblox Server Ping Card
        item {
            CyberCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Public, contentDescription = "Ping", tint = NeonCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Roblox Server Ping",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Latency to regional matchmaking clusters",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.runPingTest() },
                        enabled = !isTestingPing,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("run_ping_test_button")
                    ) {
                        if (isTestingPing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.Black,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Test Ping", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Ping Grid
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    pingTargets.forEach { target ->
                        PingTargetRow(target)
                    }
                }
            }
        }

        // FPS Stability Benchmark Test
        item {
            CyberCard(borderColor = if (isBenchmarking) NeonCyan else CyberBorder) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "FPS & Frame Pacing Test",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Testing preset: ${activePreset.title}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    Button(
                        onClick = { viewModel.runFpsBenchmark() },
                        enabled = !isBenchmarking,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("start_benchmark_button")
                    ) {
                        if (isBenchmarking) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Run", tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Run Stress Test", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Live Metrics During Test
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Current FPS", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        Text(
                            text = if (isBenchmarking) "${currentFps.toInt()}" else "$targetFps",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = NeonCyan
                            )
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Frame Pacing", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        Text(
                            text = if (isBenchmarking) "Testing..." else "Ready",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = HyperGreen
                            )
                        )
                    }
                }

                if (isBenchmarking) {
                    LinearProgressIndicator(
                        progress = { benchmarkProgress },
                        color = NeonCyan,
                        trackColor = CyberBorder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                } else {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Real-time Canvas FPS Line Chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF090E17))
                        .border(1.dp, CyberBorder, RoundedCornerShape(10.dp))
                        .padding(8.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height

                        // Grid lines
                        drawLine(
                            color = Color(0xFF1F293D),
                            start = Offset(0f, height * 0.25f),
                            end = Offset(width, height * 0.25f),
                            strokeWidth = 1.dp.toPx()
                        )
                        drawLine(
                            color = Color(0xFF1F293D),
                            start = Offset(0f, height * 0.75f),
                            end = Offset(width, height * 0.75f),
                            strokeWidth = 1.dp.toPx()
                        )

                        if (fpsSamples.size > 1) {
                            val maxVal = 144f
                            val minVal = 30f
                            val range = (maxVal - minVal).coerceAtLeast(1f)
                            val stepX = width / (fpsSamples.size - 1)

                            val path = Path()
                            fpsSamples.forEachIndexed { index, fps ->
                                val x = index * stepX
                                val normalized = (fps - minVal) / range
                                val y = height - (normalized * height).coerceIn(0f, height)
                                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                            }

                            drawPath(
                                path = path,
                                color = NeonCyan,
                                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                    }
                }
            }
        }

        // Benchmark Score History
        item {
            SectionHeader(
                title = "Benchmark History",
                subtitle = "Saved stability test results (${history.size} runs)"
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (history.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CyberCard,
                    border = BorderStroke(1.dp, CyberBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Timeline, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No benchmarks run yet.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                        )
                        Text(
                            text = "Tap 'Run Stress Test' above to measure FPS score.",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        items(history) { record ->
            BenchmarkHistoryItem(record)
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun PingTargetRow(target: PingTarget) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = CyberSurfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, CyberBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = target.region,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = target.location,
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                )
            }

            val latencyColor = when {
                target.latencyMs == null -> TextSecondary
                target.latencyMs!! < 60 -> HyperGreen
                target.latencyMs!! < 120 -> BlazeOrange
                else -> CrimsonAlert
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(latencyColor.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (target.latencyMs != null) "${target.latencyMs} ms" else "Not tested",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = latencyColor,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun BenchmarkHistoryItem(record: BenchmarkEntity) {
    val formatter = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val dateStr = formatter.format(Date(record.timestamp))

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CyberCard,
        border = BorderStroke(1.dp, CyberBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = record.presetName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "$dateStr • 1% Low: ${record.onePercentLow.toInt()} FPS",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${record.score} PTS",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = NeonCyan
                    )
                )
                Text(
                    text = "Avg ${record.averageFps.toInt()} FPS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = HyperGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}
