package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.ProfileEntity
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
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloxBoostViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfilesScreen(
    viewModel: BloxBoostViewModel,
    modifier: Modifier = Modifier
) {
    val savedProfiles by viewModel.savedProfiles.collectAsState()
    val activePreset by viewModel.activePreset.collectAsState()
    val targetFps by viewModel.targetFps.collectAsState()

    var showSaveDialog by remember { mutableStateOf(false) }

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
                title = "Profiles & Game Presets",
                subtitle = "Manage custom configurations & game-specific optimizations",
                badgeText = "Profiles"
            )
        }

        // Save Current Setup Card
        item {
            CyberCard(borderColor = NeonCyan.copy(alpha = 0.5f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Save Current Configuration",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Save active ${targetFps} FPS settings into local storage for quick switching.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Button(
                        onClick = { showSaveDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(40.dp)
                            .testTag("save_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = "Save",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Saved User Profiles Section
        item {
            SectionHeader(
                title = "My Saved Profiles",
                subtitle = "${savedProfiles.size} custom configs stored locally"
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (savedProfiles.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CyberCard,
                    border = BorderStroke(1.dp, CyberBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Bookmark, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "No custom profiles saved yet.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                        )
                        Text(
                            text = "Tune your flags in FFlag Studio and save your setup above!",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        items(savedProfiles, key = { it.id }) { profile ->
            UserProfileCard(
                profile = profile,
                onLoad = { viewModel.loadProfile(profile) },
                onDelete = { viewModel.deleteProfile(profile.id) }
            )
        }

        // Curated Game-Specific Community Presets
        item {
            SectionHeader(
                title = "Popular Roblox Game Presets",
                subtitle = "Fine-tuned flag packages for popular Roblox experiences"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                GamePresetCard(
                    title = "Blox Fruits Raid Mode",
                    gameName = "Blox Fruits",
                    description = "Removes Sea Beast & Fruit particle lag, caps 120 FPS, clears sea wind effects.",
                    badge = "Raid & PvP",
                    onApply = {
                        val preset = viewModel.presets.find { it.gameTag == "Blox Fruits" }
                            ?: viewModel.presets[0]
                        viewModel.selectPreset(preset)
                    }
                )

                GamePresetCard(
                    title = "Arsenal / Rivals 144 FPS",
                    gameName = "Competitive FPS",
                    description = "Zero motion blur, instant physics tick dispatch, frame queue buffer set to 1.",
                    badge = "Shooter",
                    onApply = {
                        val preset = viewModel.presets.find { it.title.contains("Competitive", ignoreCase = true) }
                            ?: viewModel.presets[0]
                        viewModel.selectPreset(preset)
                    }
                )

                GamePresetCard(
                    title = "Brookhaven / Adopt Me Cool Phone",
                    gameName = "Roleplay & Chill",
                    description = "Low power mode, caps 60 FPS, prevents battery heat throttling during long roleplay sessions.",
                    badge = "Low Battery",
                    onApply = {
                        val preset = viewModel.presets.find { it.title.contains("Battery", ignoreCase = true) }
                            ?: viewModel.presets[0]
                        viewModel.selectPreset(preset)
                    }
                )

                GamePresetCard(
                    title = "Bedwars Low Latency",
                    gameName = "Bedwars",
                    description = "60Hz physics packet rate, compressed network payload, immediate hit registration.",
                    badge = "Hitreg",
                    onApply = {
                        val preset = viewModel.presets.find { it.title.contains("Competitive", ignoreCase = true) }
                            ?: viewModel.presets[0]
                        viewModel.selectPreset(preset)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showSaveDialog) {
        SaveProfileDialog(
            defaultName = "My Config (${targetFps} FPS)",
            onDismiss = { showSaveDialog = false },
            onSave = { name, desc ->
                viewModel.saveCurrentProfile(name, desc)
                showSaveDialog = false
            }
        )
    }
}

@Composable
fun UserProfileCard(
    profile: ProfileEntity,
    onLoad: () -> Unit,
    onDelete: () -> Unit
) {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val dateStr = formatter.format(Date(profile.updatedAt))

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = CyberCard,
        border = BorderStroke(1.dp, CyberBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("user_profile_${profile.id}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonCyan.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${profile.targetFps} FPS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
                Text(
                    text = profile.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = "Saved $dateStr",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = CrimsonAlert,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Button(
                    onClick = onLoad,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HyperGreen),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("Load", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun GamePresetCard(
    title: String,
    gameName: String,
    description: String,
    badge: String,
    onApply: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = CyberCard,
        border = BorderStroke(1.dp, CyberBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SportsEsports,
                        contentDescription = null,
                        tint = ElectricViolet,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(ElectricViolet.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ElectricViolet,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onApply,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                modifier = Modifier.height(34.dp)
            ) {
                Text("Apply", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun SaveProfileDialog(
    defaultName: String,
    onDismiss: () -> Unit,
    onSave: (name: String, description: String) -> Unit
) {
    var name by remember { mutableStateOf(defaultName) }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Save Profile",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Profile Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    placeholder = { Text("e.g. For Bedwars tournaments") },
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, description) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                enabled = name.isNotBlank()
            ) {
                Text("Save", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        },
        containerColor = CyberCard
    )
}
