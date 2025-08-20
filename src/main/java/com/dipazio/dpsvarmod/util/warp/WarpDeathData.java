package com.dipazio.dpsvarmod.util.warp;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarpDeathData extends SavedData {
    private final Map<UUID, CompoundTag> deathPlayersList = new HashMap<>();
    private static final String DATA_NAME = "death_location_data";

    public WarpDeathData() {
        this.setDirty();
    }

    @Override
    public CompoundTag save(@NotNull CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        
        for (var entry : deathPlayersList.entrySet()) {
            CompoundTag data = new CompoundTag();

            data.putUUID("uuid", entry.getKey());
            data.put("pos", entry.getValue());

            list.add(data);
        }
        
        tag.put("warp_death_players", list);
        return tag;
    }

    public static WarpDeathData load(CompoundTag tag) {
        WarpDeathData deathData = new WarpDeathData();
        ListTag list = tag.getList("warp_death_players", CompoundTag.TAG_COMPOUND);

        for (int i = 0; i < list.size(); i++) {
            CompoundTag data = list.getCompound(i);

            UUID uuid = data.getUUID("uuid");
            CompoundTag posTag = data.getCompound("pos");

            deathData.deathPlayersList.put(uuid, posTag);
        }

        return deathData;
    }

    public static WarpDeathData get() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return new WarpDeathData();
        } else {
            ServerLevel level = server.getLevel(Level.OVERWORLD);
            return level == null ? new WarpDeathData() : level.getDataStorage().computeIfAbsent(
                    new SavedData.Factory<>(
                            WarpDeathData::new,
                            (compoundTag, provider) -> load(compoundTag)),
                    ResourceLocation.fromNamespaceAndPath(DPsVarietyMod.MODID, DATA_NAME).toDebugFileName()
            );
        }
    }

    public void setDeathLocation(UUID playerUUID, ResourceKey<Level> dimension, Player player) {
        CompoundTag tag = new CompoundTag();

        tag.putDouble("tp_X", player.getX());
        tag.putDouble("tp_Y", player.getY());
        tag.putDouble("tp_Z", player.getZ());
        tag.putString("dimension", dimension.location().toString());

        deathPlayersList.put(playerUUID, tag);
        setDirty();
    }

    public CompoundTag getDeathLocation(UUID playerUUID) {
        return deathPlayersList.get(playerUUID);
    }

    @Nullable
    public CompoundTag useAndRemove(UUID playerUUID) {
        CompoundTag tag = deathPlayersList.remove(playerUUID);
        if (tag != null) setDirty();
        return tag;
    }
}