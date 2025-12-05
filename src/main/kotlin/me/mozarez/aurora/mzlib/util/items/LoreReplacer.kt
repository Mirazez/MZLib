package me.mozarez.aurora.mzlib.util.items

import me.mozarez.aurora.mzlib.util.adventure.MiniMessageUtil.toComponent
import org.bukkit.inventory.ItemStack

object LoreReplacer {

    fun ItemStack.replaceWithMap(map: Map<String, String>) {
        for ((k,v) in map) {
            this.editMeta { meta -> meta.lore((meta.lore() ?: emptyList()).map { z -> z.replaceText { t -> t.match("%$k%").replacement(v) } }) }
        }
    }

    fun ItemStack.addLore(lore: List<String>) {
        this.editMeta { meta -> meta.lore((meta.lore() ?: emptyList()) + lore.map { it.toComponent() }) }
    }

}