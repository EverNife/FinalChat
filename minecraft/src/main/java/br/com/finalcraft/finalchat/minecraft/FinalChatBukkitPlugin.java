package br.com.finalcraft.finalchat;

import br.com.finalcraft.evernifecore.ecplugin.annotations.ECPlugin;
import br.com.finalcraft.evernifecore.listeners.base.ECListener;
import br.com.finalcraft.evernifecore.version.MCVersion;
import br.com.finalcraft.finalchat.commands.CommandRegisterer;
import br.com.finalcraft.finalchat.config.ConfigManager;
import br.com.finalcraft.finalchat.integration.builtin.DefaultParser;
import br.com.finalcraft.finalchat.integration.builtin.FactionsParser;
import br.com.finalcraft.finalchat.listener.FancyChatListener;
import br.com.finalcraft.finalchat.placeholders.PlaceHolderIntegration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@ECPlugin
public class FinalChat extends JavaPlugin{

    public static FinalChat instance;

    public static void info(String msg){
        instance.getLogger().info("[Info] " + msg.replace("&","§"));
    }
    public static void chatLog(String msg){
        instance.getLogger().info("[ChatLog] " + (MCVersion.isLowerEquals(MCVersion.v1_7_10) ? ChatColor.stripColor(msg) : msg));
    }
    public static void debug(String msg){
        instance.getLogger().info("[Debug] " + msg.replace("&","§"));
    }

    @Override
    public void onEnable() {
        instance = this;

        info("&aIniciando o Plugin...");

        info("&aCarregando configuracoes...");
        ConfigManager.initialize(this);

        info("&aRegistrando comandos...");
        CommandRegisterer.registerCommands(this, true);

        info("&aRegistrando Listeners");
        ECListener.register(this, FancyChatListener.class);

        //Iniciando PlaceHolderAPI Integration
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            info("&aIntegration to PlaceHolderAPI");
            PlaceHolderIntegration.initialize();
        }

        new BukkitRunnable(){
            @Override
            public void run() {
                if (Bukkit.getPluginManager().isPluginEnabled("Factions")){
                    info("&aIntegration to Factions");
                    FactionsParser.initialize();
                }
            }
        }.runTaskLater(instance,1);

        DefaultParser.initialize();
    }

    @Override
    public void onDisable() {

    }

    @ECPlugin.Reload
    public void onReload() {
        ConfigManager.initialize(FinalChat.instance);
        CommandRegisterer.registerCommands(FinalChat.instance, false);
    }


}
