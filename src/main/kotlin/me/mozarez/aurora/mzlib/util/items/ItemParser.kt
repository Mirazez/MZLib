package me.mozarez.aurora.mzlib.util.items

import com.nexomc.nexo.api.NexoItems
import io.lumine.mythic.lib.player.modifier.ModifierSource
import io.th0rgal.oraxen.api.OraxenItems
import me.mozarez.aurora.mzlib.MZLib.Companion.instance
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.api.Type
import net.Indyuce.mmoitems.api.crafting.ConfigMMOItem
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil
import kotlin.math.min
import kotlin.random.Random

object ItemParser {

    class TakeResult(var bool: Boolean, private val items: ArrayList<ItemStack>) {
        fun addItem(item: ItemStack) {
            items.add(item)
        }
        fun getItems() : ArrayList<ItemStack> {
            return items
        }
    }

    fun Player.takeItem(str: String) : TakeResult {
        val splitted = str.split(" ")
        return when (splitted[0]) {
            "MMOITEMS" -> this.takeMMOItem(splitted[1], splitted[2], (splitted.getOrNull(3) ?: "1").toInt())
            "NEXO" -> this.takeNexoItem(splitted[1], (splitted.getOrNull(2) ?: "1").toInt())
            else -> throw IllegalArgumentException("Неверный формат предмета: $str")
        }
    }

    fun Player.takeMMOItem(type: String, id: String, count: Int) : TakeResult {
        val result = TakeResult(true, ArrayList())
        var countActual = 0
        this.inventory.getItem(EquipmentSlot.HAND).let { item ->
            if (MMOItems.getType(item)?.id?.uppercase() == type && MMOItems.getID(item) == id) {
                val take = min(item.amount, count)
                item.amount -= take
                countActual += take
                this.inventory.setItemInMainHand(item)

                val item = item.clone()
                item.amount = take
                result.addItem(item)

                if (countActual >= count) {
                    return result
                }
            }
        }
        this.inventory.getItem(EquipmentSlot.OFF_HAND).let { item ->
            if (MMOItems.getType(item)?.id?.uppercase() == type && MMOItems.getID(item) == id) {
                val take = min(item.amount, count)
                item.amount -= take
                countActual += take
                this.inventory.setItemInOffHand(item)

                val item = item.clone()
                item.amount = take
                result.addItem(item)

                if (countActual >= count) {
                    return result
                }
                //p.inventory.setItemInMainHand(item)
            }
        }
        var slot = 0
        for (item in this.inventory.contents) {
            if (item != null && MMOItems.getType(item)?.id?.uppercase() == type && MMOItems.getID(item) == id) {
                val take = min(item.amount, count)
                item.amount -= take
                countActual += take
                this.inventory.setItem(slot, item)

                val item = item.clone()
                item.amount = take
                result.addItem(item)

                if (countActual >= count) {
                    return result
                }
            }
            slot ++
        }
        result.bool = false
        return result
    }

    fun Player.takeNexoItem(id: String, count: Int) : TakeResult {
        val result = TakeResult(true, ArrayList())
        var countActual = 0
        this.inventory.getItem(EquipmentSlot.HAND).let { item ->
            if (NexoItems.idFromItem(item) == id) {
                val take = min(item.amount, count)
                item.amount -= take
                countActual += take
                this.inventory.setItemInMainHand(item)

                val item = item.clone()
                item.amount = take
                result.addItem(item)

                if (countActual >= count) {
                    return result
                }
            }
        }
        this.inventory.getItem(EquipmentSlot.OFF_HAND).let { item ->
            if (NexoItems.idFromItem(item) == id) {
                val take = min(item.amount, count)
                item.amount -= take
                countActual += take
                this.inventory.setItemInOffHand(item)

                val item = item.clone()
                item.amount = take
                result.addItem(item)

                if (countActual >= count) {
                    return result
                }
                //p.inventory.setItemInMainHand(item)
            }
        }
        var slot = 0
        for (item in this.inventory.contents) {
            if (item != null && NexoItems.idFromItem(item) == id) {
                val take = min(item.amount, count)
                item.amount -= take
                countActual += take
                this.inventory.setItem(slot, item)

                val item = item.clone()
                item.amount = take
                result.addItem(item)

                if (countActual >= count) {
                    return result
                }
            }
            slot ++
        }
        result.bool = false
        return result
    }

