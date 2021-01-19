package dev.nidocraft.net.Utils;

import dev.nidocraft.net.Enums.GameStates;
import dev.nidocraft.net.Enums.PlayerStates;
import dev.nidocraft.net.Gamemanager.GameCache;
import dev.nidocraft.net.Gamemanager.PlayerCache;
import dev.nidocraft.net.Gamemanager.PlayerData;
import dev.nidocraft.net.Gamemanager.SbManager;
import dev.nidocraft.net.Main;
import net.minecraft.server.v1_11_R1.Enchantment;
import net.minecraft.server.v1_11_R1.Enchantments;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.nidocraft.net.Main.getValueFromPath;
import static dev.nidocraft.net.Utils.GameUtils.gunShooter;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class PlayerUtils implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        //check for vanish
        if (GameCache.players.size() == (Integer) Main.getValueFromPath("max-players")) e.getPlayer().kickPlayer("§cGame is full!");

        e.setJoinMessage("");

        Thread a = new Thread(new BukkitRunnable() {
            @Override
            public void run() {
                PlayerData.getCache(e.getPlayer());
            }
        });
        a.start();

        GameCache.players.add(e.getPlayer());
        PlayerData.getCache(e.getPlayer()).setState(PlayerStates.WAITING);
        Bukkit.broadcastMessage("§7[§a+§7] §8" + e.getPlayer().getDisplayName() + " §7joined your game §7(§b" + GameCache.players.size() + "§7/12)");

        e.getPlayer().setGameMode(GameMode.ADVENTURE);
        e.getPlayer().getInventory().clear();
        e.getPlayer().teleport(Main.spawn);

        if (GameCache.state == GameStates.STARTING) e.getPlayer().teleport(SpawnUtils.spawns.get(GameCache.players.size()));
        GameUtils.startManager();
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent e) {
        //check vanish
        e.setQuitMessage("");

        Thread a = new Thread(new BukkitRunnable() {
            @Override
            public void run() {
                PlayerData.getCache(e.getPlayer()).save();
            }
        });
        a.start();

        Bukkit.broadcastMessage("§7[§c-§7] §8" + e.getPlayer().getDisplayName() + " §7left your game §7(§b" + (Bukkit.getOnlinePlayers().size() - 1) + "§7/12)");
        GameCache.players.remove(e.getPlayer());

        if (GameCache.state == GameStates.STARTING && GameCache.players.size() <= (int) getValueFromPath("min-players")) return;

        GameCache.state = GameStates.WAITING;
        Bukkit.broadcastMessage("§cNot enough players to start!");
        Bukkit.getOnlinePlayers().forEach(p -> p.teleport(Main.spawn));
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR) return;

        Player p = e.getPlayer();
        PlayerCache a = PlayerData.getCache(p);
        if (a.getState() != PlayerStates.PLAYING) return;
        if ((a.getLastShoot() + ((int) getValueFromPath("shoot-delay") * 1000)) >= System.currentTimeMillis()) return;

        if (p.getItemInHand().getType() != Material.STICK) return;

        gunShooter(p);
    }

    public static Player getWinner() {
        HashMap<Player, Integer> entry = new HashMap<>();
        for (Player p : GameCache.players) {
            entry.put(p, PlayerData.getCache(p).getKills());
        }

        Map.Entry<Player, Integer> entryWithMaxValue = null;
        for (Map.Entry<Player, Integer> currentEntry : entry.entrySet()) {
            if (entryWithMaxValue == null || currentEntry.getValue().compareTo(entryWithMaxValue.getValue()) > 0) entryWithMaxValue = currentEntry;
        }

        return entryWithMaxValue.getKey();
    }

    public static void deathEvent(Player victim, Player attacker) {
        PlayerData.getCache(attacker).addKills();
        PlayerData.getCache(victim).addDeaths();

        Bukkit.getOnlinePlayers().forEach(p -> SbManager.drawScoreboard(p));
        Bukkit.getOnlinePlayers().stream().filter(p -> victim == p || attacker == p).forEach(p -> p.sendMessage("§4" + victim.getName() + " §cwas shot by §4" + attacker.getName()));

        victim.setGameMode(GameMode.SPECTATOR);
        victim.setFlySpeed(0);
        victim.playSound(victim.getLocation(), Sound.ENTITY_ENDERDRAGON_HURT, 10F, 1F);

        MathUtils.deathMode(victim, attacker);
    }

    public static List<Map.Entry<Player, Integer>> getTop() {

        HashMap<Player, Integer> entry1 = new HashMap<>();
        for (Player p : GameCache.players) {
            entry1.put(p, PlayerData.getCache(p).getKills());
        }

        List<Map.Entry<Player, Integer>> top = entry1.entrySet().stream().sorted(comparing(Map.Entry::getValue, reverseOrder())).limit((int) Main.getValueFromPath("max-on-scoreboard")).collect(toList());

        return top;
    }
}
