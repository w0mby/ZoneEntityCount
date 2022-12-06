package net.mobcount.events;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.mobcount.CommandHandler;

public class RegisterHandler {
    public static void register()
    {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> CommandHandler.register(dispatcher));
		    // Register a new event listener for the RIGHT_CLICK_BLOCK event
		UseBlockCallback.EVENT.register(new InteractKelpCallback());
    }
}
