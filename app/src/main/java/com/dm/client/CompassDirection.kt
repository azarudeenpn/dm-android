package com.dm.client

import android.widget.Toast
import kotlin.math.atan2

class CompassDirection {

    fun calc(a1: Float, a2: Float, b1: Float, b2: Float): Double {
        var theta = atan2(b1 - a1, a2 - b2)
        if (theta < 0.0) {
            theta = (theta + 6.2831853071795865).toFloat()
        }
        var result = (57.2957795130823209 * theta) - 90
        if (result < 0) {
            result += 360f
        }
        return result
    }


}