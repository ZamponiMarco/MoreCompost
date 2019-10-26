package com.github.jummes.morecompost.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.github.jummes.morecompost.gui.droptables.DropTablesListInventoryHolder;
import com.github.jummes.morecompost.utils.MessageUtils;

public class DropsCommand extends AbstractCommand {

	private static final String INV_TITLE = MessageUtils.color("&6Drop Tables");
	
	public DropsCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
		super(sender, subCommand, arguments, isSenderPlayer);
	}

	@Override
	protected void execute() {
		Player player = (Player) sender;
		player.openInventory(new DropTablesListInventoryHolder(INV_TITLE, 1).getInventory());
	}

	@Override
	protected boolean isOnlyPlayer() {
		return true;
	}

	@Override
	protected Permission getPermission() {
		return new Permission("morecompost.commands.drops");
	}

}
