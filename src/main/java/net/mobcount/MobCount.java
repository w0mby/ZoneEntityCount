package net.mobcount;

import static net.minecraft.server.command.CommandManager.literal;

import java.util.Collection;
import java.util.List;

import javax.print.DocFlavor;

import static net.minecraft.server.command.CommandManager.argument;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Material;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.TypeFilter;
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

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> register(dispatcher));
	}

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) { // You can also return a LiteralCommandNode for use with possible redirects
		
		dispatcher.register(
			ClientCommandManager.literal("mobcount")
			.then(ClientCommandManager.argument("pos1x",DoubleArgumentType.doubleArg()).then(ClientCommandManager.argument("pos1z",DoubleArgumentType.doubleArg()).then(ClientCommandManager.argument("pos1y",DoubleArgumentType.doubleArg())
			
				.then(ClientCommandManager.argument("pos2x",DoubleArgumentType.doubleArg()).then(ClientCommandManager.argument("pos2z",DoubleArgumentType.doubleArg()).then(ClientCommandManager.argument("pos2y",DoubleArgumentType.doubleArg())
					.then(ClientCommandManager.argument("mobToCount", EntityArgumentType.entities())
						.executes(
							ctx -> mobCount(
								ctx.getSource(),
								new BlockPos(DoubleArgumentType.getDouble(ctx, "pos1x"),DoubleArgumentType.getDouble(ctx, "pos1z"),DoubleArgumentType.getDouble(ctx, "pos1y")) ,
								new BlockPos(DoubleArgumentType.getDouble(ctx, "pos2x"),DoubleArgumentType.getDouble(ctx, "pos2z"),DoubleArgumentType.getDouble(ctx, "pos2y")),
								captureLastArgument(ctx)
							)
						)
					)
				)))
			)))
		);
	}

	private static @NotNull String captureLastArgument(@NotNull CommandContext<FabricClientCommandSource> source) {
        List<ParsedCommandNode<FabricClientCommandSource>> nodes = source.getNodes();
        StringRange node = nodes.get(nodes.size() - 1).getRange();
        return source.getInput().substring(node.getStart(), node.getEnd());
    }
//Collection<? extends Entity> collection
	public static int mobCount(FabricClientCommandSource source, BlockPos blockPos, BlockPos blockPos2, String s) throws CommandSyntaxException {
		EntityType eType;
		switch(s.toUpperCase())
		{
			case "COW": eType = EntityType.COW; break;
			case "CHICKEN": eType = EntityType.CHICKEN; break;
			case "SHEEP": eType = EntityType.SHEEP; break;
			case "PIG": eType = EntityType.PIG; break;
			case "RABBIT": eType = EntityType.RABBIT; break;
			case "GOAT": eType = EntityType.GOAT; break;
			default: eType = EntityType.PLAYER;
		}
		final PlayerEntity self = source.getPlayer(); // If not a player than the command ends
		//Entity entity = null;//(Entity)s.toArray()[0];
		var blockLst = BlockPos.iterate(blockPos, blockPos2);
		var world = source.getWorld();
		int spawnableBlock = 0;
		int blocksQty = 0;
		int spawnedMobs = 0;
		List<Entity> collection = world.getEntitiesByType(eType, new Box(blockPos.up().up().up(),blockPos2.down().down().down()), EntityPredicates.VALID_ENTITY);
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
		self.sendMessage(Text.literal(String.format("Total blocks: %d",blocksQty)));
		self.sendMessage(Text.literal(String.format("Total spawnable blocks: %d",spawnableBlock)));
		self.sendMessage(Text.literal(String.format("Total %s(s) found: %d", eType.getName().getString(),spawnedMobs)));
		if(spawnedMobs > 0)
		{
			float ratio = ((float)spawnableBlock/spawnedMobs);
			self.sendMessage(Text.literal(String.format("Entities on spawnable blocks ratio: %f",ratio)));
		}

		return 1;
	}
}
