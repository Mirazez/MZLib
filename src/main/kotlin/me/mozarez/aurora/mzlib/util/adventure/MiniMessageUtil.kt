package me.mozarez.aurora.mzlib.util.adventure

import me.mozarez.aurora.mzlib.util.adventure.MiniMessageUtil.removeDecorations
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

object MiniMessageUtil {

    fun Component.toLegacy() : String {
        return LegacyComponentSerializer.legacyAmpersand().serialize(this)
    }

    fun Component.toPlain() : String {
        return PlainComponentSerializer.plain().serialize(this);
    }

    fun Component.removeDecorations(vararg dec: TextDecoration) : Component {
        var cmp = this
        for (decoration in dec) {
            cmp = cmp.decoration(decoration, TextDecoration.State.FALSE)
        }
        return cmp
    }

    fun String.toComponentRemoveDecoration(vararg dec: TextDecoration) : Component {
        var cmp = this.toComponent()
        for (decoration in dec) {
            cmp = cmp.decoration(decoration, TextDecoration.State.FALSE)
        }
        return cmp
    }

    fun String.toRawComponent(): Component {
        return Component.text(this)
    }

    fun String.logInfo(instance: JavaPlugin) {
        instance.componentLogger.info(this.toComponent())
    }

    fun String.logWarn(instance: JavaPlugin) {
        instance.componentLogger.warn(this.toComponent())
    }

    fun String.logErr(instance: JavaPlugin) {
        instance.componentLogger.error(this.toComponent())
    }

    fun Component.logInfo(instance: JavaPlugin) {
        instance.componentLogger.info(this)
    }

    fun Component.logWarn(instance: JavaPlugin) {
        instance.componentLogger.warn(this)
    }

    fun Component.logErr(instance: JavaPlugin) {
        instance.componentLogger.error(this)
    }


    fun Component.replace(match: String, replacement: String): Component {
        return this.replaceText(TextReplacementConfig.builder().match(match).replacement(replacement).build())
    }

    fun Component.replace(match: String, replacement: ComponentLike): Component {
        return this.replaceText(TextReplacementConfig.builder().match(match).replacement(replacement).build())
    }

    fun String.toComponent(): Component {
        return MiniMessage.miniMessage().deserialize(this);
    }

    fun String.toComponent(args: Map<String,ComponentLike>) : Component {
        return this.toComponent().withMap(args)
    }

    fun String.toComponentStr(args: Map<String, String>) : Component {
        return this.withMapStr(args).toComponent()
    }

    fun String.withMap(args: Map<String, ComponentLike>) : String {
        var s = this
        for ((key, value) in args) {
            s = s.replace("%$key%", value.asComponent().toLegacy())
        }
        return s
    }

    fun String.withMapStr(args: Map<String, String>) : String {
        var s = this
        for ((key, value) in args) {
            s = s.replace("%$key%", value)
        }
        return s
    }

    fun Component.withMap(args: Map<String, ComponentLike>): Component {
        var cmp = this
        for (id in args.keys) {
            cmp = cmp.replaceText { t ->
                t.match("%$id%").replacement(args[id])
            }
        }
        return cmp
    }

    fun FileConfiguration.getComponentStr(path: String, args: Map<String,String>) : Component {
        return (this as ConfigurationSection).getComponentStr(path,args)
    }

    fun FileConfiguration.getComponent(path: String, args: Map<String,ComponentLike>) : Component {
        return (this as ConfigurationSection).getComponent(path, args)
    }

    fun ConfigurationSection.getComponentStr(path: String, args: Map<String,String>) : Component {
        var str = this.getString(path)!!
        for ((k,v) in args) {
            str = str.replace("%$k%", v)
        }
        return str.toComponent()
    }

    fun ConfigurationSection.getComponent(path: String, args: Map<String,ComponentLike>) : Component {
        val list = this.getConfigurationSection("variables")?.getKeys(false) ?: listOf()
        val args = HashMap(args)
        for (str in list) {
            args["var:$str"] = (this.getString("variables.$str") ?: "Ошибка").toComponent()
        }
        return this.getString(path)!!.toComponent(args)
    }

    fun List<String>.toComponentList(map: Map<String,ComponentLike>? = emptyMap()): ArrayList<Component> {
        val list = ArrayList<Component>()
        for (s in this) {
            list.add(s.toComponent(map!!).removeDecorations(TextDecoration.ITALIC))
        }
        return list
    }

    fun List<Component>.withMap(map: Map<String,ComponentLike>) : List<Component> {
        val list = ArrayList<Component>()
        for (s in this) {
            list.add(s.withMap(map).removeDecorations(TextDecoration.ITALIC))
        }
        return list
    }

    fun List<String>.withMapStr(map: Map<String, String>) : List<String> {
        val list = ArrayList<String>()
        for (s in this) {
            list.add(s.withMapStr(map))
        }
        return list
    }

}