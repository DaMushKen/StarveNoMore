package net.damushken.starve_no_more.datagen;

import net.damushken.starve_no_more.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EntityTypeIds;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.cow.Cow;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {
    public ModEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {

        builder(ModTags.EntityTypes.CAN_PLUMP)

                .add(EntityTypeIds.COW)
                .add(EntityTypeIds.GOAT)
                .add(EntityTypeIds.MOOSHROOM)
                .add(EntityTypeIds.SHEEP)
                .add(EntityTypeIds.PIG)
                .add(EntityTypeIds.CHICKEN)
                .add(EntityTypeIds.RABBIT)
                .add(EntityTypeIds.HOGLIN);



        builder(ModTags.EntityTypes.CAN_SPAWN_FROM_BONEMEAL)
                .add(EntityTypeIds.PIG)
                .add(EntityTypeIds.SHEEP)
                .add(EntityTypeIds.CHICKEN)
                .add(EntityTypeIds.COW)
                .add(EntityTypeIds.RABBIT);

    }
}
