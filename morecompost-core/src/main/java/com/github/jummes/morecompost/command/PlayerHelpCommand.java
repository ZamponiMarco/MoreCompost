package com.github.jummes.morecompost.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.util.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class PlayerHelpCommand extends AbstractCommand {

    private final static String HELP_MSG = MessageUtils.header("MoreCompost Help")
            + MessageUtils.color("&2/composter help &7Print the help message.\n"
            + "&2/composter drops &7Show the drops and their percentage.\n"
            + "&2/composter compostables &7Show a list of custom compostables.\n")
            + MessageUtils.delimiter("MoreCompost Help");

    public PlayerHelpCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        sender.sendMessage(HELP_MSG);
    }

    @Override
    protected boolean isOnlyPlayer() {
        return false;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("morecompost.player.help");
    }

}
