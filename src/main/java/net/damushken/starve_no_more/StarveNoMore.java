package net.damushken.starve_no_more;

import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.command.StarveNoMoreCommand;
import net.damushken.starve_no_more.mixin.SpawnGroupAccessor;
import net.damushken.starve_no_more.util.MobCapManager;
import net.damushken.starve_no_more.util.SpawnBoost;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StarveNoMore implements ModInitializer {
	public static final String MOD_ID = "starve_no_more";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.



		((SpawnGroupAccessor)(Object) SpawnGroup.CREATURE).setCapacity(40); //default = 10

		((SpawnGroupAccessor)(Object) SpawnGroup.AXOLOTLS).setCapacity(10); //default = 5
		((SpawnGroupAccessor)(Object) SpawnGroup.WATER_CREATURE).setCapacity(20); //default = 5
		((SpawnGroupAccessor)(Object) SpawnGroup.WATER_AMBIENT).setCapacity(35); //default = 20



		SpawnBoost.register();
		MobCapManager.applyAll();

		CommandRegistrationCallback.EVENT
				.register((dispatcher, registryAccess, environment) ->
				StarveNoMoreCommand.register(dispatcher, registryAccess));
		ModConfig.get();

		//LOGGER.info("SpawnGroup.CREATURE capacity is now: {}", SpawnGroup.CREATURE.getCapacity());

	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
