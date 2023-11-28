package fr.elysiancraft.boss.boss;

import fr.elysiancraft.boss.Boss;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class BossBase {
    private LivingEntity entity;
    private final ArrayList<Player> playersEvent = new ArrayList<>();
    private int bossRespawnTime;


    //Events Management
    public void rightClick(Player player) {}
    public void playerEventMove(Player player) {
        if (this.playersEvent.contains(player)) return;
        this.playersEvent.add(player);
        this.playerRound(player);
    }
    public void enableEventRadius(Player player) {
        this.playersEvent.remove(player);
    }
    public void playerRound(Player player) {}
    public void onDeath() {
        if (this.bossRespawnTime == 0) {
            this.entity.remove();
            return;
        } else {
            //@TODO: Respawn boss
        }
    }


    //Entity Management
    public LivingEntity getEntity() {
        return this.entity;
    }
    public void setEntity(LivingEntity livingEntity) {
        this.entity = livingEntity;
    }


    //Data Management
    public void setData(String key, PersistentDataType type, Object value) {
        this.entity.getPersistentDataContainer().set(new NamespacedKey(Boss.getInstance(), key),
                type,
                value
        );
    }
    public Object getData(String key, PersistentDataType type) {
        return this.entity.getPersistentDataContainer().get(new NamespacedKey(Boss.getInstance(), key),
                type
        );
    }
}
