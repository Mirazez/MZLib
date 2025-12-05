package me.mozarez.aurora.mzlib.util.exp4j

import net.objecthunter.exp4j.operator.Operator

object Exp4jOperators {
    val operators = ArrayList<Operator>()

    fun init() {
        operators.add(object : Operator("==", 2, false, 1) {
            override fun apply(vararg args: Double): Double {
                return when (args[0] == args[1]) {
                    true -> 1.0
                    else -> 0.0
                }
            }
        })
        operators.add(object : Operator("=", 2, false, 1) {
            override fun apply(vararg args: Double): Double {
                return when (args[0] == args[1]) {
                    true -> 1.0
                    else -> 0.0
                }
            }
        })
        operators.add(object : Operator("<", 2, false, 1) {
            override fun apply(vararg args: Double): Double {
                return when (args[0] < args[1]) {
                    true -> 1.0
                    else -> 0.0
                }
            }
        })
        operators.add(object : Operator("<=", 2, false, 1) {
            override fun apply(vararg args: Double): Double {
                return when (args[0] <= args[1]) {
                    true -> 1.0
                    else -> 0.0
                }
            }
        })
        operators.add(object : Operator(">", 2, false, 1) {
            override fun apply(vararg args: Double): Double {
                return when (args[0] > args[1]) {
                    true -> 1.0
                    else -> 0.0
                }
            }
        })
        operators.add(object : Operator(">=", 2, false, 1) {
            override fun apply(vararg args: Double): Double {
                return when (args[0] >= args[1]) {
                    true -> 1.0
                    else -> 0.0
                }
            }
        })
    }
}