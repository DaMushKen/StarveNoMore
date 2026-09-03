package net.damushken.starve_no_more.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
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
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("starvenomore.json");

    // CONSTANTS
    public static final transient int CAP_MIN = 5;
    public static final transient int CAP_MAX = 50;
    public static final transient int CREATURE_DEFAULT = 40;
    public static final transient int AXOLOTLS_DEFAULT = 10;
    public static final transient int WATER_CREATURE_DEFAULT = 20;
    public static final transient int WATER_AMBIENT_DEFAULT = 35;

    public static final transient boolean SPAWN_BABY_ON_BONEMEAL_DEFAULT = true;

    public static final transient float BONEMEAL_CHANCE_MIN = 0.10f;
    public static final transient float BONEMEAL_CHANCE_MAX = 1.0f;
    public static final transient float SPAWN_BABY_ON_BONEMEAL_CHANCE_DEFAULT = 0.15f;

    public static final transient float CHUNK_CHANCE_MIN = 0.2f;
    public static final transient float CHUNK_CHANCE_MAX = 0.5f;
    public static final transient float ON_CHUNK_SPAWN_CHANCE_DEFAULT = 0.3f;

    public static final transient boolean DAWN_BREEDABLE_OFFSPRINGS_DEFAULT = true;
    public static final transient float DAWN_CHANCE_MIN = 0.10f;
    public static final transient float DAWN_CHANCE_MAX = 1.0f;
    public static final transient float DAWN_BREEDABLE_OFFSPRINGS_CHANCE_DEFAULT = 0.20f;

    public static final transient int DAWN_MAX_MIN = 2;
    public static final transient int DAWN_MAX_MAX = 5;
    public static final transient int DAWN_BREEDABLE_MAX_OFFSPRINGS_DEFAULT = 3;

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

    // SINGLETON INSTANCE
    private static ModConfig instance;

    public static ModConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static ModConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                ModConfig loaded = GSON.fromJson(reader, ModConfig.class);
                if (loaded != null) {
                    LOGGER.info("Loaded config from {}", CONFIG_PATH);
                    return loaded;
                }
            } catch (IOException e) {
                LOGGER.error("Failed to read config, using defaults", e);
            }
        }
        ModConfig fresh = new ModConfig();
        fresh.save();
        return fresh;
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }
}
