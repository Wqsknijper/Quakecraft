package dev.nidocraft.net.Utils;

import dev.nidocraft.net.Enums.GameStates;
import dev.nidocraft.net.Gamemanager.GameCache;
import dev.nidocraft.net.Gamemanager.PlayerData;
import dev.nidocraft.net.Main;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static dev.nidocraft.net.Main.getValueFromPath;

public class GameUtils {

    public static ItemStack gun;

    public static void gameEndManager() {
        Player p = PlayerUtils.getWinner();
        Bukkit.broadcastMessage("§a" + p.getName() + "§a has won the game!");
        for(int i = 0; i < 10; i++){
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new BukkitRunnable() {
                @Override
                public void run() {
                    Location loc = p.getLocation();
                    Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                    FireworkMeta fwm = fw.getFireworkMeta();

                    fwm.setPower(2);
                    fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

                    fw.setFireworkMeta(fwm);
                    fw.detonate();

                    Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                    fw2.setFireworkMeta(fwm);
                }
            }, 10L);
        }
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().spigot().restart();
            }
        }, 100L);
    }

    public static void startGame(){
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("§eThe game has started!");
            p.sendTitle("§eGood Luck", "§6Have Fun", 3, 3, 3);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 10, 1f);

            p.getInventory().setItem(0, gun);
        }
        GameCache.startTime = System.currentTimeMillis();
        GameCache.state = GameStates.STARTED;
    }

    public static void startManager(){
        if(GameCache.players.size() > (int) getValueFromPath("min-players") && GameCache.state == GameStates.WAITING) GameCache.state = GameStates.STARTING;
        else if (GameCache.players.size() == (int) getValueFromPath("max-players") && GameCache.state == GameStates.STARTING) {
            Bukkit.broadcastMessage("§eThe game has reached it's §lmax playerlimit§e. Timer changed to §l10 seconds§e!");
        }

        AtomicReference<Integer> i = new AtomicReference<>(0);
        GameCache.players.forEach(p -> {
            i.getAndSet(i.get() + 1);
            p.teleport(SpawnUtils.spawns.get(i));
        });

        while(GameCache.state == GameStates.STARTING) {

            GameCache.waitings = System.currentTimeMillis() + 30000;
            Long second = (GameCache.waitings - System.currentTimeMillis()) / 1000;

            if(((ArrayList) getValueFromPath("broadcast-seconds")).contains(second)) {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    String word = second == 1 ? "second" : "seconds";

                    p.sendMessage("§eThe game is starting in §6" + second + " §e" + word);
                    p.sendTitle("§e" + second, "§6" + word, 3, 3, 3);
                    p.playSound(p.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 10, 1f);
                });
            } else if(second == 0) {
                startGame();
            }
        }
    }

    public static void gunShooter(Player p) {
        Location loc = p.getLocation();
        Vector dir = p.getLocation().getDirection();

        List<Player> hits = new ArrayList<>();
        for (double i = 0; i < 250; i += 0.5) {
            loc.add(dir);
            if (loc.getBlock().getType() != Material.AIR) break;

            Bukkit.getOnlinePlayers().stream().filter(t -> t.getLocation().distance(loc) < 50).forEach(t -> t.spawnParticle(Particle.FIREWORKS_SPARK, loc, 1));
            for (Player t : Bukkit.getOnlinePlayers().stream().filter(t -> t.getLocation().distance(loc) < 0.7 && !hits.contains(t)).collect(Collectors.toList())) {
                hits.add(t);
                PlayerUtils.deathEvent(t, p);
            }
        }
    }

    public static Double getProgress(Player p) {
        Long eenProcent = (((long) Main.getValueFromPath("shoot-delay") * 1000) / 100);
        Long keerGetal = (PlayerData.getCache(p).getLastShoot() / eenProcent);
        Long eindGetal = eenProcent * keerGetal;
        return (double) eindGetal;
    }

    public static void createGun() {
        gun = new ItemStack(Material.STICK);

        ItemMeta gunMeta = gun.getItemMeta();
        gunMeta.setDisplayName("§eQuaker");
        gunMeta.setLore(Arrays.asList("§a", "§7Right-click to shoot into the direction you're looking at!"));

        gun.setItemMeta(gunMeta);
    }

}
