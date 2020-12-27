package dev.nidocraft.net.Gamemanager;

import dev.nidocraft.net.Enums.GameStates;
import org.bukkit.entity.Player;

public class PlayerCache {

    GameStates gameState;
    int kills;
    int deaths;
    Player player;
    long lastShoot;

    public PlayerCache(Player p){
        this.player = p;
        this.kills = 0;
        this.deaths = 0;
        this.gameState = GameStates.WAITING;
        this.lastShoot = System.currentTimeMillis();
    }

    public void save(){ PlayerData.cache.remove(player); }

    public void setGameState(GameStates gs){ this.gameState = gs; }

    public GameStates getGameState() { return this.gameState; }

    public void addKills(){ this.kills++; }

    public int getKills(){ return this.kills; }

    public void addDeaths(){ this.deaths++; }

    public int getDeaths(){ return this.deaths; }

    public void setLastShoot(Long ms){ this.lastShoot = ms; }

    public long getLastShoot(){ return this.lastShoot; }
}
