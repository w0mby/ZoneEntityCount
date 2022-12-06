package net.mobcount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.mobcount.events.RegisterHandler;

public class MobCount implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("mobcount");

	@Override
	public void onInitialize() {
		RegisterHandler.register();
	}
}
