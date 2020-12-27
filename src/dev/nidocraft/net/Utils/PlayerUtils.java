package dev.nidocraft.net.Utils;

import dev.nidocraft.net.Gamemanager.GameCache;
import dev.nidocraft.net.Gamemanager.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerUtils implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e){
        //check for vanish
        e.setJoinMessage("");
        Thread a = new Thread(new BukkitRunnable() {
            @Override
            public void run() {
                PlayerData.getCache(e.getPlayer());
            }
        });
        a.start();
        GameCache.players.add(e.getPlayer());
        Bukkit.broadcastMessage("§7[§a+§7] §8" + e.getPlayer().getDisplayName() + " §7joined your game §7(§b" + GameCache.players.size() + "§7/12)");
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
        e.getPlayer().getInventory().clear();
        GameUtils.startManager();
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent e){
        //check vanish
        e.setQuitMessage("");
        Thread a = new Thread(new BukkitRunnable() {
            @Override
            public void run() {
                PlayerData.getCache(e.getPlayer()).save();
            }
        });
        a.start();
        Bukkit.broadcastMessage("§7[§a+§7] §8" + e.getPlayer().getDisplayName() + " §7left your game §7(§b" + (Bukkit.getOnlinePlayers().size() - 1) + "§7/12)");
        GameCache.players.remove(e.getPlayer());
    }
}
