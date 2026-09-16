package net.damushken.starve_no_more.util;

import net.damushken.starve_no_more.StarveNoMore;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import java.util.Optional;

public class ModTags {



    public static class Blocks {

        private static TagKey<Block> createBlockTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(StarveNoMore.MOD_ID, name));
        }
    }



    public static class Items {

        private static TagKey<Item> createItemTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(StarveNoMore.MOD_ID, name));
        }
    }



    public static class EntityTypes {



        public static final TagKey<EntityType<?>> CAN_PLUMP =
                createEntityTypeTag("can_plump");

        public static final TagKey<EntityType<?>> CAN_SPAWN_FROM_BONEMEAL =
                createEntityTypeTag("can_spawn_from_bonemeal");



        private static TagKey<EntityType<?>> createEntityTypeTag(String name) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(StarveNoMore.MOD_ID, name));
        }

    }


}
