package com.github.jummes.morecompost.command;

import com.github.jummes.morecompost.compostabletable.CompostableTable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.gui.model.ModelCollectionInventoryHolder;
import com.github.jummes.morecompost.core.MoreCompost;

public class CompostablesCommand extends AbstractCommand {

    public CompostablesCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        Player p = (Player) sender;
        try {
            p.openInventory(new ModelCollectionInventoryHolder<>(MoreCompost.getInstance(),
                    MoreCompost.getInstance().getCompostablesManager(), "compostableTables").getInventory());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
