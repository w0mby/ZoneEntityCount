package net.mobcount.application;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.inject.Inject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.context.StringRange;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MobCountRegistrationCallback implements ClientCommandRegistrationCallback {

	private MobCountService mobCountService;
	@Inject
	public MobCountRegistrationCallback(MobCountService mobCountService)
	{
		this.mobCountService = mobCountService;
	}

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		dispatcher.register(
			ClientCommandManager.literal("mobcount")
			.then(ClientCommandManager.argument("pos1x",DoubleArgumentType.doubleArg()).then(ClientCommandManager.argument("pos1z",DoubleArgumentType.doubleArg()).then(ClientCommandManager.argument("pos1y",DoubleArgumentType.doubleArg())
			
				.then(ClientCommandManager.argument("pos2x",DoubleArgumentType.doubleArg()).then(ClientCommandManager.argument("pos2z",DoubleArgumentType.doubleArg()).then(ClientCommandManager.argument("pos2y",DoubleArgumentType.doubleArg())
					.then(ClientCommandManager.argument("mobToCount", StringArgumentType.word())
						.executes(
							ctx -> mobCountService.mobCount(
								ctx.getSource().getPlayer(),
								new BlockPos(DoubleArgumentType.getDouble(ctx, "pos1x"),DoubleArgumentType.getDouble(ctx, "pos1z"),DoubleArgumentType.getDouble(ctx, "pos1y")) ,
								new BlockPos(DoubleArgumentType.getDouble(ctx, "pos2x"),DoubleArgumentType.getDouble(ctx, "pos2z"),DoubleArgumentType.getDouble(ctx, "pos2y")),
								captureLastArgument(ctx)
							)
						)
						.then(ClientCommandManager.literal("set")
						.then(ClientCommandManager.argument("ZoneName", StringArgumentType.word())
							.executes(
								ctx -> mobCountService.saveZone(ctx.getSource().getPlayer(),
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
						ctx -> mobCountService.mobCountZone(
							ctx.getSource().getPlayer(),
							StringArgumentType.getString(ctx, "ZoneName")
						)
					)
				)
			)
			.then(ClientCommandManager.literal("del")
				.then(ClientCommandManager.argument("ZoneName", StringArgumentType.word())
					.executes(
						ctx -> mobCountService.delZone(
							ctx.getSource().getPlayer(),
							StringArgumentType.getString(ctx, "ZoneName")
						)
					)
				)
			)
            .then(ClientCommandManager.literal("list")
				
					.executes(
						ctx -> mobCountService.listZones(
							ctx.getSource().getPlayer()
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
