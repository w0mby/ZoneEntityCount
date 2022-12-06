package net.mobcount;

import java.util.List;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.mobcount.util.DataStorage;
import net.mobcount.util.PlayerData;
import net.mobcount.util.Zone;

public class MobCountService {
    public static int mobCount(PlayerEntity player, BlockPos blockPos, BlockPos blockPos2, String s) throws CommandSyntaxException {
		EntityType eType;
		World world = player.getWorld();
		if(s == null) s = "";
		switch(s.toUpperCase())
		{
			case "COW": eType = EntityType.COW; break;
			case "CHICKEN": eType = EntityType.CHICKEN; break;
			case "SHEEP": eType = EntityType.SHEEP; break;
			case "PIG": eType = EntityType.PIG; break;
			case "RABBIT": eType = EntityType.RABBIT; break;
			case "GOAT": eType = EntityType.GOAT; break;
			default: eType = null;
		}

		var blockLst = BlockPos.iterate(blockPos, blockPos2);

		int spawnableBlock = 0;
		int blocksQty = 0;
		int spawnedMobs = 0;
		List<Entity> collection = null;
		if(eType != null )
		{
			collection = world.getEntitiesByType(eType, new Box(blockPos.up().up().up(),blockPos2.down().down().down()), EntityPredicates.VALID_ENTITY);
		}
		else
		{
			collection = world.getEntitiesByClass(Entity.class,new Box(blockPos.up().up().up(),blockPos2.down().down().down()), EntityPredicates.VALID_LIVING_ENTITY);
		}
		
		for(BlockPos bpos : blockLst)
		{
			blocksQty++;
			var blockCheckSpawn = world.getBlockState(bpos);
			//var xx = world.getBlockState(bpos);
			//if(xx.allowsSpawning(world, bposup, entity.getType()))
			if(blockCheckSpawn.getBlock().canMobSpawnInside())
			{
				spawnableBlock++;
			}

			for(Entity entToTest : collection)
			{
				if(entToTest.getBlockX() == bpos.getX() && entToTest.getBlockZ() == bpos.getZ() && (entToTest.getBlockY() == bpos.getY() || entToTest.getBodyY(1) == bpos.getY()))
				{
					spawnedMobs++;
				}
			}
		}
		player.sendMessage(Text.literal(String.format("Zone: start: %d %d %d - end: %d %d %d",blockPos.getX(),blockPos.getY(),blockPos.getZ(),blockPos2.getX(),blockPos2.getY(),blockPos2.getZ())));
		player.sendMessage(Text.literal(String.format("Total blocks: %d",blocksQty)));
		player.sendMessage(Text.literal(String.format("Total spawnable blocks: %d",spawnableBlock)));
		player.sendMessage(Text.literal(String.format("Total %s(s) found: %d", (eType != null ?eType.getName().getString():"living entitie"),spawnedMobs)));
		if(spawnedMobs > 0)
		{
			float ratio = ((float)spawnableBlock/spawnedMobs);
			player.sendMessage(Text.literal(String.format("Entities on spawnable blocks ratio: %f",ratio)));
		}

		return 1;
	}

	public static int saveZone(CommandContext<FabricClientCommandSource> ctx,String zoneName,Vec3d pos1, Vec3d pos2, String mobType)
	{
		var self = ctx.getSource().getPlayer();
		PlayerData pd = DataStorage.getOfflinePlayerData(self);
		if(pd.zones.containsKey(zoneName)) 
		{
			self.sendMessage(Text.literal("that zone already exist. GoldFish memories..."));
			return 0;
		}
		pd.zones.put(zoneName, new Zone(pos1, pos2,mobType));
		DataStorage.saveOfflinePlayerData( pd );
		self.sendMessage(Text.literal("zone recorded. Wonderful! use /mobcount " + zoneName + " to use the saved zone."));
		return 1;
	}

	public static int mobCountZone(FabricClientCommandSource source, String zone) throws CommandSyntaxException {
		var self = source.getPlayer();
		PlayerData pd = DataStorage.getOfflinePlayerData(source.getPlayer());
		if(!pd.zones.containsKey(zone))
		{
			self.sendMessage(Text.literal("that zone does not exist. Make an effort..."));
			return 0;
		}
		Zone z = pd.zones.get(zone);
		return mobCount(self, new BlockPos(z.pos1()), 	new BlockPos(z.pos2()),z.mobType());
	}

	public static int delZone(FabricClientCommandSource source, String zone) throws CommandSyntaxException {
		var self = source.getPlayer();
		PlayerData pd = DataStorage.getOfflinePlayerData(source.getPlayer());
		if(!pd.zones.containsKey(zone))
		{
			self.sendMessage(Text.literal("that zone does not exist. Make an effort..."));
			return 0;
		}
		pd.zones.remove(zone);
		DataStorage.saveOfflinePlayerData(pd);
		self.sendMessage(Text.literal("Zone deleted. Goodbye old friend..."));
		return 1;
	}

	public static int listZones(FabricClientCommandSource source) {
		var player = source.getPlayer();
		PlayerData pd = DataStorage.getOfflinePlayerData(player);
		
		for (String entry : pd.zones.keySet()) {
			player.sendMessage(Text.literal("  " + entry));
		}
		return 1;
	}
}
