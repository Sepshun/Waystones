package xyz.atrius.waystones.utility

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.persistence.PersistentDataType.INTEGER
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import xyz.atrius.waystones.data.crafting.CraftingRecipe
import xyz.atrius.waystones.plugin

// Just because I'm petty
typealias KotlinPlugin =
    JavaPlugin

private val DEFAULT_LORE: String =
    "${ChatColor.DARK_PURPLE}Warpstone: [${ChatColor.MAGIC}UNKNOWN${ChatColor.DARK_PURPLE}]"

fun defaultWarpKey(amount: Int = 1): ItemStack = ItemStack(Material.COMPASS, amount).update<CompassMeta> {
    this["is_warp_key", INTEGER] = 1
    lore = listOf(DEFAULT_LORE)
    setDisplayName("${ChatColor.GOLD}Warpstone Key")
}

fun KotlinPlugin.registerEvents(vararg listeners: Listener) {
    val events = server.pluginManager
    listeners.forEach { events.registerEvents(it, plugin) }
}

fun KotlinPlugin.registerRecipes(vararg recipes: Recipe) = recipes.forEach {
    if (it is CraftingRecipe)
        it.setup()
    server.addRecipe(it)
}

fun BukkitScheduler.scheduleRepeatingAutoCancelTask(
    delay: Long, period: Long = 1, task: (Long) -> Unit, finish: Runnable? = null
): Int {
    var timer = delay
    val id = scheduleSyncRepeatingTask(plugin, { task(timer).also { timer-- } }, 0, period)
    scheduleSyncDelayedTask(plugin, {
        if (isQueued(id)) cancelTask(id).also {
            finish?.run()
        }
    }, delay)
    return id
}

fun KotlinPlugin.registerCommands(vararg commands: Pair<String, CommandExecutor>) = commands.forEach { (command, executor) ->
    getCommand(command)?.setExecutor(executor)
}