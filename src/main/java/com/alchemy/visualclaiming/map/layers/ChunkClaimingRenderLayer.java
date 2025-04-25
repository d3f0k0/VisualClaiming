package com.alchemy.visualclaiming.map.layers;

import com.alchemy.visualclaiming.database.FTBChunkClaimPosition;
import com.alchemy.visualclaiming.database.VCClientCache;
import hellfall.visualores.map.DrawUtils;
import hellfall.visualores.map.layers.RenderLayer;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

public class ChunkClaimingRenderLayer extends RenderLayer {
    List<FTBChunkClaimPosition> visibleChunks = new ArrayList<>();
    private FTBChunkClaimPosition hoveredChunk;
    public ChunkClaimingRenderLayer(String key) {
        super(key);
    }

    @Override
    public void render(double cameraX, double cameraZ, double scale) {
        for (FTBChunkClaimPosition chunk : visibleChunks) {
            int teamColor = (chunk.teamColor & 0x00FFFFFF) + 0x77000000;
            int borderColor;
            if (chunk.flags == 1) {
                borderColor = 0xDDFF0000;
            } else {
                borderColor = teamColor;
            }
            DrawUtils.drawOverlayBox(chunk.x, chunk.z, borderColor, teamColor);
        }
    }

    @Override
    public void updateVisibleArea(int dimensionID, int[] visibleBounds) {
        visibleChunks = VCClientCache.instance.getChunkClaimsInArea(dimensionID, visibleBounds);
    }

    @Override
    public void updateHovered(double mouseX, double mouseY, double cameraX, double cameraZ, double scale) {
        ChunkPos mousePos = new ChunkPos(DrawUtils.getMouseBlockPos(mouseX, mouseY, cameraX, cameraZ, scale));
        hoveredChunk = null;
        for (FTBChunkClaimPosition chunkClaimPosition : visibleChunks) {
            if (chunkClaimPosition.x == mousePos.x && chunkClaimPosition.z == mousePos.z) {
                hoveredChunk = chunkClaimPosition;
                break;
            }
        }
    }

    @Override
    public List<String> getTooltip() {
        if (hoveredChunk != null) {
            return hoveredChunk.tooltips;
        }
        return null;
    }

}
