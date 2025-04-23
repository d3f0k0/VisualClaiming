package com.alchemy.visualclaiming.database;

import com.feed_the_beast.ftbutilities.gui.ClientClaimedChunks;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

public class VCDimensionCache {
    private final Object2ObjectMap<ChunkPos, FTBChunkClaimPosition> chunks = new Object2ObjectOpenHashMap<>();

    public void addChunkData(ChunkPos pos, ClientClaimedChunks.ChunkData chunkData) {
        chunks.put(pos, new FTBChunkClaimPosition(pos, chunkData));
    }

    public List<FTBChunkClaimPosition> getChunkClaimInArea(ChunkPos topLeft, ChunkPos bottomRight) {
        List<FTBChunkClaimPosition> result = new ArrayList<>();
        for (int i = topLeft.x; i <= bottomRight.x; i++) {
            for (int j = topLeft.z; j <= bottomRight.z; j++) {
                ChunkPos pos = new ChunkPos(i, j);
                if (chunks.containsKey(pos)) {
                    result.add(chunks.get(pos));
                }
            }
        }
        return result;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound result = new NBTTagCompound();
        for (ChunkPos key : chunks.keySet()) {
            result.setTag(key.x + "," + key.z, chunks.get(key).toNBT());
        }
        return result;
    }

    public void fromNBT(NBTTagCompound nbt) {
        for (String nbtKey : nbt.getKeySet()) {
            String[] splitpos = nbtKey.split(",");
            ChunkPos key = new ChunkPos(Integer.parseInt(splitpos[0]), Integer.parseInt(splitpos[1]));
            NBTTagCompound chunkNBT = nbt.getCompoundTag(nbtKey);
            chunks.put(key, new FTBChunkClaimPosition(key,
                        new ClientClaimedChunks.ChunkData(
                                new ClientClaimedChunks.Team(chunkNBT.getShort("team")),
                                chunkNBT.getInteger("flag"))
            ));
        }
    }
}
