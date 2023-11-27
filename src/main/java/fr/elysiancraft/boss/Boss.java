package fr.elysiancraft.boss;

import fr.elysiancraft.boss.boss.AxolotlBoss;
import fr.elysiancraft.boss.boss.BossBase;
import fr.elysiancraft.boss.commands.AxolotBossCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Objects;

public final class Boss extends JavaPlugin implements Listener {
    private final ArrayList<BossBase> bossList = new ArrayList<>();

    @Override
    public void onEnable() {
        getLogger().info("Plugin activ\u00E9 !");
        Objects.requireNonNull(getCommand("axolotboss")).setExecutor(new AxolotBossCommand());
        getServer().getPluginManager().registerEvents(this, this);
        bossList.clear();

        getServer().getWorlds().forEach(world -> {
            world.getEntities().forEach(entity -> {
                if (entity.getScoreboardTags().contains("bossAxolotl")) {
                    getLogger().info("Axolotl boss found !");
                    bossList.add(new AxolotlBoss((LivingEntity) entity));
                }
            });
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin d\u00E9sactiv\u00E9 !");
    }

    public void addBoss(BossBase boss) {
        bossList.add(boss);
    }

    @EventHandler
    public void playerClick(PlayerInteractAtEntityEvent event) throws InterruptedException {
        bossList.forEach(boss -> {
            if (boss.getEntity().equals(event.getRightClicked())) {
                boss.rightClick(event.getPlayer());
            }
        });
    }
    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        bossList.forEach(boss -> {
            if (boss.getEntity().getLocation().distance(player.getLocation()) <= 5) {
                boss.playerEventMove(player);
            } else {
                boss.enableEventRadius(player);
            }
        });
    }
    @EventHandler
    public void entityDeath(EntityDeathEvent event) {
        bossList.forEach(boss -> {
            if (boss.getEntity().equals(event.getEntity())) {
                boss.onDeath();
                bossList.remove(boss);
            }
        });
    }
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        ItemStack consumed = event.getItem();
        if (!Objects.requireNonNull(consumed.getItemMeta()).getPersistentDataContainer().has(new NamespacedKey(Boss.getInstance(),"customFood"), PersistentDataType.STRING)) return;
        Player consumer = event.getPlayer();
        if (Objects.equals(Objects.requireNonNull(consumed.getItemMeta()).getPersistentDataContainer().get(new NamespacedKey(Boss.getInstance(), "customFood"), PersistentDataType.STRING), "branchiflore")) {
            consumer.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING,72000,2,true,true));
        }
    }


    public static Boss getInstance() {
        return JavaPlugin.getPlugin(Boss.class);
    }
}