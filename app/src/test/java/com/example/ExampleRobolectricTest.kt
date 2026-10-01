package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.datasource.DefaultFFlags
import com.example.data.model.FFlagType
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read app name from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("BloxBoost", appName)
  }

  @Test
  fun `default flags include target fps flag`() {
    val flags = DefaultFFlags.getDefaultFlags()
    val fpsFlag = flags.find { it.key == "DFIntTaskSchedulerTargetFps" }
    assertNotNull(fpsFlag)
    assertEquals(FFlagType.INTEGER, fpsFlag?.type)
  }

  @Test
  fun `presets contain potato ultra preset`() {
    val presets = DefaultFFlags.getPresets()
    assertTrue(presets.isNotEmpty())
    val potato = presets.find { it.title.contains("Potato", ignoreCase = true) }
    assertNotNull(potato)
    assertEquals(240, potato?.targetFps)
  }
}
