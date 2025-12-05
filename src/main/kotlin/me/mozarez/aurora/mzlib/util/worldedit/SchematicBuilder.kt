package me.mozarez.aurora.mzlib.util.worldedit

import com.fastasyncworldedit.core.util.TaskManager
import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader
import com.sk89q.worldedit.function.operation.Operation
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.transform.AffineTransform
import com.sk89q.worldedit.session.ClipboardHolder
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class SchematicBuilder(val instance: JavaPlugin, val file: File) {
    private var location : Location? = null
    private var rotation : Int = 0

    private var editSession : EditSession? = null

    val clipboardholder : ClipboardHolder
        get() {
            val holder = ClipboardHolder(ClipboardFormats.findByFile(file)?.getReader(file.inputStream())?.read() ?: throw Exception("Не найден файл!"))
            holder.transform = (AffineTransform().rotateY(rotation.toDouble()))
            return holder
        }

    fun location(location: Location) : SchematicBuilder {
        this.location = location
        return this
    }

    fun rotation(rotation: Int) : SchematicBuilder {
        this.rotation = rotation
        return this
    }

    fun paste() : SchematicBuilder {
        val location = location!!
        val point = BukkitAdapter.adapt(location).toBlockPoint()

        editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(location.world))

        TaskManager.taskManager().async {
            val operation = clipboardholder.createPaste(editSession).to(point).ignoreAirBlocks(false).build()
            Operations.complete(operation)
            editSession!!.close()
        }
        return this
    }

    fun undo() {
        TaskManager.taskManager().async {
            editSession!!.undo(editSession)
        }
    }

}