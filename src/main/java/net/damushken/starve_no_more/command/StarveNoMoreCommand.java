package net.damushken.starve_no_more.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.damushken.starve_no_more.util.MobCapManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.function.*;

public class StarveNoMoreCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess) {

        LiteralArgumentBuilder<ServerCommandSource> root = CommandManager.literal("starvenomore")
                .requires(source -> source.hasPermissionLevel(2));

        root.then(intGamerule("set_creature_max_capacity",
                ModConfig.CAP_MIN, ModConfig.CAP_MAX, ModConfig.CREATURE_DEFAULT,
                cfg -> cfg.creatureMaxCapacity, (cfg, v) -> cfg.creatureMaxCapacity = v,
                MobCapManager::applyAll));

        root.then(intGamerule("set_axolotls_max_capacity",
                ModConfig.CAP_MIN, ModConfig.CAP_MAX, ModConfig.AXOLOTLS_DEFAULT,
                cfg -> cfg.axolotlsMaxCapacity, (cfg, v) -> cfg.axolotlsMaxCapacity = v,
                MobCapManager::applyAll));

        root.then(intGamerule("set_water_creature_max_capacity",
                ModConfig.CAP_MIN, ModConfig.CAP_MAX, ModConfig.WATER_CREATURE_DEFAULT,
                cfg -> cfg.waterCreatureMaxCapacity, (cfg, v) -> cfg.waterCreatureMaxCapacity = v,
                MobCapManager::applyAll));

        root.then(intGamerule("set_water_ambient_max_capacity",
                ModConfig.CAP_MIN, ModConfig.CAP_MAX, ModConfig.WATER_AMBIENT_DEFAULT,
                cfg -> cfg.waterAmbientMaxCapacity, (cfg, v) -> cfg.waterAmbientMaxCapacity = v,
                MobCapManager::applyAll));

        root.then(boolGamerule("do_spawn_baby_on_bonemeal",
                ModConfig.SPAWN_BABY_ON_BONEMEAL_DEFAULT,
                cfg -> cfg.spawnBabyOnBonemeal, (cfg, v) -> cfg.spawnBabyOnBonemeal = v));

        root.then(floatGamerule("set_spawn_baby_on_bonemeal_chance",
                ModConfig.BONEMEAL_CHANCE_MIN, ModConfig.BONEMEAL_CHANCE_MAX,
                ModConfig.SPAWN_BABY_ON_BONEMEAL_CHANCE_DEFAULT,
                cfg -> cfg.spawnBabyOnBonemealChance, (cfg, v) -> cfg.spawnBabyOnBonemealChance = v));

        root.then(floatGamerule("set_on_chunk_spawn_chance",
                ModConfig.CHUNK_CHANCE_MIN, ModConfig.CHUNK_CHANCE_MAX,
                ModConfig.ON_CHUNK_SPAWN_CHANCE_DEFAULT,
                cfg -> cfg.onChunkSpawnChance, (cfg, v) -> cfg.onChunkSpawnChance = v));

        root.then(boolGamerule("do_dawn_breedable_offsprings",
                ModConfig.DAWN_BREEDABLE_OFFSPRINGS_DEFAULT,
                cfg -> cfg.dawnBreedableOffsprings, (cfg, v) -> cfg.dawnBreedableOffsprings = v));

        root.then(floatGamerule("set_dawn_breedable_offsprings_chance",
                ModConfig.DAWN_CHANCE_MIN, ModConfig.DAWN_CHANCE_MAX,
                ModConfig.DAWN_BREEDABLE_OFFSPRINGS_CHANCE_DEFAULT,
                cfg -> cfg.dawnBreedableOffspringsChance, (cfg, v) -> cfg.dawnBreedableOffspringsChance = v));

        root.then(intGamerule("set_dawn_breedable_max_offsprings",
                ModConfig.DAWN_MAX_MIN, ModConfig.DAWN_MAX_MAX,
                ModConfig.DAWN_BREEDABLE_MAX_OFFSPRINGS_DEFAULT,
                cfg -> cfg.dawnBreedableMaxOffsprings, (cfg, v) -> cfg.dawnBreedableMaxOffsprings = v,
                () -> {}));

        root.then(boolGamerule("do_on_haybale_faster_baby_growth",
                ModConfig.DO_ON_HAYBALE_FASTER_BABY_GROWTH_DEFAULT,
                cfg -> cfg.doOnHaybaleFasterBabyGrowth, (cfg, v) -> cfg.doOnHaybaleFasterBabyGrowth = v));

        root.then(intGamerule("set_on_haybale_faster_baby_growth_multiplier",
                ModConfig.HAYBALE_MULTIPLIER_MIN, ModConfig.HAYBALE_MULTIPLIER_MAX,
                ModConfig.ON_HAYBALE_FASTER_BABY_GROWTH_MULTIPLIER_DEFAULT,
                cfg -> cfg.onHaybaleFasterBabyGrowthMultiplier, (cfg, v) -> cfg.onHaybaleFasterBabyGrowthMultiplier = v,
                () -> {}));

        root.then(boolGamerule("do_plump",
                ModConfig.DO_PLUMP_DEFAULT,
                cfg -> cfg.doPlump, (cfg, v) -> cfg.doPlump = v));

