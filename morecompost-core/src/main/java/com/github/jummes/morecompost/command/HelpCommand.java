package com.github.jummes.morecompost.command;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.util.MessageUtils;

public class HelpCommand extends AbstractCommand {

	private final static String HELP_MSG = MessageUtils.header("MoreCompost Help")
			+ MessageUtils.color("&2/mc help &7Print the help message.\n"
					+ "&2/mc reload &7Reload configuration files.\n" + "&2/mc inspect &7Inspect a composter\n"
					+ "&2/mc drops &7Open drops menu.\n" + "&2/mc compostables &7Open compostables menu.")
			+ MessageUtils.delimiter();

	public HelpCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
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
		return new Permission("morecompost.commands.help");
	}

}
