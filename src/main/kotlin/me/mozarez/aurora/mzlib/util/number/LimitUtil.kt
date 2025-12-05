package me.mozarez.aurora.mzlib.util.number

import kotlin.math.max
import kotlin.math.min

object LimitUtil {

    fun Number.limit(min: Number, max: Number): Number {
        return min(max.toDouble(), max(this.toDouble(), min.toDouble()))
    }

}