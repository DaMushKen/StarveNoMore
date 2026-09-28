package net.damushken.starve_no_more.util;

import net.damushken.starve_no_more.StarveNoMore;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class SpawnBoost {

    public static void register() {
        BiomeModifications.create(Identifier.fromNamespaceAndPath(StarveNoMore.MOD_ID, "boost_creature_spawns"))
                .add(ModificationPhase.ADDITIONS,
                        BiomeSelectors.includeByKey(

                                Biomes.OLD_GROWTH_BIRCH_FOREST,
                                Biomes.BIRCH_FOREST,
                                Biomes.DARK_FOREST,
                                Biomes.FLOWER_FOREST,
                                Biomes.WINDSWEPT_FOREST,
                                Biomes.WINDSWEPT_HILLS,
                                Biomes.WINDSWEPT_GRAVELLY_HILLS,
                                Biomes.FOREST,
                                Biomes.PLAINS,
                                Biomes.BADLANDS,
                                Biomes.ERODED_BADLANDS,
                                Biomes.SUNFLOWER_PLAINS,
                                Biomes.WOODED_BADLANDS,
                                Biomes.SWAMP,
                                Biomes.WINDSWEPT_SAVANNA,
                                Biomes.SAVANNA,
                                Biomes.SPARSE_JUNGLE,
                                Biomes.TAIGA,
                                Biomes.SNOWY_TAIGA,
                                Biomes.OLD_GROWTH_SPRUCE_TAIGA,
                                Biomes.OLD_GROWTH_PINE_TAIGA,
                                Biomes.SAVANNA_PLATEAU,
                                Biomes.JUNGLE,
                                Biomes.BAMBOO_JUNGLE),

                        biomeModificationContext -> {
                            var spawnSettings = biomeModificationContext.getMobSpawnSettings();

                            // Boost group size for specific passive mobs.
                            // Must remove + re-add since entries are immutable.

                            spawnSettings.removeSpawnsOfEntityType(EntityType.COW);
                            spawnSettings.addSpawn(MobCategory.CREATURE,
                                    new MobSpawnSettings.SpawnerData(EntityType.COW, 2, 5), 1);

                            spawnSettings.removeSpawnsOfEntityType(EntityType.SHEEP);
                            spawnSettings.addSpawn(MobCategory.CREATURE,
                                    new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 2, 5), 1);

                            spawnSettings.removeSpawnsOfEntityType(EntityType.CHICKEN);
                            spawnSettings.addSpawn(MobCategory.CREATURE,
                                    new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 2, 5), 1);

                            spawnSettings.removeSpawnsOfEntityType(EntityType.PIG);
                            spawnSettings.addSpawn(MobCategory.CREATURE,
                                    new MobSpawnSettings.SpawnerData(EntityType.PIG, 2, 5), 1);
                        });
    }
}
