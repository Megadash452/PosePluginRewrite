package me.megadash452.posepluginrewrite

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent

class SneakListener : Listener {
    // companion object {
    //     // @JvmStatic val sneakPoseBuilder =
    // }

    @EventHandler
    fun onSneak(event: PlayerToggleSneakEvent) {
        // Don't enable crawling when player is flying
        if (!event.player.isFlying) {
            if (event.isSneaking) {
                println("Player sneaked!!!")
            } else {
                println("Player stood!!!")
            }
        }
    }
}