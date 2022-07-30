package me.megadash452.posepluginrewrite

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

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