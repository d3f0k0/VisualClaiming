package com.alchemy.visualclaiming.network;

import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

public class MessageVisualClaimingUpdate extends MessageToClient {
    private int minX, minZ, maxX, maxZ;

    public MessageVisualClaimingUpdate() {};

    public MessageVisualClaimingUpdate(int minX, int maxX, int minZ, int maxZ, EntityPlayer player) {

    }

    @Override
    public @NotNull NetworkWrapper getWrapper() {
        return VisualClaimingNetHandler.VC_CLAIMS;
    }
}
