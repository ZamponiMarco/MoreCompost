package com.github.jummes.morecompost.command;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.morecompost.core.MoreCompost;

public class AboutCommand extends AbstractCommand {

    private final static String ABOUT_MSG = MessageUtils.header("MoreCompost About") + MessageUtils.color(
            String.format("&6&lMore&c&lCompost &7| &8&l%s\n", MoreCompost.getInstance().getDescription().getVersion()))
            + MessageUtils.delimiter("MoreCompost About");

    public AboutCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        sender.sendMessage(ABOUT_MSG);
    }

    @Override
    protected boolean isOnlyPlayer() {
        return false;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("morecompost.admin.about");
    }

}
