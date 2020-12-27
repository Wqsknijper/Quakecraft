package dev.nidocraft.net.Gamemanager;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerData {

    public static HashMap<Player, PlayerCache> cache = new HashMap<Player, PlayerCache>();

    public static PlayerCache getCache(Player p) {
        if(!cache.containsKey(p)) cache.put(p, new PlayerCache(p));
        return cache.get(p);
    }
}
