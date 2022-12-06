package net.mobcount.events;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.mobcount.MobCountService;
import net.mobcount.util.DataStorage;

public class InteractKelpCallback implements UseBlockCallback {

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if(!world.isClient) return ActionResult.PASS;
        if (hand == Hand.MAIN_HAND && player.getStackInHand(hand).getItem() == Items.KELP) {
            BlockPos blockPos = hitResult.getBlockPos();
            int x = blockPos.getX();
            int y = blockPos.getY();
            int z = blockPos.getZ();
            player.sendMessage(Text.literal("The coordinates are x: " + x + " y: " + y + " z: " + z), true);

            if(DataStorage.currentPos1 == null)
            {
                DataStorage.currentPos1 = new Vec3d(x,y,z);
                player.sendMessage(Text.literal("start at x: " + x + " y: " + y + " z: " + z + ". Define end pos."), true);
                DataStorage.currentPos2 = null;
            }
            else
            {
                DataStorage.currentPos2 = new Vec3d(x,y,z);
                player.sendMessage(Text.literal("end at x: " + x + " y: " + y + " z: " + z), true);
                try {
                    MobCountService.mobCount(player, new BlockPos(DataStorage.currentPos1), new BlockPos(DataStorage.currentPos2), null);
                    DataStorage.currentPos1 = DataStorage.currentPos2 = null;
                    return ActionResult.SUCCESS;    
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            }

        }
        return ActionResult.PASS;
    }

}