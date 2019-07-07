package com.github.jummes.morecompost.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.github.jummes.morecompost.core.MoreCompost;

public class ReloadCommand extends AbstractCommand {

	public ReloadCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
		super(sender, subCommand, arguments, isSenderPlayer);
	}

	@Override
	protected void execute() {
		
		MoreCompost.getInstance().getDropsManager().reloadData();
		MoreCompost.getInstance().getCompostablesManager().reloadData();
		sender.sendMessage("done");

	}

	@Override
	protected boolean isOnlyPlayer() {
		return false;
	}

	@Override
	protected Permission getPermission() {
		return new Permission("morecompost.commands.reload");
	}

}
