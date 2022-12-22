package net.mobcount.application.util;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.mobcount.infrastructure.ConfigManager;

public class RenderHelper {

    public static final int RED =  color(128,255,0,0);
    public static final int ORANGE = color(128,255,165,0);
    public static final int GREEN = color(128,0,255,0);

    public static void renderBlockOverlay(Block b, int color)
    {
        ColorProviderRegistry.BLOCK.register((state, world, pos, index) -> {
            // Return the red translucent color you want the block to be here
            if(ConfigManager.MustDrawOverlay){
                return color;
            }
            return 0xffffffff;
          }, b);
    }

    public static int color(int a, int r, int g, int b) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}
    
}
