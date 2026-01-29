package com.LinzeElze;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeathBack extends JavaPlugin implements Listener, CommandExecutor {

    private final Map<UUID, Location> deathLocations = new HashMap<>();
    private FileConfiguration langConfig;

    @Override
    public void onEnable() {
        // 初始化設定
        reloadPluginConfig();

        getServer().getPluginManager().registerEvents(this, this);
        if (getCommand("deathback") != null) {
            getCommand("deathback").setExecutor(this);
        }
        getLogger().info("DeathBack 插件已啟動！");
    }

    // 封裝成一個方法，方便初始化與 Reload 調用
    public void reloadPluginConfig() {
        saveDefaultConfig();      // 產生 config.yml
        reloadConfig();           // 重新讀取 config.yml 到記憶體
        saveAllLanguages();       // 導出所有語言檔
        loadLanguageConfig();     // 讀取對應語言內容
    }

    private void saveAllLanguages() {
        String[] languages = {"messages_zh.yml", "messages_en.yml"};
        File langFolder = new File(getDataFolder(), "languages");
        if (!langFolder.exists()) langFolder.mkdirs();

        for (String fileName : languages) {
            File langFile = new File(langFolder, fileName);
            if (!langFile.exists()) {
                saveResource("languages/" + fileName, false);
            }
        }
    }

    private void loadLanguageConfig() {
        String lang = getConfig().getString("language", "zh");
        File langFile = new File(getDataFolder(), "languages/messages_" + lang + ".yml");

        if (!langFile.exists()) {
            langFile = new File(getDataFolder(), "languages/messages_zh.yml");
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    private String getMsg(String path) {
        String message = langConfig.getString(path, "§c[Missing message: " + path + "]");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location loc = player.getLocation();
        deathLocations.put(player.getUniqueId(), loc);

        String coords = String.format("X:%d Y:%d Z:%d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        TextComponent mainMsg = new TextComponent(getMsg("death_message").replace("%coords%", coords) + " ");
        TextComponent btn = new TextComponent(getMsg("click_button"));
        btn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deathback"));
        btn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(getMsg("hover_text"))));

        mainMsg.addExtra(btn);
        player.spigot().sendMessage(mainMsg);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 檢查 reload 子指令
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            // 從語言檔讀取「權限不足」訊息
            if (!sender.hasPermission("deathback.admin")) {
                sender.sendMessage(getMsg("no_permission"));
                return true;
            }

            reloadPluginConfig();

            // 從語言檔讀取「重載成功」訊息
            sender.sendMessage(getMsg("reload_success"));
            return true;
        }

        // 正常的 /deathback 邏輯
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getMsg("only_player"));
            return true;
        }

        UUID uuid = player.getUniqueId();
        if (deathLocations.containsKey(uuid)) {
            player.teleport(deathLocations.get(uuid));
            player.sendMessage(getMsg("teleport_success"));
            deathLocations.remove(uuid);
        } else {
            player.sendMessage(getMsg("no_record"));
        }
        return true;
    }
}