package me.megadash452.posepluginrewrite

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

// fun main(args: Array<String>) {
//     println("Hello World!")
//
//     // Try adding program arguments via Run/Debug configuration.
//     // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
//     println("Program arguments: ${args.joinToString()}")
// }

class PosePluginRewrite : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        println("Plugin start")
        Bukkit.getPluginManager().registerEvents(CrawlListener(), this)
    }
    override fun onDisable() {
        // Plugin shutdown logic
        println("Plugin stop")
    }


}