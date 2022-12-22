package net.mobcount.domain;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.mobcount.application.InteractKelpCallback;
import net.mobcount.application.MobCountRegistrationCallback;

public interface RegisterHandler {
    public void register();
}
