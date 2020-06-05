package com.github.jummes.morecompost.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.gui.model.ModelCollectionInventoryHolder;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.droptable.DropTable;

public class DropsCommand extends AbstractCommand {

    public DropsCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        Player p = (Player) sender;
        try {
            p.openInventory(new ModelCollectionInventoryHolder(MoreCompost.getInstance(), null,
                    new ModelPath<>(MoreCompost.getInstance().getDropsManager(), null),
                    MoreCompost.getInstance().getDropsManager().getClass().getDeclaredField("dropTables"), 1)
                    .getInventory());
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
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
