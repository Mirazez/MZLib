package me.mozarez.aurora.mzlib.util.placeholderManager

import org.bukkit.entity.Player

object PlaceholderManager {
    val map = HashMap<String, Placeholder>()

    fun parse(string: String, player: Player) : String {
        var outStr = string
        for (id in map.keys) {
            if (outStr.startsWith(id)) {
                outStr = outStr.replace(id, map[id]!!.get(player))
            }
        }
        return outStr
    }

    fun getPlaceholder(string: String, player: Player) : Placeholder? {
        for (id in map.keys) {
            if (string.startsWith(id)) {
                return map[id]!!
            }
        }
        return null
    }

    fun Placeholder.register() {
        map[this.id] = this
    }
}