    fun ItemStack.toStringId() : String {
        if (instance.server.pluginManager.isPluginEnabled("Oraxen")) {
            OraxenItems.getIdByItem(this)?.let {
                return "ORAXEN $it ${this.amount}"
            }
        }
        if (instance.server.pluginManager.isPluginEnabled("MMOItems")) {
            MMOItems.getType(this)?.id?.let { type ->
                MMOItems.getID(this)?.let { id ->
                    return "MMOITEMS $type $id ${this.amount}"
                }
            }
        }
        if (instance.server.pluginManager.isPluginEnabled("Nexo")) {
            NexoItems.idFromItem(this)?.let {
                return "NEXO $it ${this.amount}"
            }
        }
        return this.type.toString()
    }

    fun ItemStack.isItem(string: String): Boolean {
        val splitted = string.split(" ")
        return when (splitted[0].uppercase()) {
            "MMOITEMS" -> {
                val category = splitted[1].uppercase()
                val itemid = splitted[2].uppercase()
                this.isMMOItem(category, itemid)
            }
            "MMOITEMSPREVIEW" -> {
                val category = splitted[1].uppercase()
                val itemid = splitted[2].uppercase()
                this.isMMOItem(category, itemid)
            }
            "ORAXEN" -> {
                OraxenItems.getIdByItem(this) == splitted[1]
            }
            "NEXO" -> {
                NexoItems.idFromItem(this) == splitted[1]
            }
            else -> false
        }
    }



    fun ItemStack.isOraxenItemWithId(id: String) : Boolean {
        return OraxenItems.getIdByItem(this) == id
    }

    fun ItemStack.isMMOItem(type: String, id: String) : Boolean {
        return MMOItems.getType(this)?.id == type && MMOItems.getID(this) == id
    }

    fun parse(string: String?) : ItemStack {
        var string = string
        if (string == null) {
            string = "GRASS"
        }
        var item = ItemStack(Material.AIR)
        val splitted = string.split(" ")
        when (splitted[0].uppercase()) {
            "MMOITEMS" -> {
                val category = splitted[1].uppercase()
                val itemid = splitted[2].uppercase()
                MMOItems.plugin.getItem(category, itemid)?.let { item = it }
                item.amount = parseAmount(splitted.getOrNull(3))
            }
            "MMOITEMSPREVIEW" -> {
                val category = splitted[1].uppercase()
                val itemid = splitted[2].uppercase()
                ConfigMMOItem(MMOItems.plugin.templates.getTemplate(Type(category, ModifierSource.VOID), itemid), parseAmount(splitted.getOrNull(3))).let { item = it.preview }
            }
            "ORAXEN" -> {
                val size = parseAmount(splitted.getOrNull(2))
                item = OraxenItems.getItemById(splitted[1]).setAmount(size).build()
            }
            "NEXO" -> {
                val size = parseAmount(splitted.getOrNull(2))
                item = NexoItems.itemFromId(splitted[1])?.build() ?: throw IllegalArgumentException("Неизвестный предмет Nexo: ${splitted[1]}")
                item.amount = size
            }
            else -> {
                try {
                    val splittedTwo = splitted[0].split(":")
                    item = ItemStack(Material.valueOf(splittedTwo[0]))
                    val meta = item.itemMeta
                    meta.setCustomModelData(splittedTwo[1].toIntOrNull() ?: 0)
                    item.itemMeta = meta
                } catch (_: Exception) {
                    throw IllegalArgumentException("Невозможно прочитать $string")
                }
            }
        }
        return item
    }

    fun parseAmount(s: String?): Int {
        if (s?.contains("~") == true) {
            s.split("~").map { it.toIntOrNull() ?: 1 }.let { splitted ->
                return splitted[0].rangeTo(splitted[1]).random()
            }
        }
        return s?.toIntOrNull() ?: 1
    }

}