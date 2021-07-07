package com.github.jummes.morecompost.listener;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.manager.CompostablesManager;
import com.github.jummes.morecompost.manager.DropsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

public class HopperInteractComposterListener implements Listener {

    private static final String METADATA_KEY = "forcedDropTableId";

    @EventHandler
    public void onHopperInteractComposter(InventoryMoveItemEvent e) {

        // Source is hopper block (hopper minecarts doesn't work with composters)
        if (e.getSource().getType().equals(InventoryType.HOPPER) && e.getSource().getHolder() instanceof Hopper) {

            Block hopperBlock = e.getSource().getLocation().getBlock();
            Directional hopper = (Directional) hopperBlock.getBlockData();
            Block destination = hopperBlock.getLocation().clone()
                    .add(Faces.valueOf(hopper.getFacing().name()).getVector()).getBlock();

            // Destination is composter
            if (destination.getType().equals(Material.COMPOSTER)) {
                hopperCompostBlock(e, destination);
            }
        }

        // Destination is hopper
        else if (e.getDestination().getType().equals(InventoryType.HOPPER)) {
            Block hopperBlock = e.getDestination().getLocation().getBlock();
            Block source = hopperBlock.getLocation().clone().add(0, 1, 0).getBlock();

            // Source is composter
            if (source.getType().equals(Material.COMPOSTER)) {
                dropItemToHopper(e, source, hopperBlock);
            }
        }

    }

    private void hopperCompostBlock(InventoryMoveItemEvent e, Block destination) {
        CompostablesManager compostablesManager = MoreCompost.getInstance().getCompostablesManager();
        if (compostablesManager.getHighestPriorityCompostableTable(getOwner(destination)).compost(destination,
                e.getItem())) {
            ItemStack toSet = e.getItem();
            int amount = e.getItem().getAmount();
            toSet.setAmount(amount - 1);
            e.setItem(toSet);
        } else {
            e.setCancelled(true);
        }
    }

    private void dropItemToHopper(InventoryMoveItemEvent e, Block source, Block destination) {
        DropsManager dropsManager = MoreCompost.getInstance().getDropsManager();

        if (source.hasMetadata(METADATA_KEY)) {
            dropsManager.getDropTableById(source.getMetadata(METADATA_KEY).get(0).asString())
                    .fillContainer(destination);
            source.removeMetadata(METADATA_KEY, MoreCompost.getInstance());
        } else {
            dropsManager.getHighestPriorityDropTable(getOwner(source)).fillContainer(destination);
        }

        e.setItem(new ItemStack(Material.AIR));

        // Just to be sure
        Levelled levelled = (Levelled) source.getBlockData();
        levelled.setLevel(0);
        source.setBlockData(levelled);
    }

    /**
     * Returns the owner of this composter, returns null if composter has no owner
     *
     * @param b composter to analize
     * @return owner of the composter, null if he hasn't one
     */
    private Player getOwner(Block b) {
        try {
            return Bukkit.getOfflinePlayer((UUID) b.getMetadata("owner").stream()
                    .filter(metadata -> metadata.getOwningPlugin() instanceof MoreCompost).findFirst().get().value())
                    .getPlayer();
        } catch (Exception e) {
            return null;
        }
    }

    private enum Faces {

        NORTH(0, 0, -1), SOUTH(0, 0, 1), EAST(1, 0, 0), WEST(-1, 0, 0), UP(0, 1, 0), DOWN(0, -1, 0);

        private Vector vector;

        private Faces(int x, int y, int z) {
            vector = new Vector(x, y, z);
        }

        public Vector getVector() {
            return vector;
        }

    }

}
