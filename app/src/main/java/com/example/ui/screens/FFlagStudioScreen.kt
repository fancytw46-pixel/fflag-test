package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FFlag
import com.example.data.model.FFlagCategory
import com.example.data.model.FFlagType
import com.example.ui.components.ImpactBadge
import com.example.ui.components.SectionHeader
import com.example.ui.components.getCategoryIcon
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

@Composable
fun FFlagStudioScreen(
    viewModel: BloxBoostViewModel,
    modifier: Modifier = Modifier
) {
    val flags by viewModel.filteredFlags.collectAsState()
    val allFlags by viewModel.flags.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberDark)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Search 20+ FFlags (e.g. shadow, fps, grass)...", color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = NeonCyan) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondary)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = CyberBorder,
                focusedContainerColor = CyberCard,
                unfocusedContainerColor = CyberCard,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("fflag_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(FFlagCategory.values()) { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) NeonCyan else CyberSurfaceVariant,
                    border = BorderStroke(1.dp, if (isSelected) NeonCyan else CyberBorder),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { viewModel.setSelectedCategory(cat) }
                        .testTag("cat_chip_${cat.name}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = getCategoryIcon(cat),
                            contentDescription = cat.displayName,
                            tint = if (isSelected) Color.Black else TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = cat.displayName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.Black else TextPrimary
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Header with count & action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${flags.size} of ${allFlags.size} Fast Flags",
                style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { showResetDialog = true },
                    border = BorderStroke(1.dp, CyberBorder),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Defaults", fontSize = 11.sp)
                }

                Button(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("add_custom_fflag_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.Black, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Flag", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // FFlag List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(flags, key = { it.key }) { flag ->
                FFlagItemCard(
                    flag = flag,
                    onValueChange = { newVal -> viewModel.updateFlagValue(flag.key, newVal) },
                    onToggleEnabled = { enabled -> viewModel.toggleFlagEnabled(flag.key, enabled) },
                    onDelete = { viewModel.removeCustomFlag(flag.key) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Add Custom FFlag Dialog
    if (showAddDialog) {
        AddCustomFFlagDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { key, value, type, cat ->
                viewModel.addCustomFlag(key, value, type, cat)
                showAddDialog = false
            }
        )
    }

    // Reset Confirm Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset to Factory Defaults?", color = TextPrimary) },
            text = {
                Text(
                    "This will restore all Fast Flags to their official Roblox client default values.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetFlagsToDefault()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonAlert)
                ) {
                    Text("Reset All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CyberCard
        )
    }
}

@Composable
fun FFlagItemCard(
    flag: FFlag,
    onValueChange: (String) -> Unit,
    onToggleEnabled: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (flag.isEnabled) CyberBorder else CyberBorder.copy(alpha = 0.4f),
                RoundedCornerShape(14.dp)
            ),
        color = CyberCard
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Name + Toggle Switch
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = flag.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (flag.isEnabled) TextPrimary else TextSecondary
                        )
                    )
                    Text(
                        text = flag.key,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = NeonCyan
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (flag.isCustom) {
                        IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CrimsonAlert, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Switch(
                        checked = flag.isEnabled,
                        onCheckedChange = onToggleEnabled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = NeonCyan,
                            uncheckedThumbColor = TextSecondary,
                            uncheckedTrackColor = CyberSurfaceVariant
                        ),
                        modifier = Modifier.testTag("switch_${flag.key}")
                    )
                }
            }

            // Description
            Text(
                text = flag.description,
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                modifier = Modifier.padding(vertical = 6.dp)
            )

            // Badges row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                ImpactBadge(flag.impact)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CyberSurfaceVariant)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = flag.category.displayName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontSize = 9.sp
                        )
                    )
                }
            }

            // Interactive Editor based on Type
            AnimatedVisibility(visible = flag.isEnabled) {
                when (flag.type) {
                    FFlagType.BOOLEAN -> {
                        val isTrue = flag.currentValue.equals("true", ignoreCase = true)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(CyberSurfaceVariant.copy(alpha = 0.6f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Value: ${if (isTrue) "True (Enabled)" else "False (Disabled)"}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = if (isTrue) HyperGreen else BlazeOrange,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { onValueChange("true") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isTrue) HyperGreen else CyberBorder
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text("True", fontSize = 10.sp, color = if (isTrue) Color.Black else TextSecondary)
                                }
                                Button(
                                    onClick = { onValueChange("false") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (!isTrue) BlazeOrange else CyberBorder
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text("False", fontSize = 10.sp, color = if (!isTrue) Color.Black else TextSecondary)
                                }
                            }
                        }
                    }

                    FFlagType.INTEGER -> {
                        val min = flag.minInt ?: 0
                        val max = flag.maxInt ?: 240
                        val current = flag.currentValue.toIntOrNull() ?: min

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(CyberSurfaceVariant.copy(alpha = 0.6f))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Current Value: $current",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = NeonCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Range: $min - $max",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                            }
                            Slider(
                                value = current.toFloat().coerceIn(min.toFloat(), max.toFloat()),
                                onValueChange = {
                                    val step = flag.intStep ?: 1
                                    val rounded = ((it - min) / step).toInt() * step + min
                                    onValueChange(rounded.toString())
                                },
                                valueRange = min.toFloat()..max.toFloat(),
                                colors = SliderDefaults.colors(
                                    thumbColor = NeonCyan,
                                    activeTrackColor = NeonCyan,
                                    inactiveTrackColor = CyberBorder
                                ),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    FFlagType.STRING -> {
                        OutlinedTextField(
                            value = flag.currentValue,
                            onValueChange = onValueChange,
                            label = { Text("Flag String Value") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = CyberBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddCustomFFlagDialog(
    onDismiss: () -> Unit,
    onAdd: (key: String, value: String, type: FFlagType, category: FFlagCategory) -> Unit
) {
    var key by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("true") }
    var selectedType by remember { mutableStateOf(FFlagType.BOOLEAN) }
    var selectedCat by remember { mutableStateOf(FFlagCategory.PERFORMANCE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Custom FFlag",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Enter any Roblox Fast Flag (e.g. FFlag..., DFInt..., FInt...).",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )

                OutlinedTextField(
                    value = key,
                    onValueChange = { key = it },
                    label = { Text("FFlag Key") },
                    placeholder = { Text("e.g. DFIntCustomFpsLimit") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Data Type:", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FFlagType.values().forEach { t ->
                        val isSel = selectedType == t
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) NeonCyan else CyberSurfaceVariant,
                            modifier = Modifier
                                .clickable {
                                    selectedType = t
                                    value = when (t) {
                                        FFlagType.BOOLEAN -> "true"
                                        FFlagType.INTEGER -> "0"
                                        FFlagType.STRING -> ""
                                    }
                                }
                        ) {
                            Text(
                                text = t.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) Color.Black else TextPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Value") },
                    keyboardOptions = if (selectedType == FFlagType.INTEGER) {
                        KeyboardOptions(keyboardType = KeyboardType.Number)
                    } else KeyboardOptions.Default,
                    singleLine = true,
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
                onClick = { onAdd(key, value, selectedType, selectedCat) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                enabled = key.isNotBlank()
            ) {
                Text("Add Flag", color = Color.Black, fontWeight = FontWeight.Bold)
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
