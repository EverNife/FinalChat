package br.com.finalcraft.finalchat.config.fancychat;

import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.finalchat.FinalChat;
import br.com.finalcraft.finalchat.config.ConfigManager;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class FancyTag {

    public String name;
    public String permission;
    public String placeholderCondition;
    public String format;
    public String hover_message;
    public String run_command;
    public String suggest_command;

    FancyText fancyText;

    public static Map<String,FancyTag> mapOfFancyTags = new HashMap<String,FancyTag>();

    public static void initialize(){
        mapOfFancyTags.clear();

        for (String fancyTagName : ConfigManager.getMainConfig().getKeys("TagFormats")){
            try {
                FancyTag fancyTag = new FancyTag(fancyTagName);
                mapOfFancyTags.put(fancyTagName,fancyTag);
            }catch (Exception e){
                FinalChat.info("Error trying to read " + fancyTagName + " FancyTag : " + e.getMessage());
            }
        }

        FinalChat.info("§aFinished Loading " + mapOfFancyTags.size() + " FancyTags!");
    }

    public FancyTag(String name){
        this.name = name;
        this.format = ConfigManager.getMainConfig().getString("TagFormats." + name + ".format","");
        this.permission = ConfigManager.getMainConfig().getString("TagFormats." + name + ".permission","");
        this.placeholderCondition = ConfigManager.getMainConfig().getString("TagFormats." + name + ".placeholderCondition","");

        StringBuilder hoverBuilder = new StringBuilder();
        for (String line : ConfigManager.getMainConfig().getStringList("TagFormats." + name + ".hover-messages")){
            hoverBuilder.append(line + "\n");
        }
        if (hoverBuilder.toString().isEmpty()){
            this.hover_message = "";
        }else {
            this.hover_message = hoverBuilder.substring(0,hoverBuilder.length() - 1);
        }

        this.run_command = ConfigManager.getMainConfig().getString("TagFormats." + name + ".run-command","");
        this.suggest_command = ConfigManager.getMainConfig().getString("TagFormats." + name + ".suggest-command","");

        format          = ChatColor.translateAlternateColorCodes('&',format);
        hover_message   = ChatColor.translateAlternateColorCodes('&',hover_message);

        fancyText = new FancyText(format);
        if (!this.hover_message.isEmpty()) fancyText.setHoverText(hover_message);
        if (!this.run_command.isEmpty()) fancyText.setRunCommandAction(run_command);
        if (!this.suggest_command.isEmpty()) fancyText.setSuggestCommandAction(suggest_command);
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String getPlaceholderCondition() {
        return placeholderCondition;
    }

    public String getFormat() {
        return format;
    }

    public String getHover_message() {
        return hover_message;
    }

    public String getRun_command() {
        return run_command;
    }

    public String getSuggest_command() {
        return suggest_command;
    }

    public FancyText getFancyText() {
        return fancyText;
    }
}
