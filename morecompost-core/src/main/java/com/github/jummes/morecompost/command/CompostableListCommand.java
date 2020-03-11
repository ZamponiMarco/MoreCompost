package com.github.jummes.morecompost.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.morecompost.compostabletable.CompostableTable;
import com.github.jummes.morecompost.core.MoreCompost;

public class CompostableListCommand extends AbstractCommand {

	public CompostableListCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
		super(sender, subCommand, arguments, isSenderPlayer);
	}

	@Override
	protected void execute() {
		Player p = (Player) sender;
		CompostableTable table = MoreCompost.getInstance().getCompostablesManager()
				.getHighestPriorityCompostableTable(p);
		StringBuilder string = new StringBuilder();
		string.append(MessageUtils.header("&cCompostables"));
		if (!table.isReplaceDefaultCompostables()) {
			string.append(MessageUtils.color("&6All the default compostables\n"));
		}
		table.getCompostables().stream().filter(compostable -> !compostable.isDefault())
				.forEach(compostable -> string.append(MessageUtils.color("&6" + compostable.toString() + "\n")));
		string.append(MessageUtils.delimiter("&cCompostables"));
		p.sendMessage(string.toString());
	}

	@Override
	protected boolean isOnlyPlayer() {
		return true;
	}

	@Override
	protected Permission getPermission() {
		return new Permission("morecompost.player.drops");
	}

}
