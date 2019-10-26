package com.github.jummes.morecompost.commands.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.github.jummes.morecompost.commands.factory.CommandFactory;
import com.google.common.collect.Lists;

public class MoreCompostCommandExecutor implements CommandExecutor, TabCompleter {

	private static final List<String> SUBTYPES = Lists.newArrayList("drops", "compostables", "help", "reload",
			"inspect");

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean isSenderPlayer = sender instanceof Player;
		String subCommand = args.length >= 1 ? args[0] : "";
		String[] arguments = args.length >= 2 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
		CommandFactory.buildCommand(sender, isSenderPlayer, subCommand, arguments).checkExecution();
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		final List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], SUBTYPES, completions);
		}
		Collections.sort(completions);
		return completions;
	}

}
