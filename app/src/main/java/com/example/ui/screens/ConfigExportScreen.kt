package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberCard
import com.example.ui.components.SectionHeader
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
import org.json.JSONObject

@Composable
fun ConfigExportScreen(
    viewModel: BloxBoostViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val jsonString by viewModel.generatedJson.collectAsState()
    val activePreset by viewModel.activePreset.collectAsState()

    var copiedToClipboard by remember { mutableStateOf(false) }
    var pathCopied by remember { mutableStateOf(false) }
    var syntaxVerified by remember { mutableStateOf<Boolean?>(null) }

    val androidRobloxPath = "Android/data/com.roblox.client/files/ClientSettings/ClientAppSettings.json"

    fun copyText(text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    fun shareConfig() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, jsonString)
            putExtra(Intent.EXTRA_TITLE, "ClientAppSettings.json - BloxBoost")
            type = "application/json"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share ClientAppSettings.json")
        context.startActivity(shareIntent)
    }

    fun verifySyntax() {
        syntaxVerified = try {
            JSONObject(jsonString)
            true
        } catch (_: Exception) {
            false
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
            SectionHeader(
                title = "ClientAppSettings.json Generator",
                subtitle = "Active configuration based on ${activePreset.title}",
                badgeText = "Roblox Engine"
            )
        }

        // Live JSON Code Preview
        item {
            CyberCard(borderColor = NeonCyan.copy(alpha = 0.5f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = "Code",
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ClientAppSettings.json",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary
                            )
                        )
                    }

                    if (syntaxVerified == true) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = "Valid", tint = HyperGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Valid JSON", color = HyperGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Monospace scrollable container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF070B12))
                        .border(1.dp, CyberBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    val hScroll = rememberScrollState()
                    val vScroll = rememberScrollState()
                    Text(
                        text = jsonString.ifEmpty { "{}" },
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.5.sp,
                            lineHeight = 16.sp,
                            color = NeonCyan
                        ),
                        modifier = Modifier
                            .horizontalScroll(hScroll)
                            .testTag("json_code_preview")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Actions row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            copyText(jsonString, "ClientAppSettings.json")
                            copiedToClipboard = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("copy_json_button")
                    ) {
                        Icon(
                            imageVector = if (copiedToClipboard) Icons.Default.Check else Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (copiedToClipboard) "Copied!" else "Copy JSON",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    OutlinedButton(
                        onClick = { shareConfig() },
                        border = BorderStroke(1.dp, CyberBorder),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        modifier = Modifier
                            .weight(0.7f)
                            .height(44.dp)
                            .testTag("share_json_button")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Share")
                    }

                    OutlinedButton(
                        onClick = { verifySyntax() },
                        border = BorderStroke(1.dp, CyberBorder),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = HyperGreen),
                        modifier = Modifier
                            .weight(0.7f)
                            .height(44.dp)
                    ) {
                        Text("Validate")
                    }
                }
            }
        }

        // Android Installation Tutorial
        item {
            SectionHeader(
                title = "Android Mobile Installation Guide",
                subtitle = "Follow these steps to apply FFlags to your Android Roblox client",
                badgeText = "Mobile Guide"
            )
            Spacer(modifier = Modifier.height(10.dp))

            CyberCard {
                // Path Banner
                Text(
                    text = "TARGET FILE PATH:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CyberSurfaceVariant,
                    border = BorderStroke(1.dp, CyberBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 12.dp)
                        .clickable {
                            copyText(androidRobloxPath, "Roblox ClientSettings Path")
                            pathCopied = true
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = androidRobloxPath,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = NeonCyan,
                                fontSize = 11.sp
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (pathCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                            contentDescription = "Copy Path",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Steps List
                TutorialStep(
                    number = "1",
                    title = "Open File Explorer (ZArchiver or FV Explorer)",
                    description = "Due to Android 11+ Scoped Storage restrictions, use ZArchiver, MiXplorer, or FV File Explorer to access Android/data."
                )
                TutorialStep(
                    number = "2",
                    title = "Locate Roblox Folder",
                    description = "Navigate to: /Android/data/com.roblox.client/files/"
                )
                TutorialStep(
                    number = "3",
                    title = "Create 'ClientSettings' Folder",
                    description = "If it does not exist already, create a new folder named exactly 'ClientSettings' (case sensitive)."
                )
                TutorialStep(
                    number = "4",
                    title = "Save 'ClientAppSettings.json'",
                    description = "Create a new file named 'ClientAppSettings.json' inside ClientSettings and paste the copied JSON content."
                )
                TutorialStep(
                    number = "5",
                    title = "Launch Roblox Client",
                    description = "Restart Roblox. Your custom FPS target, shadow disable, low-FX shaders, and latency flags will load immediately!"
                )
            }
        }

        // PC / Bloxstrap Cross-Play Guide
        item {
            CyberCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = "PC Guide", tint = ElectricViolet)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PC / Bloxstrap Cross-Play",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Playing on Windows PC as well? You can paste this exact same JSON into Bloxstrap -> Fast Flags -> Fast Flag Editor -> Import JSON, or into '%localappdata%\\Roblox\\Versions\\version-xxx\\ClientSettings\\ClientAppSettings.json'.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun TutorialStep(
    number: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(NeonCyan)
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    fontSize = 13.sp
                )
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 12.sp
                ),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
