package fr.elysiancraft.boss.boss;

import fr.elysiancraft.boss.Boss;
import org.bukkit.*;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class AxolotlBoss extends BossBase {
    private Player lastPlayer;

    public AxolotlBoss(String name, World world, Location location) {
        Axolotl axolotl = world.spawn(location, Axolotl.class);
        axolotl.setCustomName(name);
        axolotl.setInvulnerable(true);
        axolotl.setAI(true);
        axolotl.addScoreboardTag("bossAxolotl");
        axolotl.setVariant(Axolotl.Variant.GOLD);
        axolotl.setAdult();
        axolotl.setAgeLock(true);
        axolotl.setPersistent(true);
        axolotl.setCollidable(false);

        this.setEntity(axolotl);
        this.setData("hunger", PersistentDataType.INTEGER, 0);
    }

    public AxolotlBoss(LivingEntity axolotl) {
        this.setEntity(axolotl);
    }

    @Override
    public void rightClick(Player player) {
        super.rightClick(player);
        if (this.getHunger() >= 100) {
            player.sendMessage("Je n'ai plus faim ! Mais tu peux revenire plus tard.");
            return;
        }

        if (!player.getInventory().getItemInMainHand().getType().equals(Material.COD)) {
            player.sendMessage("C'est quoi sa ?");
            player.sendMessage("Je vais TE manger pour la peine !");
            this.eatPlayer(player);
            return;
        }
        this.setHunger(this.getHunger() + player.getInventory().getItemInMainHand().getAmount());
        player.getInventory().removeItem(player.getInventory().getItemInMainHand());
        player.sendMessage("bouffe : "+this.getHunger());
        this.lastPlayer = player;
        player.sendMessage("Merci de m'avoir nourrit !");
        if (this.getHunger() >= 100) {
            this.waitTime(1000);
            player.sendMessage("Je te donne ce cadeau !");
            ItemStack branchiflore = new ItemStack(Material.DRIED_KELP);
            branchiflore.setAmount(1);
            ItemMeta im = branchiflore.getItemMeta();
            assert im != null;
            im.setDisplayName("Branchiflore");
            ArrayList<String> lore = new ArrayList<>();
            lore.add("Permet de respirer sous l'eau pendant 1h.");
            im.setLore(lore.stream().toList());
            im.getPersistentDataContainer().set(new NamespacedKey(Boss.getInstance(), "customFood"), PersistentDataType.STRING, "branchiflore");
            branchiflore.setItemMeta(im);
            player.sendMessage(branchiflore.getItemMeta().getDisplayName());
            this.getEntity().getWorld().dropItem(this.getEntity().getLocation(), branchiflore);
        }
    }

    @Override
    public void playerRound(Player player) {
        super.playerRound(player);
        if (player.equals(this.lastPlayer)) return;
        if (this.getHunger() >= 100) {
            player.sendMessage("Je n'ai plus faim ! Mais tu peux revenire plus tard.");
            return;
        }
        player.sendMessage("Je suis coinc\u00E9 ici depuis longtemps, j'ai faim ...");
        BukkitRunnable foodRequest = new BukkitRunnable() {

            private int oldHunger = getHunger();
            @Override
            public void run() {
                player.sendMessage("J'ai, tr\u00E8s faim, t'aurais pas de la morue ??");
                checkFoodGive.runTaskLater(Boss.getInstance(), 20 * 5);
            }

            final BukkitRunnable checkFoodGive = new BukkitRunnable() {
                @Override
                public void run() {
                    if (getHunger() <= oldHunger) {
                        player.sendMessage("Dans ce cas je vais TE manger !");
                        eatPlayer(player);
                    }
                }
            };
        };

        foodRequest.runTaskLater(Boss.getInstance(), 20 * 5);
    }

    public void setHunger(int hunger) {
        this.setData("hunger", PersistentDataType.INTEGER, hunger);
    }

    public int getHunger() {
        return (int) this.getData("hunger", PersistentDataType.INTEGER);
    }

    private void eatPlayer(Player player) {
        if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) return;
        this.getEntity().attack(player);
        player.setHealth(0);
        this.setHunger(this.getHunger() + 5);
    }
}
