package com.github.jummes.morecompost.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.gui.compostabletables.CompostableTablesListInventoryHolder;
import com.github.jummes.morecompost.utils.MessageUtils;
import com.google.common.collect.Lists;

public class CompostablesCommand extends AbstractCommand {

	private static final String INV_TITLE = MessageUtils.color("&6Compostable Tables");

	public CompostablesCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
		super(sender, subCommand, arguments, isSenderPlayer);
	}

	@Override
	protected void execute() {
		Player player = (Player) sender;
		player.openInventory(new CompostableTablesListInventoryHolder(INV_TITLE,
				Lists.newArrayList(MoreCompost.getInstance().getCompostablesManager().getCompostables().values()), 1)
						.getInventory());

	}

	@Override
	protected boolean isOnlyPlayer() {
		return true;
	}

	@Override
	protected Permission getPermission() {
		return new Permission("morecompost.commands.compostables");
	}

}
