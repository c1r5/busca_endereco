package com.example.qualmeuendereco

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.qualmeuendereco.viewmodels.MainViewModel
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun getStates() {
        MainViewModel().getStates(appContext)
    }
}