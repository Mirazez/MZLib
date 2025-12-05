package me.mozarez.aurora.mzlib.util.conditions

import me.clip.placeholderapi.PlaceholderAPI
import me.mozarez.aurora.mzlib.MZLib.Companion.instance
import me.mozarez.aurora.mzlib.util.adventure.MiniMessageUtil.logErr
import me.mozarez.aurora.mzlib.util.exp4j.Exp4jOperators
import net.Indyuce.mmoitems.MMOItems
import net.objecthunter.exp4j.ExpressionBuilder
import org.bukkit.entity.Player

object ConditionManager {

    val regex = Regex("\\[([\\w-]+)\\]")
    val conditions = HashMap<String, Condition>()

    init {
        conditions["HAS-PERMISSION"] = Condition { p, line ->
            requireNotNull(p)
            return@Condition p.hasPermission(line.trim())
        }
        conditions["HAS-MMOITEM"] = Condition { p, line ->
            requireNotNull(p)
            val splitted = line.split(" ")
            val type = splitted[0]
            val id = splitted[1]
            var count = 0
            for (item in p.inventory.contents.filterNotNull()) {
                if (MMOItems.getType(item)?.id?.uppercase() == type && MMOItems.getID(item) == id) {
                    count += item.amount
                }
            }
            return@Condition count >= (splitted.getOrElse(2) {1}).toString().toInt()
        }
        conditions["EXPRESSION"] = Condition { p, line ->
            return@Condition try {
                ExpressionBuilder(line).operator(Exp4jOperators.operators).build().evaluate() != 0.0
            } catch (e: IllegalArgumentException) {
                "Произошла ошибка при проверке строки $line".logErr(instance)
                e.printStackTrace()
                false
            }
        }
    }

    fun invoke(line: String, player: Player? = null): Boolean {
        val result = regex.find(line)
        if (result != null) {
            val line = PlaceholderAPI.setPlaceholders(player, line.replaceFirst(regex, "").trim())
            val id = result.groupValues[1]
            val condition = conditions.get(id.uppercase())
            if (condition != null) {
                return condition.check(line, player)
            } else {
                "Не существует данного условия $id".logErr(instance)
            }
        } else {
            "Неверная строка $line".logErr(instance)
        }
        return false
    }

    fun invokeList(conditions: List<String>, player: Player? = null) : Boolean {
        return conditions.map { invoke(PlaceholderAPI.setPlaceholders(player, it), player) }.all { it }
    }

}