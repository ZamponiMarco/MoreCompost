package com.github.jummes.morecompost.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.drop.Drop;
import com.github.jummes.morecompost.droptable.DropTable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class DropPercentageCommand extends AbstractCommand {

    public DropPercentageCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        Player p = (Player) sender;
        DropTable table = MoreCompost.getInstance().getDropsManager().getHighestPriorityDropTable(p);
        double maxWeight = table.getSortedWeightSet().last();
        Map<Drop, Double> percentages = new HashMap<>();
        table.getDrops().forEach(drop -> percentages.put(drop, (drop.getWeight() / maxWeight) * 100));
        NumberFormat f = new DecimalFormat("#0.00");
        StringBuilder string = new StringBuilder();
        string.append(MessageUtils.header("&cDrops"));
        percentages.forEach((drop, d) -> string.append(MessageUtils
                .color(String.format("&6%s » &c&l%s%%\n", drop.getDropDescription().toString(), f.format(d)))));
        string.append(MessageUtils.delimiter("&cDrops"));
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
