package net.damushken.starve_no_more.datagen;

import net.damushken.starve_no_more.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {
    public ModEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {

        valueLookupBuilder(ModTags.EntityTypes.CAN_PLUMP)

                .add(EntityType.COW)
                .add(EntityType.GOAT)
                .add(EntityType.MOOSHROOM)
                .add(EntityType.SHEEP)
                .add(EntityType.PIG)
                .add(EntityType.CHICKEN)
                .add(EntityType.RABBIT)
                .add(EntityType.HOGLIN);



        valueLookupBuilder(ModTags.EntityTypes.CAN_SPAWN_FROM_BONEMEAL)
                .add(EntityType.PIG)
                .add(EntityType.SHEEP)
                .add(EntityType.CHICKEN)
                .add(EntityType.COW)
                .add(EntityType.RABBIT);

    }
}
