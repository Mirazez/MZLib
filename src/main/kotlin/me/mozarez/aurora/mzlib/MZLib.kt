package me.mozarez.aurora.mzlib

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.greedyStringArgument
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import me.mozarez.aurora.mzlib.util.actions.ActionManager
import me.mozarez.aurora.mzlib.util.adventure.MiniMessageUtil.toComponent
import me.mozarez.aurora.mzlib.util.conditions.ConditionManager
import me.mozarez.aurora.mzlib.util.exp4j.Exp4jOperators
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.InputStreamReader

class MZLib : JavaPlugin() {

    companion object {
        lateinit var instance: JavaPlugin
        lateinit var config: FileConfiguration
        lateinit var cfgfile: File
    }

    override fun onLoad() {
        load(this)
    }

    override fun onEnable() {
        init()
    }

    override fun onDisable() {
        drop()
    }

    fun load(instanc: JavaPlugin) {
        instance = instanc
        CommandAPI.onLoad(CommandAPIBukkitConfig(instance).verboseOutput(false))
        Exp4jOperators.init()
    }

    fun init() {
        CommandAPI.onEnable()
        if (instance is MZLib) {
            instance.dataFolder.mkdirs()
            cfgfile = File(instance.dataFolder, "config.yml")
        } else {
            File(instance.dataFolder, "MZLib").mkdirs()
            cfgfile = File(instance.dataFolder, "MZLib/config.yml")
        }
        saveDefaultConfig()
        registerTree()
    }

    fun registerTree() {
        commandTree("mzlib") {
            withPermission("mzlib.command")
            literalArgument("conditions") {
                withPermission("mzlib.command.conditions")
                literalArgument("test") {
                    withPermission("mzlib.command.conditions.test")
                    greedyStringArgument("condition") {
                        replaceSuggestions(ArgumentSuggestions.strings { _ ->
                            ConditionManager.conditions.keys.map { "[$it] " }.toTypedArray()
                        })
                        playerExecutor { sender, args ->
                            val line = args["condition"] as String
                            val result = when (ConditionManager.invoke(line, sender)) {
                                true -> "<green><b>ИСТИННО"
                                false -> "<red><b>ЛОЖНО"
                            }
                            sender.sendMessage("<blue>> <white>Результат: $result".toComponent())
                        }
                    }
                }
            }
            literalArgument("actions") {
                withPermission("mzlib.command.actions")
                literalArgument("test") {
                    withPermission("mzlib.command.actions.test")
                    greedyStringArgument("action") {
                        replaceSuggestions(ArgumentSuggestions.strings { _ ->
                            ActionManager.actions.keys.map { "[$it] " }.toTypedArray()
                        })
                        playerExecutor { sender, args ->
                            val line = args["action"] as String
                            ActionManager.invoke(line, sender)
                        }
                    }
                }
            }
        }
    }

    fun drop() {
        CommandAPI.onDisable()
    }

    override fun saveDefaultConfig() {
        if (!cfgfile.exists()) {
            val reader = InputStreamReader(this.getResource("config.yml")!!)
            MZLib.config = YamlConfiguration.loadConfiguration(reader)
            MZLib.config.save(cfgfile)
        } else {
            MZLib.config = YamlConfiguration.loadConfiguration(cfgfile)
        }
    }

    override fun reloadConfig() {
        MZLib.config = YamlConfiguration.loadConfiguration(cfgfile)
    }

    override fun getConfig(): FileConfiguration {
        return MZLib.config
    }
}
