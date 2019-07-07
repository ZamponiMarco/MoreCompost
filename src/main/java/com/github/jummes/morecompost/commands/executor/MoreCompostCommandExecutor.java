package com.github.jummes.morecompost.commands.executor;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.jummes.morecompost.commands.factory.CommandFactory;

public class MoreCompostCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean isSenderPlayer = sender instanceof Player;
		String subCommand = args.length >= 1 ? args[0] : "";
		String[] arguments = args.length >= 2 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
		CommandFactory.buildCommand(sender, isSenderPlayer, subCommand, arguments).checkExecution();
		return true;
	}

}
