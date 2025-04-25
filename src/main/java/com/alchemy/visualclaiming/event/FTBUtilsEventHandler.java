package com.alchemy.visualclaiming.event;

import com.alchemy.visualclaiming.database.VCClientCache;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.misc.ChunkSelectorMap;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftbutilities.events.chunks.UpdateClientDataEvent;
import com.feed_the_beast.ftbutilities.gui.ClientClaimedChunks;
import com.feed_the_beast.ftbutilities.net.MessageClaimedChunksRequest;
import com.feed_the_beast.ftbutilities.net.MessageClaimedChunksUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Map;
@Mod.EventBusSubscriber()
@SuppressWarnings("unused")
public class FTBUtilsEventHandler {
    private static ChunkPos lastPosition;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.world.isRemote || !(event.player instanceof EntityPlayerMP mp)) return;
        new MessageClaimedChunksRequest(mp).sendToServer();
    }

    @SubscribeEvent
    public static void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        if (event.getEntityPlayer().world.isRemote || !(event.getEntityPlayer() instanceof EntityPlayerMP mp)) return;
        new MessageClaimedChunksRequest(mp).sendToServer();
    }

    @SubscribeEvent
    public static void onEnteringChunk(EntityEvent.EnteringChunk event)
    {
        if (event.getEntity() != Minecraft.getMinecraft().player)
        {
            return;
        }

        if (lastPosition == null || MathUtils.dist(event.getNewChunkX(), event.getNewChunkZ(), lastPosition.x, lastPosition.z) >= 3D)
        {
            lastPosition = new ChunkPos(event.getNewChunkX(), event.getNewChunkZ());
            new MessageClaimedChunksRequest(Minecraft.getMinecraft().player).sendToServer();
        }
    }


    // Base on FTB Utilities JourneyMap integration, adapt for VisualOres Layer
    @SubscribeEvent
    public static void onDataReceived(UpdateClientDataEvent event) {
        MessageClaimedChunksUpdate message = event.getMessage();
        int dim = ClientUtils.getDim();
        for (ClientClaimedChunks.Team team : message.teams.values()) {
            for (Map.Entry<Integer, ClientClaimedChunks.ChunkData> entry : team.chunks.entrySet())
            {
                int x = entry.getKey() % ChunkSelectorMap.TILES_GUI;
                int z = entry.getKey() / ChunkSelectorMap.TILES_GUI;
                ClientClaimedChunks.ChunkData chunkData = entry.getValue();
                VCClientCache.instance.addChunkData(dim, new ChunkPos(message.startX + x, message.startZ + z),
                            chunkData.team.uid,
                            chunkData.flags,
                            chunkData.team.color.getColor().hashCode(),
                            chunkData.team.nameComponent.getFormattedText());
            }
        }

    }
}
