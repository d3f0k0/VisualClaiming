package com.alchemy.visualclaiming.network;

import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbutilities.data.ClaimedChunk;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import net.minecraft.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;


public class MessageVisualClaimingRequest extends MessageToServer {
    private int minX, minZ, maxX, maxZ;

    public MessageVisualClaimingRequest() {}

    public MessageVisualClaimingRequest(int minX, int maxX, int minZ, int maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    @Override
    public void writeData(DataOut data) {
        data.writeVarInt(minX);
        data.writeVarInt(maxX);
        data.writeVarInt(minZ);
        data.writeVarInt(maxZ);
    }

    @Override
    public void readData(DataIn data) {
        minX = data.readVarInt();
        minZ = data.readVarInt();
        maxX = data.readVarInt();
        maxZ = data.readVarInt();
    }

    @Override
    public @NotNull NetworkWrapper getWrapper() {
        return VisualClaimingNetHandler.VC_CLAIMS;
    }

    @Override
    public void onMessage(EntityPlayerMP player) {
        if (ClaimedChunks.isActive()) {
            new MessageVisualClaimingUpdate(minX, maxX, minZ, maxZ, player).sendTo(player);
        }
    }
}
