package net.damushken.starve_no_more.util;

import net.damushken.starve_no_more.StarveNoMore;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.SpawnSettings;

public class SpawnBoost {

    public static void register() {
        BiomeModifications.create(new Identifier(StarveNoMore.MOD_ID, "boost_creature_spawns"))
                .add(ModificationPhase.ADDITIONS,
                        BiomeSelectors.includeByKey(

                                BiomeKeys.OLD_GROWTH_BIRCH_FOREST,
                                BiomeKeys.BIRCH_FOREST,
                                BiomeKeys.DARK_FOREST,
                                BiomeKeys.FLOWER_FOREST,
                                BiomeKeys.WINDSWEPT_FOREST,
                                BiomeKeys.WINDSWEPT_HILLS,
                                BiomeKeys.WINDSWEPT_GRAVELLY_HILLS,
                                BiomeKeys.FOREST,
                                BiomeKeys.PLAINS,
                                BiomeKeys.BADLANDS,
                                BiomeKeys.ERODED_BADLANDS,
                                BiomeKeys.SUNFLOWER_PLAINS,
                                BiomeKeys.WOODED_BADLANDS,
                                BiomeKeys.SWAMP,
                                BiomeKeys.WINDSWEPT_SAVANNA,
                                BiomeKeys.SAVANNA,
                                BiomeKeys.SPARSE_JUNGLE,
                                BiomeKeys.TAIGA,
                                BiomeKeys.SNOWY_TAIGA,
                                BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                                BiomeKeys.OLD_GROWTH_PINE_TAIGA,
                                BiomeKeys.SAVANNA_PLATEAU,
                                BiomeKeys.JUNGLE,
                                BiomeKeys.BAMBOO_JUNGLE),

                        biomeModificationContext -> {
                            var spawnSettings = biomeModificationContext.getSpawnSettings();

                            // Boost group size for specific passive mobs.
                            // Must remove + re-add since entries are immutable.

                            spawnSettings.removeSpawnsOfEntityType(EntityType.COW);
                            spawnSettings.addSpawn(SpawnGroup.CREATURE,
                                    new SpawnSettings.SpawnEntry(EntityType.COW, 1, 2, 5));

                            spawnSettings.removeSpawnsOfEntityType(EntityType.SHEEP);
                            spawnSettings.addSpawn(SpawnGroup.CREATURE,
                                    new SpawnSettings.SpawnEntry(EntityType.SHEEP, 1, 2, 5));

                            spawnSettings.removeSpawnsOfEntityType(EntityType.CHICKEN);
                            spawnSettings.addSpawn(SpawnGroup.CREATURE,
                                    new SpawnSettings.SpawnEntry(EntityType.CHICKEN, 1, 2, 5));

                            spawnSettings.removeSpawnsOfEntityType(EntityType.PIG);
                            spawnSettings.addSpawn(SpawnGroup.CREATURE,
                                    new SpawnSettings.SpawnEntry(EntityType.PIG, 1, 2, 5));
                        });
    }
}
