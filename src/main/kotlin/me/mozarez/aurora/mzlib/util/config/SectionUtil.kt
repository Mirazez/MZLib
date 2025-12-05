package me.mozarez.aurora.mzlib.util.config

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration

object SectionUtil {

    fun FileConfiguration.getKeys(path: String, withPath: Boolean) : Set<String> {
        return this.getKeys(path).map { t -> "$path.$t" }.toSet()
    }

    fun FileConfiguration.getKeys(path: String) : Set<String> {
        return this.getConfigurationSection(path)?.getKeys(false) ?: setOf()
    }

}