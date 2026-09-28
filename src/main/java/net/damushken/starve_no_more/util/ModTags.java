package net.damushken.starve_no_more.util;

import net.damushken.starve_no_more.StarveNoMore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

import java.util.Optional;

public class ModTags {



    public static class Blocks {

        private static TagKey<Block> createBlockTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(StarveNoMore.MOD_ID, name));
        }
    }



    public static class Items {

        private static TagKey<Item> createItemTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(StarveNoMore.MOD_ID, name));
        }
    }



    public static class EntityTypes {



        public static final TagKey<EntityType<?>> CAN_PLUMP =
                createEntityTypeTag("can_plump");

        public static final TagKey<EntityType<?>> CAN_SPAWN_FROM_BONEMEAL =
                createEntityTypeTag("can_spawn_from_bonemeal");



        private static TagKey<EntityType<?>> createEntityTypeTag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(StarveNoMore.MOD_ID, name));
        }

    }


}
