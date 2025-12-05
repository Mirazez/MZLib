package me.mozarez.aurora.mzlib.util.placeholderManager

import org.bukkit.entity.Player

open class Placeholder(val id: String) {
    open fun set(obj: Any) {

    }

    open fun get(player: Player) : String {
        return ""
    }
}