package com.alchemy.visualclaiming.database;

import com.feed_the_beast.ftbutilities.gui.ClientClaimedChunks;
import hellfall.visualores.database.IClientCachePerDimOnly;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class VCClientCache implements IClientCachePerDimOnly {
    public static final VCClientCache instance = new VCClientCache();
    private final Int2ObjectMap<VCDimensionCache> cache = new Int2ObjectArrayMap<>();
    @Override
    public void setupCacheFiles() {
        addDimFiles("visualclaiming_");
    }

    public void addChunkData(int dim, ChunkPos pos, ClientClaimedChunks.ChunkData chunkData) {
        if (!cache.containsKey(dim)) {
            cache.put(dim, new VCDimensionCache());
        }
        cache.get(dim).addChunkData(pos, chunkData);
    }

    public void addChunkData(int dim, ChunkPos pos, short uid, int flags, int teamColor, String teamName) {
        if (!cache.containsKey(dim)) {
            cache.put(dim, new VCDimensionCache());
        }
        cache.get(dim).addChunkData(pos, uid, flags, teamColor, teamName);
    }

    public List<FTBChunkClaimPosition> getChunkClaimsInArea(int dim, int[] bounds) {
        if (cache.containsKey(dim)) {
            return cache.get(dim).getChunkClaimInArea(
                    new ChunkPos(bounds[0] >> 4, bounds[1] >> 4),
                    new ChunkPos((bounds[0] + bounds[2]) >> 4, (bounds[1] + bounds[3]) >> 4));
        }
        return new ArrayList<>();
    };

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public Collection<Integer> getExistingDimensions(String s) {
        return cache.keySet();
    }

    @Override
    public NBTTagCompound saveDimFile(String prefix, int dim) {
        if (cache.containsKey(dim)) {
            return cache.get(dim).toNBT();
        }
        return null;
    }

    @Override
    public void readDimFile(String prefix, int dim, NBTTagCompound data) {
        if (!cache.containsKey(dim)) {
            cache.put(dim, new VCDimensionCache());
        }
        cache.get(dim).fromNBT(data);
    }
}