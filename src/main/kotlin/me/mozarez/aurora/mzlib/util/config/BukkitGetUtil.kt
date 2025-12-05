package me.mozarez.aurora.mzlib.util.config

import com.destroystokyo.paper.ParticleBuilder
import me.mozarez.aurora.mzlib.util.adventure.MiniMessageUtil.getComponent
import me.mozarez.aurora.mzlib.util.adventure.MiniMessageUtil.getComponentStr
import me.mozarez.aurora.mzlib.util.adventure.MiniMessageUtil.toComponent
import me.mozarez.aurora.mzlib.util.adventure.TitleBuilder
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.title.Title
import org.bukkit.Particle
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration

object BukkitGetUtil {

    fun FileConfiguration.getParticle(path: String) : ParticleBuilder {
        return (this as ConfigurationSection).getParticle(path)
    }

    fun ConfigurationSection.getParticle(path: String) : ParticleBuilder {
        val sp = this.getConfigurationSection(path)!!
        val count = sp.getInt("count")
        val r = sp.getInt("color.r")
        val g = sp.getInt("color.g")
        val b = sp.getInt("color.b")
        val offsetx = sp.getDouble("offset.x")
        val offsety = sp.getDouble("offset.y")
        val offsetz = sp.getDouble("offset.z")
        return Particle.REDSTONE.builder().color(r,g,b).offset(offsetx,offsety,offsetz).count(count)
    }

    fun FileConfiguration.getTitle(path: String) : Title {
        return (this as ConfigurationSection).getTitle(path, emptyMap())
    }

    fun ConfigurationSection.getTitle(path: String, map: Map<String, ComponentLike>) : Title {
        val section = this.getConfigurationSection(path)
        val title = TitleBuilder()
        title.title((section?.getComponent("title", map) ?: "Ошибка".toComponent()))
        title.subtitle((section?.getComponent("subtitle", map) ?: "Обратитесь к администратору ($path)".toComponent()))
        return title.build()
    }

    fun FileConfiguration.getTitleStr(path: String) : Title {
        return (this as ConfigurationSection).getTitle(path, emptyMap())
    }

    fun ConfigurationSection.getTitleStr(path: String, map: Map<String, String>) : Title {
        val section = this.getConfigurationSection(path)
        val title = TitleBuilder()
        title.title((section?.getComponentStr("title", map) ?: "Ошибка".toComponent()))
        title.subtitle((section?.getComponentStr("subtitle", map) ?: "Обратитесь к администратору ($path)".toComponent()))
        return title.build()
    }

}