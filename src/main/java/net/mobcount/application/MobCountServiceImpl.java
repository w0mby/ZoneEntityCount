package net.mobcount.application;

import java.util.List;

import com.google.inject.Inject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;


import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.mobcount.application.util.RenderHelper;
import net.mobcount.domain.entities.PlayerData;
import net.mobcount.domain.entities.Zone;
import net.mobcount.infrastructure.ConfigManager;

public class MobCountServiceImpl implements MobCountService {


	private PlayerDataRepositoryImpl playerDataRepository;

	@Inject
	public MobCountServiceImpl(PlayerDataRepositoryImpl playerDataRepository)
	{
		this.playerDataRepository = playerDataRepository;
	}

	public int mobCount(PlayerEntity player, BlockPos blockPos, BlockPos blockPos2, String s) throws CommandSyntaxException {
		EntityType eType = null;
		try
		{
			eType = EntityType.get(s.toLowerCase()).get();
		}
		catch(Exception e)
{} 
		World world = player.getWorld();


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
			if(ConfigManager.MustDrawOverlay)
			{
				RenderHelper.renderBlockOverlay(world.getBlockState(bpos).getBlock(), RenderHelper.RED);
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

	public int saveZone(ClientPlayerEntity player,String zoneName,Vec3d pos1, Vec3d pos2, String mobType)
	{
		var server = player.getServer();
		PlayerData pd = playerDataRepository.get(player.getUuid(),server == null?"":server.getName());
		Zone existingZone = pd.getZone(zoneName);
		if(existingZone != null) 
		{
			player.sendMessage(Text.literal("that zone already exist. GoldFish memories..."));
			return 0;
		}
		pd.addZone(zoneName, new Zone(pos1, pos2,mobType));
		boolean isSaved = playerDataRepository.update(pd);
		if(isSaved){
			player.sendMessage(Text.literal("zone recorded. Wonderful! use /mobcount " + zoneName + " to use the saved zone."));
			return 1;
		}
		else
		{
			player.sendMessage(Text.literal("UhOh, we have a problem to save the zone. Check the logs you should fin the culprit."));
			return 0;
		}
	}

	public int mobCountZone(ClientPlayerEntity player, String zoneName) throws CommandSyntaxException {
		var server = player.getServer();
		PlayerData pd = playerDataRepository.get(player.getUuid(),server == null?"":server.getName());
		Zone zone = pd.getZone(zoneName);
		if(zone == null)
		{
			player.sendMessage(Text.literal("that zone does not exist. Make an effort..."));
		}
		return mobCount(player, new BlockPos(zone.pos1()), new BlockPos(zone.pos2()),zone.mobType());
	}

	public int delZone(ClientPlayerEntity player, String zoneName) throws CommandSyntaxException {
		var server = player.getServer();
		PlayerData playerData = playerDataRepository.get(player.getUuid(),server == null?"":server.getName());
		Zone zone = playerData.getZone(zoneName);
		if(zone == null)
		{
			player.sendMessage(Text.literal("that zone does not exist. Make an effort..."));
			return 0;
		}
		playerData.removeZone(zoneName);
		boolean isSaved = playerDataRepository.update(playerData);
		player.sendMessage(Text.literal("Zone deleted. Goodbye old friend..."));
		if(isSaved){
			player.sendMessage(Text.literal("Zone deleted. Goodbye old friend..."));
			return 1;
		}
		else
		{
			player.sendMessage(Text.literal("UhOh, we have a problem to delete the zone. Check the logs you should fin the culprit."));
			return 0;
		}
	}

	public int listZones(ClientPlayerEntity player) {
		var server = player.getServer();
		PlayerData pd = playerDataRepository.get(player.getUuid(),server == null?"":server.getName());
		
		for (String entry : pd.getZones().keySet()) {
			player.sendMessage(Text.literal("  " + entry));
		}
		return 1;
	}
}
