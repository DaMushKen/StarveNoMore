package net.damushken.starve_no_more.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger("starvenomore-config");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // CONSTANTS
    public static final transient int CAP_MIN = 5;
    public static final transient int CAP_MAX = 100;
    public static final transient int CREATURE_DEFAULT = 45;
    public static final transient int AXOLOTLS_DEFAULT = 10;
    public static final transient int WATER_CREATURE_DEFAULT = 20;
    public static final transient int WATER_AMBIENT_DEFAULT = 35;

    public static final transient boolean SPAWN_BABY_ON_BONEMEAL_DEFAULT = true;

    public static final transient float BONEMEAL_CHANCE_MIN = 0.05f;
    public static final transient float BONEMEAL_CHANCE_MAX = 1.0f;
    public static final transient float SPAWN_BABY_ON_BONEMEAL_CHANCE_DEFAULT = 0.15f;

    public static final transient float CHUNK_CHANCE_MIN = 0.2f;
    public static final transient float CHUNK_CHANCE_MAX = 0.5f;
    public static final transient float ON_CHUNK_SPAWN_CHANCE_DEFAULT = 0.3f;

    public static final transient boolean DAWN_BREEDABLE_OFFSPRINGS_DEFAULT = true;
    public static final transient float DAWN_CHANCE_MIN = 0.05f;
    public static final transient float DAWN_CHANCE_MAX = 1.0f;
    public static final transient float DAWN_BREEDABLE_OFFSPRINGS_CHANCE_DEFAULT = 0.20f;

    public static final transient int DAWN_MAX_MIN = 2;
    public static final transient int DAWN_MAX_MAX = 5;
    public static final transient int DAWN_BREEDABLE_MAX_OFFSPRINGS_DEFAULT = 3;

    public static final transient int DAWN_BREEDABLE_MAX_TICKS_MIN = 2400; //2 min
    public static final transient int DAWN_BREEDABLE_MAX_TICKS_MAX = 12000; //10 min
    public static final transient int DAWN_BREEDABLE_MAX_TICKS_DEFAULT = 4800; //4 min

    public static final transient boolean DO_ON_HAYBALE_FASTER_BABY_GROWTH_DEFAULT = true;
    public static final transient int HAYBALE_MULTIPLIER_MIN = 10;
    public static final transient int HAYBALE_MULTIPLIER_MAX = 100;
    public static final transient int ON_HAYBALE_FASTER_BABY_GROWTH_MULTIPLIER_DEFAULT = 30; // stored as percent

    public static final transient boolean DO_PLUMP_DEFAULT = true;
    public static final transient float PLUMP_SCALE_MIN = 1.25f;
    public static final transient float PLUMP_SCALE_MAX = 2.0f;
    public static final transient float PLUMP_SCALE_DEFAULT = 1.25f;

    public static final transient float PLUMP_DROPS_MULTIPLIER_MIN = 1.25f;
    public static final transient float PLUMP_DROPS_MULTIPLIER_MAX = 2.0f;
    public static final transient float PLUMP_DROPS_MULTIPLIER_DEFAULT = 1.5f;

    public static final transient int PLUMP_DAYS_MIN = 1;
    public static final transient int PLUMP_DAYS_MAX = 5;
    public static final transient int PLUMP_DAYS_DEFAULT = 2;

    public static final transient boolean DO_GOATS_DROP_AND_PLUMP_DEFAULT = true;

    public static final transient boolean DO_WILD_PLUMP_DEFAULT = false;



    // PERSISTED VALUES
    public int creatureMaxCapacity = CREATURE_DEFAULT;
    public int axolotlsMaxCapacity = AXOLOTLS_DEFAULT;
    public int waterCreatureMaxCapacity = WATER_CREATURE_DEFAULT;
    public int waterAmbientMaxCapacity = WATER_AMBIENT_DEFAULT;

    public boolean spawnBabyOnBonemeal = SPAWN_BABY_ON_BONEMEAL_DEFAULT;
    public float spawnBabyOnBonemealChance = SPAWN_BABY_ON_BONEMEAL_CHANCE_DEFAULT;

    public float onChunkSpawnChance = ON_CHUNK_SPAWN_CHANCE_DEFAULT;

    public boolean dawnBreedableOffsprings = DAWN_BREEDABLE_OFFSPRINGS_DEFAULT;
    public float dawnBreedableOffspringsChance = DAWN_BREEDABLE_OFFSPRINGS_CHANCE_DEFAULT;
    public int dawnBreedableMaxOffsprings = DAWN_BREEDABLE_MAX_OFFSPRINGS_DEFAULT;
    public int dawnBreedableMaxTicks = DAWN_BREEDABLE_MAX_TICKS_DEFAULT;

    public boolean doOnHaybaleFasterBabyGrowth = DO_ON_HAYBALE_FASTER_BABY_GROWTH_DEFAULT;
    public int onHaybaleFasterBabyGrowthMultiplier = ON_HAYBALE_FASTER_BABY_GROWTH_MULTIPLIER_DEFAULT;

    public boolean doPlump = DO_PLUMP_DEFAULT;
    public float plumpScale = PLUMP_SCALE_DEFAULT;
    public float plumpDropsMultiplier = PLUMP_DROPS_MULTIPLIER_DEFAULT;
    public int plumpDays = PLUMP_DAYS_DEFAULT;

    public boolean doGoatsDropAndPlump = DO_GOATS_DROP_AND_PLUMP_DEFAULT;

    public boolean doWildPlump = DO_WILD_PLUMP_DEFAULT;



    // PER WORLD SINGLETON
    private static ModConfig instance;
    private static Path activeConfigPath;



    public static ModConfig get() {
        if (instance == null) {
            LOGGER.warn("ModConfig.get() called before config was loaded." +
                    "Using temporary defaults. This should not happen; report if seen repeatedly.");
            return new ModConfig();
        }
        return instance;
    }

    /** Called from ServerLifecycleEvents.SERVER_STARTED */
    public static void loadForServer(MinecraftServer server) {
        Path worldRoot = server.getSavePath(WorldSavePath.ROOT);
        activeConfigPath = worldRoot.resolve("starvenomore.json");

        if (Files.exists(activeConfigPath)) {
            try (Reader reader = Files.newBufferedReader(activeConfigPath)) {
                ModConfig loaded = GSON.fromJson(reader, ModConfig.class);
                instance = (loaded != null) ? loaded : new ModConfig();
                LOGGER.info("Loaded per-world config from {}", activeConfigPath);
            } catch (IOException e) {
                LOGGER.error("Failed to read world config, using defaults", e);
                instance = new ModConfig();
            }
        } else {
            instance = new ModConfig();
            instance.save();
            LOGGER.info("Created new per-world config at {}", activeConfigPath);
        }
    }

    /** Called from ServerLifecycleEvents.SERVER_STOPPING, to release state between worlds */
    public static void unload() {
        instance = null;
        activeConfigPath = null;
    }

    public void save() {
        if (activeConfigPath == null) {
            LOGGER.warn("save() called with no active world config path, skipping");
            return;
        }
        try {
            Files.createDirectories(activeConfigPath.getParent());
            try (Writer writer = Files.newBufferedWriter(activeConfigPath)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save world config", e);
        }
    }
}
