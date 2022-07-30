package me.megadash452.posepluginrewrite

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.type.Slab
import org.bukkit.entity.Player
import org.bukkit.entity.Pose
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import java.util.UUID

/// Tells if a player is standing on a slab. When they are, their y-coord will be +0.5
fun isOnSlab(player_loc: Location): Boolean {
    return player_loc.y - player_loc.blockY > 0
}

val BARRIER_BLOCK = Bukkit.createBlockData(Material.BARRIER)
/// Spawn a barrier block above the player's head to put them in the swimming position
fun placeCrawlBlock(player: Player) {
    val loc = player.location.clone()
    // Create blocks on the upper half of the player
    loc.y += 1
    // Start in the top-right corner
    loc.x += 1
    loc.z += 1

    // Create a 3x1x3 (flat)
    for (z in 0..2) {
        for (x in 0..2) {
            val blockData = player.world.getBlockAt(loc).blockData
            // block below the CrawlBlock that is about to be placed
            val bottomLoc = loc.clone(); bottomLoc.y -= 1
            val blockBottom = player.world.getBlockAt(bottomLoc).blockData
            // Only place a barrier block if the block at this location isn't already a block
            // Only place a barrier block if you can walk through the bottom block
            // Slabs placed faced up get replaced
            if (!blockData.material.isSolid && !blockBottom.material.isSolid ||
               (blockData is Slab && blockData.type == Slab.Type.TOP))
                player.sendBlockChange(loc, BARRIER_BLOCK)

            loc.x -= 1
        }
        loc.x += 3
        loc.z -= 1
    }
}

/// Restore blocks placed by placeCrawlBlock()
fun unplaceCrawlBlock(player: Player, location: Location) {
    // CrawlBlocks will be on the upper half of the player
    location.y += 1
    // Start in the top-right corner
    location.x += 1
    location.z += 1

    for (z in 0..2) {
        for (x in 0..2) {
            // Get the data of the block stored on the server and send it to the player
            player.sendBlockChange(location, player.world.getBlockAt(location).blockData)
            location.x -= 1
        }
        location.x += 3
        location.z -= 1
    }
}


class CrawlListener : Listener {
    // The locations where a player has activated crawling
    private val crawlLocations: HashMap<UUID, ArrayDeque<Location>> = HashMap()

    private fun crawl(player: Player) {
        // Add player to the Hashmap
        if (crawlLocations[player.uniqueId] == null) {
            val dq = ArrayDeque<Location>()
            dq.addLast(player.location.clone())
            crawlLocations[player.uniqueId] = dq
        } else {
            crawlLocations[player.uniqueId]?.addLast(player.location.clone())
        }
        // Activate Crawling
        placeCrawlBlock(player);
    }

    private fun unCrawl(player: Player) {
        val playerDq = crawlLocations[player.uniqueId]

        // restore blocks in all the locations the player triggered a crawl
        if (playerDq != null)
            for (location in playerDq.reversed()) {
                unplaceCrawlBlock(player, location)

            }
        // Once all blocks have been restored, reset the Deque
        crawlLocations[player.uniqueId] = ArrayDeque()
    }

    @EventHandler
    fun onSneak(event: PlayerToggleSneakEvent) {
        val player = event.player
        // Don't enable crawling when player is flying
        if (!player.isFlying )
            // For now posing isn't allowed on slab (don't know how to differentiate being on slab or the air)
            if (event.isSneaking && player.pose != Pose.SWIMMING && !isOnSlab(player.location)) {
                // TODO: allow posing on slab (is player completely on ground or air?)
                // Player start crawling
                println("Player is Crawling!!!")
                crawl(player)
            } else if (!event.isSneaking) {
                // Player stands from a crawl
                println("Player stood!!!")
                unCrawl(player)
            }
    }
}