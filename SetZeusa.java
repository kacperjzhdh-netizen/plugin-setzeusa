package pl.zeus.setzeusa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class SetZeusa extends JavaPlugin implements Listener {
    private static final String MENU_TITLE = ChatColor.DARK_PURPLE + "⚡ " +
            ChatColor.GOLD + "LEGENDARNY SET ZEUSA " + ChatColor.YELLOW + "⚡";

    private NamespacedKey zeusSwordKey;

    @Override
    public void onEnable() {
        zeusSwordKey = new NamespacedKey(this, "zeus_sword");
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("SetZeusa wlaczony. Komenda: /setzeusa");
    }

    private Enchantment enchantment(String key) {
        return Registry.ENCHANTMENT.get(NamespacedKey.minecraft(key));
    }

    private void addEnchant(ItemMeta meta, String key, int level) {
        Enchantment enchantment = enchantment(key);
        if (enchantment != null) {
            // true pozwala ustawić poziom ponad zwykły limit enchantu.
            meta.addEnchant(enchantment, level, true);
        } else {
            getLogger().warning("Nie znaleziono enchantu: " + key);
        }
    }

    private ItemStack makeArmor(Material material, String displayName, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + displayName);
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + description,
                ChatColor.YELLOW + "Ochrona " + ChatColor.WHITE + "52",
                ChatColor.AQUA + "Niezniszczalny"
        ));
        meta.setUnbreakable(true);
        addEnchant(meta, "protection", 52);
        addEnchant(meta, "unbreaking", 10);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack makeHelmet() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Głowa Zeusa");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Hełm władcy piorunów",
                ChatColor.YELLOW + "Ochrona " + ChatColor.WHITE + "52",
                ChatColor.AQUA + "Niezniszczalny",
                ChatColor.DARK_GRAY + "Uwaga: domyślna tekstura główki"
        ));
        meta.setUnbreakable(true);
        addEnchant(meta, "protection", 52);
        addEnchant(meta, "unbreaking", 10);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack makeSword() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Miecz Zeusa");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Broń władcy Olimpu",
                ChatColor.RED + "Ostrość " + ChatColor.WHITE + "53",
                ChatColor.AQUA + "Niezniszczalny",
                ChatColor.YELLOW + "Przy trafieniu: błyskawica"
        ));
        meta.setUnbreakable(true);
        addEnchant(meta, "sharpness", 53);
        addEnchant(meta, "unbreaking", 10);
        meta.getPersistentDataContainer().set(zeusSwordKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack filler() {
        ItemStack item = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    private void openMenu(Player player) {
        ZeusMenuHolder holder = new ZeusMenuHolder();
        Inventory inventory = Bukkit.createInventory(holder, 27, MENU_TITLE);
        holder.setInventory(inventory);

        ItemStack pane = filler();
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, pane);
        }
        inventory.setItem(11, makeHelmet());
        inventory.setItem(13, makeArmor(Material.GOLDEN_CHESTPLATE,
                "Tunika Zeusa", "Zbroja boga piorunów"));
        inventory.setItem(15, makeSword());
        player.openInventory(inventory);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("setzeusa")) {
            return false;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Tej komendy może użyć tylko gracz.");
            return true;
        }
        if (!player.hasPermission("setzeusa.use")) {
            player.sendMessage(ChatColor.RED + "Nie masz uprawnienia do tej komendy.");
            return true;
        }
        openMenu(player);
        return true;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof ZeusMenuHolder)) {
            return;
        }

        event.setCancelled(true);

        // Reagujemy tylko na kliknięcia w górnym GUI. Kliknięcia w ekwipunek gracza
        // również są anulowane, aby shift-click i skróty nie przenosiły przedmiotów.
        if (event.getClickedInventory() == null ||
                event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack reward = switch (event.getRawSlot()) {
            case 11 -> makeHelmet();
            case 13 -> makeArmor(Material.GOLDEN_CHESTPLATE,
                    "Tunika Zeusa", "Zbroja boga piorunów");
            case 15 -> makeSword();
            default -> null;
        };
        if (reward == null) return;

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ChatColor.RED + "Masz pełny ekwipunek!");
            return;
        }

        player.getInventory().addItem(reward);
        player.sendMessage(ChatColor.GOLD + "Otrzymujesz: " +
                reward.getItemMeta().getDisplayName());
    }

    @EventHandler
    public void onMenuDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof ZeusMenuHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwordHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon.getType() != Material.NETHERITE_SWORD || !weapon.hasItemMeta()) return;

        ItemMeta meta = weapon.getItemMeta();
        if (!meta.getPersistentDataContainer().has(zeusSwordKey, PersistentDataType.BYTE)) return;

        // Efekt wizualny: nie zadaje dodatkowych obrażeń i nie niszczy bloków.
        event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
    }

    private static final class ZeusMenuHolder implements InventoryHolder {
        private Inventory inventory;

        void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }
}
