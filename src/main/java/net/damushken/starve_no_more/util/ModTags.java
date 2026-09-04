package net.damushken.starve_no_more.util;

import net.damushken.starve_no_more.StarveNoMore;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {



    public static class Blocks {

        private static TagKey<Block> createBlockTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(StarveNoMore.MOD_ID, name));
        }
    }



    public static class Items {

        private static TagKey<Item> createItemTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(StarveNoMore.MOD_ID, name));
        }
    }



    public static class EntityTypes {

        public static final TagKey<EntityType<?>> CAN_PLUMP =
                createEntityTypeTag("can_plump");

        private static TagKey<EntityType<?>> createEntityTypeTag(String name) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(StarveNoMore.MOD_ID, name));
        }

    }


}
