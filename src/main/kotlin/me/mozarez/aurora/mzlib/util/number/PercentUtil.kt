package me.mozarez.aurora.mzlib.util.number

import kotlin.random.Random

object PercentUtil {
    fun Number.percentOf(percent: Number): Double {
        return this.toDouble() * (percent.toDouble() / 100)
    }

    fun Number.chanceOf(num: Number) : Boolean {
        if (Random.nextDouble(num.toDouble()) <= this.toDouble()) {
            return true
        }
        return false
    }
}