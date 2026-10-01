package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.BenchmarkScreen
import com.example.ui.screens.ConfigExportScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FFlagStudioScreen
import com.example.ui.screens.ProfilesScreen
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberDark
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloxBoostViewModel

enum class BloxScreen(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    DASHBOARD("Booster", Icons.Filled.RocketLaunch, Icons.Outlined.RocketLaunch, "nav_dashboard"),
    FFLAGS("FFlags", Icons.Filled.Tune, Icons.Outlined.Tune, "nav_fflags"),
    EXPORT("Export", Icons.Filled.Description, Icons.Outlined.Description, "nav_export"),
    BENCHMARK("Benchmark", Icons.Filled.Speed, Icons.Outlined.Speed, "nav_benchmark"),
    PROFILES("Profiles", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder, "nav_profiles")
}

class MainActivity : ComponentActivity() {
    private val viewModel: BloxBoostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                BloxBoostApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BloxBoostApp(viewModel: BloxBoostViewModel) {
    var currentScreen by remember { mutableStateOf(BloxScreen.DASHBOARD) }
    val snackbarHostState = remember { SnackbarHostState() }
    val toastMessage by viewModel.toastMessage.collectAsState()

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    // BackHandler: return to dashboard first before exiting
    BackHandler(enabled = currentScreen != BloxScreen.DASHBOARD) {
        currentScreen = BloxScreen.DASHBOARD
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDark),
        contentWindowInsets = WindowInsets.statusBars,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = CyberSurface,
                contentColor = TextPrimary,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
            ) {
                BloxScreen.values().forEach { screen ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.title,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = screen.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = NeonCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = NeonCyan
                        ),
                        modifier = Modifier.testTag(screen.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CyberDark)
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                BloxScreen.DASHBOARD -> DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToFFlags = { currentScreen = BloxScreen.FFLAGS },
                    onNavigateToExport = { currentScreen = BloxScreen.EXPORT }
                )
                BloxScreen.FFLAGS -> FFlagStudioScreen(viewModel = viewModel)
                BloxScreen.EXPORT -> ConfigExportScreen(viewModel = viewModel)
                BloxScreen.BENCHMARK -> BenchmarkScreen(viewModel = viewModel)
                BloxScreen.PROFILES -> ProfilesScreen(viewModel = viewModel)
            }
        }
    }
}
