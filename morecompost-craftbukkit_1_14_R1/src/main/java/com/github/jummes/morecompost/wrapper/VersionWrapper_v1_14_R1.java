package com.github.jummes.morecompost.wrapper;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.wrapper.VersionWrapper;

import net.minecraft.server.v1_14_R1.EntityItem;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagList;

public class VersionWrapper_v1_14_R1 implements VersionWrapper {

	private static final String CUSTOM_KEY = "custom_drop";

	@Override
	public void dropKeyedItem(Location location, ItemStack item) {
		CraftWorld craftWorld = ((CraftWorld) location.getWorld());
		Random r = new Random();

		EntityItem craftItem = new EntityItem(EntityTypes.ITEM, craftWorld.getHandle());
		craftItem.setItemStack(CraftItemStack.asNMSCopy(item));
		CraftEntity.getEntity((CraftServer) Bukkit.getServer(), craftItem).setMetadata(CUSTOM_KEY,
				new FixedMetadataValue(MoreCompost.getInstance(), true));
		craftItem.setPosition(location.getX() + .35 + r.nextDouble() * .3, location.getY() + 1,
				location.getZ() + .35 + r.nextDouble() * .3);
		craftItem.setMot(r.nextDouble() * .2 - .1, r.nextDouble() * .2, r.nextDouble() * .2 - .1);

		craftWorld.addEntity(craftItem, SpawnReason.CUSTOM);
	}

	@Override
	public ItemStack skullFromValue(String value) {
		UUID id = new UUID(value.hashCode(), value.hashCode());
		net.minecraft.server.v1_14_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(new ItemStack(Material.PLAYER_HEAD));
		NBTTagCompound tag = nmsItem.getOrCreateTag();
		NBTTagCompound skullOwner = new NBTTagCompound();
		NBTTagCompound texturesCompound = new NBTTagCompound();
		NBTTagList textures = new NBTTagList();
		NBTTagCompound textureValue = new NBTTagCompound();
		textureValue.setString("Value", value);
		textures.add(textureValue);
		texturesCompound.set("textures", textures);
		skullOwner.set("Properties", texturesCompound);
		skullOwner.setString("Id", id.toString());
		tag.set("SkullOwner", skullOwner);
		nmsItem.setTag(tag);
		return CraftItemStack.asBukkitCopy(nmsItem);
	}

}
