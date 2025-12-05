package me.mozarez.aurora.mzlib.util.actions

import me.clip.placeholderapi.PlaceholderAPI
import me.mozarez.aurora.mzlib.MZLib.Companion.instance
import me.mozarez.aurora.mzlib.util.adventure.AdventureSoundUtil.playAdvSound
import me.mozarez.aurora.mzlib.util.adventure.MiniMessageUtil.logErr
import me.mozarez.aurora.mzlib.util.adventure.MiniMessageUtil.toComponent
import me.mozarez.aurora.mzlib.util.adventure.MiniMessageUtil.withMapStr
import me.mozarez.aurora.mzlib.util.items.ItemParser
import me.mozarez.aurora.mzlib.util.items.ItemParser.takeItem
import me.mozarez.aurora.mzlib.util.items.ItemParser.takeMMOItem
import me.mozarez.aurora.mzlib.util.other.ObjectUtil.toIntOr
import net.Indyuce.mmoitems.MMOItems
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.node.Node
import net.objecthunter.exp4j.ExpressionBuilder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.function.Consumer

object ActionManager {

    val regex = Regex("\\[([\\w-]+)\\]")

    val exprRegex = Regex("\\$\\{([A-zА-я0-9*^%]+)}")

    val regexAdditional = Regex("<(delay|chance):(\\d+)>")

    val actions = HashMap<String, Action>()

    val consoleSender = Bukkit.getConsoleSender()

    init {
        actions["COMMAND"] = Action { p, line ->
            p?.let {
                Bukkit.dispatchCommand(p, line)
            }
        }
        actions["COMMAND-OP"] = Action { p, line ->
            p?.let {
                Bukkit.dispatchCommand(consoleSender, "execute as ${p.name} run $line")
            }
        }
        actions["CONSOLE"] = Action { _, line ->
            Bukkit.dispatchCommand(consoleSender, line)
        }
        actions["MESSAGE"] = Action { p, line ->
            p?.sendMessage(line.toComponent())
        }
        actions["CLOSE-INVENTORY"] = Action { p, _ ->
            p?.closeInventory()
        }
        actions["SOUND"] = Action { p, line ->
            val splitted = line.split(" ")
            p?.playAdvSound(splitted[0], splitted.getOrNull(1)?.toFloat() ?: 1.0F, splitted.getOrNull(2)?.toFloat() ?: 1.0F)
        }
        actions["ADD-PERMISSION"] = Action { p, line ->
            p?.let { p ->
                LuckPermsProvider.get().userManager.modifyUser(p.uniqueId) {user ->
                    user.data().add(Node.builder(line).build())
                }
            }
        }
        actions["REMOVE-PERMISSION"] = Action { p, line ->
            p?.let { p ->
                LuckPermsProvider.get().userManager.modifyUser(p.uniqueId) {user ->
                    user.data().remove(Node.builder(line).build())
                }
            }
        }
        actions["TAKE"] = Action { p, line ->
            p?.let { p ->
                p.takeItem(line)
            }
        }
        actions["GIVE"] = Action { p, line ->
            p?.let { p ->
                val noAdded = p.inventory.addItem(ItemParser.parse(line))
                for (value in noAdded.values) {
                    p.world.dropItem(p.location, value)
                }
            }
        }
//        actions["LOOP"] = Action { p, line ->
//            loop_regex.find(line)?.groupValues[1]?.let { options ->
//                val json = Json.parseToJsonElement("{$options}")
//                if (json is JsonObject) {
//                    val count = json["count"]?.jsonPrimitive?.intOrNull ?: 0
//                    val loopActions = try { json["loop_actions"]?.jsonPrimitive?.content as List<String> } catch (_: Exception) { listOf() }
//                    val endActions = try { json["end_actions"]?.jsonPrimitive?.content as List<String> } catch (_: Exception) { listOf() }
//                    var loopCount = 0
//                    Bukkit.getScheduler().runTaskTimer(instance, { task ->
//                        invokeList(loopActions, p, placeholders = mapOf(
//                            "loop_number" to (loopCount+1).toString(),
//                            "loop_number_inversed" to (count - loopCount).toString()
//                        ))
//                        loopCount++
//                        if (loopCount == count) {
//                            task.cancel()
//                            invokeList(endActions, p,)
//                        }
//
//                    }, 0L, 20L)
//                }
//            }
//        }
    }

    fun invokeList(lines: List<String>, player: Player?, update: Consumer<Player>? = null, placeholders: Map<String, String>? = null) {
        for (line in lines) {
            invoke(line, player, update, placeholders)
        }
    }

    fun invoke(line: String, player: Player?, update: Consumer<Player>? = null, placeholders: Map<String, String>? = null) {
        val result = regex.find(line)
        if (result != null) {
            var line = line.replaceFirst(regex, "").trim()
            if (!placeholders.isNullOrEmpty()) {
                line = line.withMapStr(placeholders)
            }
            val id = result.groupValues[1]
            if (id.uppercase() == "UPDATE") {
                Bukkit.getScheduler().runTaskLater(instance, { _ ->
                    player?.let { p -> update?.accept(p) }
                }, 0)
                return
            }
            val action = actions.get(id.uppercase())
            if (action != null) {
                val found = regexAdditional.findAll(line)
                val delay = found.filter { it.groupValues.lastOrNull() == "delay" }.firstOrNull()?.groupValues?.getOrNull(2)?.toInt() ?: 0
                val chance = found.filter { it.groupValues.lastOrNull() == "chance" }.firstOrNull()?.groupValues?.getOrNull(2)?.toInt() ?: 100
                line = line.replace(regexAdditional, "")
                line = replaceExpressions(line)
                if ((0..100).random() <= chance) {
                    Bukkit.getScheduler().runTaskLater(instance, { _ ->
                        action.run(player, PlaceholderAPI.setPlaceholders(player, line.trim()))
                    }, delay.toLong())
                }
            } else {
                "Не существует данного действия $id".logErr(instance)
            }
        } else {
            "Неверная строка $line".logErr(instance)
        }
    }

    fun replaceExpressions(line: String) : String {
        var line = line
        val result = exprRegex.findAll(line)
        for (matchResult in result) {
            line = line.replaceFirst(matchResult.value, try {
                ExpressionBuilder(matchResult.groupValues[1]).build().evaluate().toString()
            } catch (e: Exception) {
                ""
            })
        }
        return line
    }

}