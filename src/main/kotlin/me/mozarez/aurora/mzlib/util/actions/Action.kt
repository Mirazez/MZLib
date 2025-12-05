package me.mozarez.aurora.mzlib.util.actions

import org.bukkit.entity.Player
import java.util.function.BiConsumer

class Action(private val runnable: BiConsumer<Player?, String>) {

    fun run(player: Player?, line: String) {
        runnable.accept(player, line)
    }

}