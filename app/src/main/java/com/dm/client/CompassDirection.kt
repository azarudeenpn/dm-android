package com.dm.client

import kotlin.math.PI
import kotlin.math.atan2

class CompassDirection {

    fun calc(a1: Float, a2: Float, b1: Float, b2: Float): Double {
        var theta = atan2(b1 - a1, a2 - b2)
        if (theta < 0.0) {
            theta = (theta + 2 * PI).toFloat()
        }
        var result = (Math.toDegrees(theta.toDouble())) - 90
        if (result < 0) {
            result += 360f
        }
        return result
    }


}