package net.mobcount.domain;

import com.google.inject.Inject;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.mobcount.application.InteractKelpCallback;
import net.mobcount.application.MobCountRegistrationCallback;

@Implementation
public class GeneralRegisterHandler implements RegisterHandler {

    private MobCountRegistrationCallback mobCountRegistrationCallback;
    private InteractKelpCallback interactKelpCallback;

    @Inject
    public GeneralRegisterHandler(MobCountRegistrationCallback mobCountRegistrationCallback, InteractKelpCallback interactKelpCallback)
    {
        this.interactKelpCallback = interactKelpCallback;
        this.mobCountRegistrationCallback = mobCountRegistrationCallback;
    }
    public void register()
    {
        //Register the mobCount Command.
        ClientCommandRegistrationCallback.EVENT.register(mobCountRegistrationCallback);
		    // Register a new event listener for the RIGHT_CLICK_BLOCK event
		UseBlockCallback.EVENT.register(interactKelpCallback);
    }
}
