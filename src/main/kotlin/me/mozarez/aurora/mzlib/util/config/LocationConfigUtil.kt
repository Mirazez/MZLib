package me.mozarez.aurora.mzlib.util.config

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection

object LocationConfigUtil {

    fun ConfigurationSection.buildLocation(world: World) : Location {
        return Location(
            world,
            this.getDouble("x"),
            this.getDouble("y"),
            this.getDouble("z"),
            this.getDouble("yaw").toFloat(),
            this.getDouble("pitch").toFloat()
        )
    }

    fun String.buildLocation(world: World) : Location {
        val split = this.split(";")
        return Location(
            world,
            split[0].toDouble(),
            split[1].toDouble(),
            split[2].toDouble(),
            split[3].toFloat(),
            split[4].toFloat()
        )
    }

}