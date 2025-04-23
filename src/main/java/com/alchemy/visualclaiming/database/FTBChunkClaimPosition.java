package com.alchemy.visualclaiming.database;

import com.feed_the_beast.ftbutilities.gui.ClientClaimedChunks;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

public class FTBChunkClaimPosition {
    public int x;
    public int z;

    public ClientClaimedChunks.ChunkData chunkData;
    public int teamColor;
    public List<String> tooltips;

    public FTBChunkClaimPosition(ChunkPos pos, ClientClaimedChunks.ChunkData chunkData) {
        this.x = pos.x;
        this.z = pos.z;
        this.chunkData = chunkData;
        this.teamColor = chunkData.team.color.getColor().rgb();

        tooltips = new ArrayList<>();
        tooltips.add(I18n.format("visualclaiming.team", chunkData.team.nameComponent));
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound result = new NBTTagCompound();
        result.setShort("team", chunkData.team.uid);
        result.setInteger("flags", chunkData.flags);
        return result;
    }
}
