package net.mobcount.application;

import com.google.inject.ImplementedBy;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@ImplementedBy(MobCountServiceImpl.class)
public interface MobCountService {

	
    public int mobCount(PlayerEntity player, BlockPos blockPos, BlockPos blockPos2, String s) throws CommandSyntaxException;
	public int saveZone(ClientPlayerEntity player,String zoneName,Vec3d pos1, Vec3d pos2, String mobType);
	public int mobCountZone(ClientPlayerEntity player, String zoneName) throws CommandSyntaxException;

	public int delZone(ClientPlayerEntity player, String zoneName) throws CommandSyntaxException ;

	public int listZones(ClientPlayerEntity player) ;
}
