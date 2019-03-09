package com.example.rajak.helloword

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import java.io.IOException
import android.util.Log
import com.google.android.things.contrib.driver.cap12xx.Cap12xx
import com.google.android.things.contrib.driver.cap12xx.Cap12xxInputDriver

private val TAG = MainActivity::class.java.simpleName
private val I2C_BUS = "BUS NAME"

class MainActivity : Activity() {
    private lateinit var mInputDriver: Cap12xxInputDriver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupCapacitiveTouchButtons()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyCapacitiveTouchButtons()
    }

    private fun setupCapacitiveTouchButtons() {
        // Set input key codes
        val keyCodes = intArrayOf(KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_8)

        try {
            mInputDriver = Cap12xxInputDriver(this,
                    I2C_BUS, null,
                    Cap12xx.Configuration.CAP1208,
                    keyCodes)

            // Disable repeated events
            mInputDriver.setRepeatRate(Cap12xx.REPEAT_DISABLE)
            // Block touches above 4 unique inputs
            mInputDriver.setMultitouchInputMax(4)

            mInputDriver.register()

        } catch (e: IOException) {
            Log.w(TAG, "Unable to open driver connection", e)
        }

    }

    private fun destroyCapacitiveTouchButtons() {
        mInputDriver.unregister()

        try {
            mInputDriver.close()
        } catch (e: IOException) {
            Log.w(TAG, "Unable to close touch driver", e)
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        // Handle key events from captouch inputs
        when (keyCode) {
            KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_8 -> {
                Log.d(TAG, "Captouch key released: " + event.keyCode)
                return true
            }
            else -> {
                Log.d(TAG, "Unknown key released: " + keyCode)
                return super.onKeyUp(keyCode, event)
            }
        }
    }

}
