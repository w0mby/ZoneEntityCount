package net.mobcount;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.mobcount.events.InteractKelpCallback;
import net.mobcount.events.MobCountRegistrationCallback;

public class RegisterHandler {
    public static void register()
    {
        //Regiister the mobCount Command.
        ClientCommandRegistrationCallback.EVENT.register(new MobCountRegistrationCallback());
		    // Register a new event listener for the RIGHT_CLICK_BLOCK event
		UseBlockCallback.EVENT.register(new InteractKelpCallback());
    }
}
