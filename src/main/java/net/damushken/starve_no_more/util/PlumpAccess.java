package net.damushken.starve_no_more.util;

public interface PlumpAccess {
    boolean starvenomore$isPlump();
    void starvenomore$setPlump(boolean plump);
    void starvenomore$resetBreedTimer();

    boolean starvenomore$isPlayerLineage();
    void starvenomore$setPlayerLineage(boolean value);

    boolean starvenomore$isEffectivelyPlumpSynced();
    void starvenomore$setEffectivelyPlumpSynced(boolean value);

    float starvenomore$getSyncedScale();
    void starvenomore$setSyncedScale(float scale);
}
