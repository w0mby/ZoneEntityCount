package net.mobcount;

import static net.minecraft.server.command.CommandManager.literal;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Material;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class MobCount implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("mobcount");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
	}

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) { // You can also return a LiteralCommandNode for use with possible redirects
		dispatcher.register(
			literal("mobcount")
			.then(argument("pos1", BlockPosArgumentType.blockPos())
				.then(argument("pos2", BlockPosArgumentType.blockPos())
					.then(argument("mobToCount", EntityArgumentType.entities())
						.executes(
							ctx -> mobCount(
								ctx.getSource(),
								BlockPosArgumentType.getBlockPos(ctx, "pos1"),
								BlockPosArgumentType.getBlockPos(ctx, "pos2"),
								EntityArgumentType.getEntities(ctx, "mobToCount")
							)
						)
					)
				)
			)
		);
	}

	public static int mobCount(ServerCommandSource source, BlockPos blockPos, BlockPos blockPos2, Collection<? extends Entity> collection) throws CommandSyntaxException {
		final PlayerEntity self = source.getPlayer(); // If not a player than the command ends
		if(collection.size()==0) return 0;
		Entity entity = (Entity)collection.toArray()[0];
		var blockLst = BlockPos.iterate(blockPos, blockPos2);
		Box b = new Box(blockPos,blockPos2);
		var world = source.getWorld();
		int spawnableBlock = 0;
		int blocksQty = 0;
		int spawnedMobs = 0;
		for(BlockPos bpos : blockLst)
		{
			blocksQty++;
			var bposup = bpos.up();
			var x = world.getBlockState(bposup);
			if(x.getBlock().canMobSpawnInside())
			{
				spawnableBlock++;
			}
			for(Entity entToTest : collection)
			{
					//var entityList = world.getEntitiesByType(entity.getType(), new Box(blockPos, blockPos2),EntityPredicates.VALID_LIVING_ENTITY);
					if(entToTest.getBlockX() == bpos.getX() && entToTest.getBlockZ() == bpos.getZ() && (entToTest.getBlockY() == bposup.getY() || entToTest.getBodyY(1) == bposup.getY()))
					{
						spawnedMobs++;
					}
			}
		}

		self.sendMessage(Text.literal(String.format("Total blocks: %d",blocksQty)));
		self.sendMessage(Text.literal(String.format("Total spawnable blocks: %d",spawnableBlock)));

		
		self.sendMessage(Text.literal(String.format("Total %s found: %d",entity.getDisplayName(),spawnedMobs)));
		if(spawnedMobs > 0)
		{
			float ratio = ((float)spawnableBlock/spawnedMobs);
			self.sendMessage(Text.literal(String.format("Entity on block ratio: %f",ratio)));
		}

		return 1;
	}
}
