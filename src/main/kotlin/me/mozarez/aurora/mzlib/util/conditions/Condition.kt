package me.mozarez.aurora.mzlib.util.conditions

import org.bukkit.entity.Player
import java.util.function.BiFunction

class Condition(private val runnable: BiFunction<Player?, String, Boolean>) {

    fun check(line: String, player: Player? = null) : Boolean {
        return runnable.apply(player, line)
    }

}