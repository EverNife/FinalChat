package br.com.finalcraft.finalchat.config.fancychat;

import br.com.finalcraft.evernifecore.fancytext.FancyText;
import br.com.finalcraft.evernifecore.util.FCColorUtil;
import br.com.finalcraft.finalchat.FinalChat;
import br.com.finalcraft.finalchat.config.ConfigManager;

public class TellTag {

    public static TellTag TELL_TAG;

    private final String name;
    private String sender_format;
    private String receiver_format;
    private String hover_message;
    private String suggest_command;

    private final FancyText fancyTextSender;
    private final FancyText fancyTextReceiver;

    public static void initialize(){
        TELL_TAG = new TellTag();
        FinalChat.info("§aFinished Loading TellTag!");
    }

    public TellTag(){
        this.name               = "TellTag";
        this.sender_format      = ConfigManager.getMainConfig().getString("TellTag.sender-format","");
        this.receiver_format    = ConfigManager.getMainConfig().getString("TellTag.receiver-format","");
        this.suggest_command    = ConfigManager.getMainConfig().getString("TellTag.suggest-command","");

        StringBuilder hoverBuilder = new StringBuilder();
        for (String line : ConfigManager.getMainConfig().getStringList("TellTag.hover-messages")){
            hoverBuilder.append(line + "\n");
        }
        if (hoverBuilder.toString().isEmpty()){
            this.hover_message = "";
        }else {
            this.hover_message = hoverBuilder.substring(0,hoverBuilder.length() - 1);
        }

        this.sender_format           = FCColorUtil.colorfy(this.sender_format);
        this.receiver_format         = FCColorUtil.colorfy(this.receiver_format);
        this.hover_message           = FCColorUtil.colorfy(this.hover_message);
        suggest_command = (!suggest_command.startsWith("/") ? "/" + suggest_command : suggest_command);

        fancyTextSender = new FancyText(sender_format);
        fancyTextReceiver = new FancyText(receiver_format);
        if (!this.hover_message.isEmpty()){
            fancyTextSender.setHoverText(hover_message);
            fancyTextReceiver.setHoverText(hover_message);
        }

        if (!this.suggest_command.isEmpty()) {
            fancyTextSender.setSuggestCommandAction(suggest_command);
            fancyTextReceiver.setSuggestCommandAction(suggest_command);
        }
    }

    public String getName() {
        return name;
    }

    public FancyText getFancyTextSender() {
        return fancyTextSender;
    }

    public FancyText getFancyTextReceiver() {
        return fancyTextReceiver;
    }
}
