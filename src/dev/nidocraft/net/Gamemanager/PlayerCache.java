package dev.nidocraft.net.Gamemanager;

import com.sun.media.jfxmedia.events.PlayerStateEvent;
import dev.nidocraft.net.Enums.PlayerStates;
import org.bukkit.entity.Player;

public class PlayerCache {

    PlayerStates state;
    int kills;
    int deaths;
    Player player;
    long lastShoot;

    public PlayerCache(Player p){
        this.player = p;
        this.kills = 0;
        this.deaths = 0;
        this.lastShoot = System.currentTimeMillis();
    }

    public void save() {
        PlayerData.cache.remove(player);
    }

    public void addKills() {
        this.kills++;
    }

    public int getKills() {
        return this.kills;
    }

    public void addDeaths() {
        this.deaths++;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setLastShoot(Long ms) {
        this.lastShoot = ms;
    }

    public void setState(PlayerStates playerState) {
        this.state = playerState;
    }

    public PlayerStates getState() {
        return this.state;
    }

    public long getLastShoot() {
        return this.lastShoot;
    }
}
