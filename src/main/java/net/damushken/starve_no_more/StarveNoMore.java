package net.damushken.starve_no_more;

import net.damushken.starve_no_more.mixin.SpawnGroupAccessor;
import net.fabricmc.api.ModInitializer;

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

		((SpawnGroupAccessor)(Object) SpawnGroup.CREATURE).setCapacity(35); //default = 10

		((SpawnGroupAccessor)(Object) SpawnGroup.AXOLOTLS).setCapacity(10); //default = 5
		((SpawnGroupAccessor)(Object) SpawnGroup.WATER_CREATURE).setCapacity(15); //default = 5
		((SpawnGroupAccessor)(Object) SpawnGroup.WATER_AMBIENT).setCapacity(30); //default = 20

		//LOGGER.info("SpawnGroup.CREATURE capacity is now: {}", SpawnGroup.CREATURE.getCapacity());

	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
