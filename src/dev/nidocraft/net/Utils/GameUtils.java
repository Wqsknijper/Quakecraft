package dev.nidocraft.net.Utils;

import dev.nidocraft.net.Enums.GameStates;
import dev.nidocraft.net.Gamemanager.GameCache;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dev.nidocraft.net.Main.getValueFromPath;

public class GameUtils {

    public static void startGame(){
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("§eThe game has started!");
            p.sendTitle("§eGood Luck", "§6Have Fun", 3, 3, 3);
            p.playSound(p.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 10, 1f);
        }
    }

    public static void startManager(){
        if(GameCache.players.size() > (int) getValueFromPath("min-players") && GameCache.state == GameStates.WAITING) GameCache.state = GameStates.STARTING;
        else if (GameCache.players.size() == (int) getValueFromPath("max-players") && GameCache.state == GameStates.STARTING) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("§eThe game has reached it's §lmax playerlimit§e. Timer changed to §l10 seconds§e!");
            }
        }

        while(GameCache.state == GameStates.STARTING) {

            GameCache.waitings = System.currentTimeMillis() + 30000;
            Long second = (GameCache.waitings - System.currentTimeMillis()) / 1000;

            if(((ArrayList) getValueFromPath("broadcast-seconds")).contains(second)) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    String word = second == 1 ? "second" : "seconds";

                    p.sendMessage("§eThe game is starting in §6" + second + " §e" + word);
                    p.sendTitle("§e" + second, "§6" + word, 3, 3, 3);
                    p.playSound(p.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 10, 1f);
                }
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
            Bukkit.getOnlinePlayers().stream().filter(t -> t.getLocation().distance(loc) < 50).forEach(t -> t.spawnParticle(Particle.BARRIER, loc, 1));
            for (Player t : Bukkit.getOnlinePlayers().stream().filter(t -> t.getLocation().distance(loc) < 0.7 && !hits.contains(t)).collect(Collectors.toList())) {
                hits.add(t);

                // code die gebeurt bij een kill
                // t = victim, p = attacker
            }
        }
    }

}
