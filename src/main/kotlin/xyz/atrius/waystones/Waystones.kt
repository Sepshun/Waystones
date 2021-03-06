package xyz.atrius.waystones

import xyz.atrius.waystones.commands.WarpstoneCommand
import xyz.atrius.waystones.data.config.Config
import xyz.atrius.waystones.data.crafting.CompassRecipe
import xyz.atrius.waystones.event.*
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.KotlinPlugin
import xyz.atrius.waystones.utility.registerCommands
import xyz.atrius.waystones.utility.registerEvents
import xyz.atrius.waystones.utility.registerRecipes

lateinit var plugin       : KotlinPlugin
lateinit var configuration: Config

@Suppress("unused")
class Waystones : KotlinPlugin() {

    lateinit var names: WarpNameService

    override fun onEnable() {
        plugin        = this
        configuration = Config(this)
        names         = WarpNameService(this)
        // Register listeners
        registerEvents(
            WarpEvent(names),
            NameEvent(names),
            DestroyEvent(names),
            InfoEvent(names),
            LinkEvent(names)
        )
        // Register warp key recipe if enabled
        if (configuration.keyItems()) registerRecipes(
            CompassRecipe
        )
        // Register Waystones Command
        registerCommands(
            "waystones" to WarpstoneCommand
        )
        logger.info("Warpstones loaded!")
    }

    override fun onDisable() {
        logger.info("Warpstones disabled!")
    }
}