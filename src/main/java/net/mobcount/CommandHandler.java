package net.mobcount;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.context.StringRange;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CommandHandler {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) { // You can also return a LiteralCommandNode for use with possible redirects
		
		dispatcher.register(
			ClientCommandManager.literal("mobcount")
			.then(ClientCommandManager.argument("pos1x",DoubleArgumentType.doubleArg()).then(ClientCommandManager.argument("pos1z",DoubleArgumentType.doubleArg()).then(ClientCommandManager.argument("pos1y",DoubleArgumentType.doubleArg())
			
				.then(ClientCommandManager.argument("pos2x",DoubleArgumentType.doubleArg()).then(ClientCommandManager.argument("pos2z",DoubleArgumentType.doubleArg()).then(ClientCommandManager.argument("pos2y",DoubleArgumentType.doubleArg())
					.then(ClientCommandManager.argument("mobToCount", StringArgumentType.word())
						.executes(
							ctx -> MobCountService.mobCount(
								ctx.getSource().getPlayer(),
								new BlockPos(DoubleArgumentType.getDouble(ctx, "pos1x"),DoubleArgumentType.getDouble(ctx, "pos1z"),DoubleArgumentType.getDouble(ctx, "pos1y")) ,
								new BlockPos(DoubleArgumentType.getDouble(ctx, "pos2x"),DoubleArgumentType.getDouble(ctx, "pos2z"),DoubleArgumentType.getDouble(ctx, "pos2y")),
								captureLastArgument(ctx)
							)
						)
						.then(ClientCommandManager.literal("set")
						.then(ClientCommandManager.argument("ZoneName", StringArgumentType.word())
							.executes(
								ctx -> MobCountService.saveZone(ctx,
									StringArgumentType.getString(ctx, "ZoneName"),
									new Vec3d(DoubleArgumentType.getDouble(ctx, "pos1x"),DoubleArgumentType.getDouble(ctx, "pos1z"),DoubleArgumentType.getDouble(ctx, "pos1y")),
									new Vec3d(DoubleArgumentType.getDouble(ctx, "pos2x"),DoubleArgumentType.getDouble(ctx, "pos2z"),DoubleArgumentType.getDouble(ctx, "pos2y")),
									StringArgumentType.getString(ctx, "mobToCount"))
								)
							)
						)

					)
				)))
			)))
			.then(ClientCommandManager.literal("zone")
				.then(ClientCommandManager.argument("ZoneName", StringArgumentType.word())
					.executes(
						ctx -> MobCountService.mobCountZone(
							ctx.getSource(),
							StringArgumentType.getString(ctx, "ZoneName")
						)
					)
				)
			)
			.then(ClientCommandManager.literal("del")
				.then(ClientCommandManager.argument("ZoneName", StringArgumentType.word())
					.executes(
						ctx -> MobCountService.delZone(
							ctx.getSource(),
							StringArgumentType.getString(ctx, "ZoneName")
						)
					)
				)
			)
            .then(ClientCommandManager.literal("list")
				
					.executes(
						ctx -> MobCountService.listZones(
							ctx.getSource()
						)
					)
			)
		);
	}

	private static @NotNull String captureLastArgument(@NotNull CommandContext<FabricClientCommandSource> source) {
        List<ParsedCommandNode<FabricClientCommandSource>> nodes = source.getNodes();
        StringRange node = nodes.get(nodes.size() - 1).getRange();
        return source.getInput().substring(node.getStart(), node.getEnd());
    }
}