        root.then(floatGamerule("set_plump_scale",
                ModConfig.PLUMP_SCALE_MIN, ModConfig.PLUMP_SCALE_MAX, ModConfig.PLUMP_SCALE_DEFAULT,
                cfg -> cfg.plumpScale, (cfg, v) -> cfg.plumpScale = v));

        root.then(floatGamerule("set_plump_drops_multiplier",
                ModConfig.PLUMP_DROPS_MULTIPLIER_MIN, ModConfig.PLUMP_DROPS_MULTIPLIER_MAX, ModConfig.PLUMP_DROPS_MULTIPLIER_DEFAULT,
                cfg -> cfg.plumpDropsMultiplier, (cfg, v) -> cfg.plumpDropsMultiplier = v));

        root.then(intGamerule("set_plump_days",
                ModConfig.PLUMP_DAYS_MIN, ModConfig.PLUMP_DAYS_MAX, ModConfig.PLUMP_DAYS_DEFAULT,
                cfg -> cfg.plumpDays, (cfg, v) -> cfg.plumpDays = v,
                () -> {}));

        root.then(boolGamerule("do_goats_drop_and_plump",
                ModConfig.DO_GOATS_DROP_AND_PLUMP_DEFAULT,
                cfg -> cfg.doGoatsDropAndPlump, (cfg, v) -> cfg.doGoatsDropAndPlump = v));

        dispatcher.register(root);
    }

    // HELPERS

    private static LiteralArgumentBuilder<ServerCommandSource> intGamerule(
            String name, int min, int max, int def,
            Function<ModConfig, Integer> getter, BiConsumer<ModConfig, Integer> setter,
            Runnable onChange) {

        return CommandManager.literal(name)
                .executes(ctx -> {
                    ModConfig cfg = ModConfig.get();
                    infoMessage(ctx.getSource(), name, String.valueOf(getter.apply(cfg)), String.valueOf(def));
                    return 1;
                })
                .then(CommandManager.literal("reset")
                        .executes(ctx -> {
                            ModConfig cfg = ModConfig.get();
                            setter.accept(cfg, def);
                            cfg.save();
                            onChange.run();
                            resetMessage(ctx.getSource(), name, String.valueOf(def));
                            return 1;
                        }))
                .then(CommandManager.argument("value", IntegerArgumentType.integer(min, max))
                        .executes(ctx -> {
                            int value = IntegerArgumentType.getInteger(ctx, "value");
                            ModConfig cfg = ModConfig.get();
                            setter.accept(cfg, value);
                            cfg.save();
                            onChange.run();
                            setMessage(ctx.getSource(), name, String.valueOf(value));
                            return 1;
                        }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> floatGamerule(
            String name, float min, float max, float def,
            Function<ModConfig, Float> getter, BiConsumer<ModConfig, Float> setter) {

        return CommandManager.literal(name)
                .executes(ctx -> {
                    ModConfig cfg = ModConfig.get();
                    infoMessage(ctx.getSource(), name, String.valueOf(getter.apply(cfg)), String.valueOf(def));
                    return 1;
                })
                .then(CommandManager.literal("reset")
                        .executes(ctx -> {
                            ModConfig cfg = ModConfig.get();
                            setter.accept(cfg, def);
                            cfg.save();
                            resetMessage(ctx.getSource(), name, String.valueOf(def));
                            return 1;
                        }))
                .then(CommandManager.argument("value", FloatArgumentType.floatArg(min, max))
                        .executes(ctx -> {
                            float value = FloatArgumentType.getFloat(ctx, "value");
                            ModConfig cfg = ModConfig.get();
                            setter.accept(cfg, value);
                            cfg.save();
                            setMessage(ctx.getSource(), name, String.valueOf(value));
                            return 1;
                        }));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> boolGamerule(
            String name, boolean def,
            Function<ModConfig, Boolean> getter, BiConsumer<ModConfig, Boolean> setter) {

        return CommandManager.literal(name)
                .executes(ctx -> {
                    ModConfig cfg = ModConfig.get();
                    infoMessage(ctx.getSource(), name, String.valueOf(getter.apply(cfg)), String.valueOf(def));
                    return 1;
                })
                .then(CommandManager.literal("reset")
                        .executes(ctx -> {
                            ModConfig cfg = ModConfig.get();
                            setter.accept(cfg, def);
                            cfg.save();
                            resetMessage(ctx.getSource(), name, String.valueOf(def));
                            return 1;
                        }))
                .then(CommandManager.argument("value", BoolArgumentType.bool())
                        .executes(ctx -> {
                            boolean value = BoolArgumentType.getBool(ctx, "value");
                            ModConfig cfg = ModConfig.get();
                            setter.accept(cfg, value);
                            cfg.save();
                            setMessage(ctx.getSource(), name, String.valueOf(value));
                            return 1;
                        }));
    }

    private static void infoMessage(ServerCommandSource source, String name, String current, String def) {
        source.sendFeedback(() -> Text.literal(
                "§7[StarveNoMore] §f" + name + " = §a" + current + " §7(default: " + def + ")"), false);
    }

    private static void setMessage(ServerCommandSource source, String name, String value) {
        source.sendFeedback(() -> Text.literal(
                "§7[StarveNoMore] §f" + name + " set to §a" + value), true);
    }

    private static void resetMessage(ServerCommandSource source, String name, String def) {
        source.sendFeedback(() -> Text.literal(
                "§7[StarveNoMore] §f" + name + " reset to default (§a" + def + "§f)"), true);
    }
}