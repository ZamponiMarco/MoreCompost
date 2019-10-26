package com.github.jummes.morecompost.commands.factory;

import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;

import com.github.jummes.morecompost.commands.AbstractCommand;
import com.github.jummes.morecompost.commands.HelpCommand;

public class CommandFactory {

	public static AbstractCommand buildCommand(CommandSender sender, boolean isSenderPlayer, String subCommand,
			String[] arguments) {
		try {
			return (AbstractCommand) Class
					.forName("com.github.jummes.morecompost.commands." + WordUtils.capitalize(subCommand) + "Command")
					.getConstructor(CommandSender.class, String.class, String[].class, boolean.class)
					.newInstance(sender, subCommand, arguments, isSenderPlayer);
		} catch (Exception e) {
			return new HelpCommand(sender, subCommand, arguments, isSenderPlayer);
		}
	}

}
