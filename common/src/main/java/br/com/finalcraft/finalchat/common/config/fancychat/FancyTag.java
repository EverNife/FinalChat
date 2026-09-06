package br.com.finalcraft.finalchat.common.config.fancychat;

import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.evernifecore.util.FCColorUtil;
import br.com.finalcraft.finalchat.common.FinalChatBootstrap;
import br.com.finalcraft.finalchat.common.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;

/**
 * One configurable piece of a chat line, as declared under {@code TagFormats.<name>}: the text, its
 * hover and its click action, plus the optional permission and placeholder condition that decide
 * whether the piece shows up at all.
 */
public class FancyTag {

    public String name;
    public String permission;
    public String placeholderCondition;
    public String format;
    public String hover_message;
    public String run_command;
    public String suggest_command;

    FancyText fancyText;

    public static Map<String, FancyTag> mapOfFancyTags = new HashMap<String, FancyTag>();

    public static void initialize() {
        mapOfFancyTags.clear();

        for (String fancyTagName : ConfigManager.getMainConfig().getKeys("TagFormats")) {
            try {
                FancyTag fancyTag = new FancyTag(fancyTagName);
                mapOfFancyTags.put(fancyTagName, fancyTag);
            } catch (Exception e) {
                FinalChatBootstrap.get().getLog().warning("Could not read the FancyTag [{}]: {}", fancyTagName, e.getMessage());
            }
        }

        FinalChatBootstrap.get().getLog().info("Finished loading {} FancyTags!", mapOfFancyTags.size());
    }

    public FancyTag(String name) {
        this.name = name;
        this.format = ConfigManager.getMainConfig().getString("TagFormats." + name + ".format", "");
        this.permission = ConfigManager.getMainConfig().getString("TagFormats." + name + ".permission", "");
        this.placeholderCondition = ConfigManager.getMainConfig().getString("TagFormats." + name + ".placeholderCondition", "");

        StringBuilder hoverBuilder = new StringBuilder();
        for (String line : ConfigManager.getMainConfig().getStringList("TagFormats." + name + ".hover-messages")) {
            hoverBuilder.append(line + "\n");
        }
        if (hoverBuilder.toString().isEmpty()) {
            this.hover_message = "";
        } else {
            this.hover_message = hoverBuilder.substring(0, hoverBuilder.length() - 1);
        }

        this.run_command = ConfigManager.getMainConfig().getString("TagFormats." + name + ".run-command", "");
        this.suggest_command = ConfigManager.getMainConfig().getString("TagFormats." + name + ".suggest-command", "");

        format          = FCColorUtil.colorfy(format);
        hover_message   = FCColorUtil.colorfy(hover_message);

        fancyText = FancyText.of(format);
        if (!this.hover_message.isEmpty()) fancyText.setHover(hover_message);
        if (!this.run_command.isEmpty()) fancyText.setClickCommand(run_command);
        if (!this.suggest_command.isEmpty()) fancyText.setClickSuggest(suggest_command);
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
