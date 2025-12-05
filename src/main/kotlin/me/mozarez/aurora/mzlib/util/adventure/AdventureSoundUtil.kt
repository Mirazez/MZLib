package me.mozarez.aurora.mzlib.util.adventure

import net.kyori.adventure.sound.Sound
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import java.util.OptionalLong
import kotlin.math.floor

object AdventureSoundUtil {

    val defaultCategory = SoundCategory.VOICE

    fun getAdventureSound(key: NamespacedKey, category: SoundCategory, volume: Float, pitch: Float, seed: OptionalLong): Sound {
        return Sound.sound()
            .source(category)
            .volume(volume)
            .pitch(pitch)
            .seed(seed)
            .type(key)
            .build()
    }

    fun Player.playAdvSound(location: Location, sound: NamespacedKey, category: SoundCategory, volume: Float, pitch: Float, seed: OptionalLong) {
        this.playSound(
            getAdventureSound(sound, category, volume, pitch, seed),
            location.x(),
            location.y(),
            location.z()
        )
    }

    fun Player.playAdvSound(sound: NamespacedKey, category: SoundCategory, volume: Float, pitch: Float) {
        this.playAdvSound(sound,category,volume,pitch, OptionalLong.empty())
    }

    fun Player.playAdvSound(sound: NamespacedKey, category: SoundCategory, volume: Float, pitch: Float, seed: OptionalLong) {
        this.playSound(getAdventureSound(
            sound,
            category,
            volume,
            pitch,
            seed
        ))
    }

    fun Player.playAdvSound(sound: String, category: SoundCategory, volume: Float, pitch: Float, seed: OptionalLong) {
        this.playSound(getAdventureSound(NamespacedKey.fromString(sound)!!, category, volume, pitch, seed))
    }

    fun Player.playAdvSound(sound: org.bukkit.Sound, category: SoundCategory, volume: Float, pitch: Float, seed: OptionalLong) {
        this.playAdvSound(sound.key)
    }

    fun Player.playAdvSound(sound: String, category: SoundCategory, volume: Float, pitch: Float) {
        this.playAdvSound(sound, category, volume, pitch, OptionalLong.empty())
    }

    fun Player.playAdvSound(s: String) {
        this.playAdvSound(s, defaultCategory, 1.0F, 1.0F)
    }

    fun Player.playAdvSound(s: NamespacedKey) {
        this.playAdvSound(s, defaultCategory, 1.0F, 1.0F)
    }

    fun Player.playAdvSound(s: String, volume: Float, pitch: Float) {
        this.playAdvSound(s, defaultCategory, volume, pitch)
    }

    fun Player.playAdvSound(s: NamespacedKey, volume: Float, pitch: Float) {
        this.playAdvSound(s, defaultCategory, volume, pitch)
    }

}