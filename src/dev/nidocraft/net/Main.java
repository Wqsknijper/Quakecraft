package dev.nidocraft.net;

import dev.nidocraft.net.Gamemanager.GameCache;
import dev.nidocraft.net.Utils.PlayerUtils;
import dev.nidocraft.net.Utils.SpawnUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Main extends JavaPlugin {

    @Override
    public void onEnable(){
        SpawnUtils.generateSpawns(25, 75, -25, 47, 47, 20);
        regEvents();
        GameCache.seconds.addAll(Arrays.asList(30, 20, 10, 5, 4, 3, 2, 1));
        getLogger().info("Enabled");
    }

    void regEvents() {
        getServer().getPluginManager().registerEvents(new PlayerUtils(), this);
    }

}
