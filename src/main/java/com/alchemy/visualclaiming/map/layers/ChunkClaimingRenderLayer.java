package com.alchemy.visualclaiming.map.layers;

import com.alchemy.visualclaiming.database.FTBChunkClaimPosition;
import com.alchemy.visualclaiming.database.VCClientCache;
import hellfall.visualores.map.DrawUtils;
import hellfall.visualores.map.layers.RenderLayer;

import java.util.ArrayList;
import java.util.List;

public class ChunkClaimingRenderLayer extends RenderLayer {
    List<FTBChunkClaimPosition> visibleChunks = new ArrayList<>();
    public ChunkClaimingRenderLayer(String key) {
        super(key);
    }

    @Override
    public void render(double cameraX, double cameraZ, double scale) {
        for (FTBChunkClaimPosition chunk : visibleChunks) {
            int teamColor = (chunk.teamColor & 0x00FFFFFF) + 0x77000000;
            DrawUtils.drawOverlayBox(chunk.x, chunk.z, teamColor, teamColor);
        }
    }

    @Override
    public void updateVisibleArea(int dimensionID, int[] visibleBounds) {
        visibleChunks = VCClientCache.instance.getChunkClaimsInArea(dimensionID, visibleBounds);
    }
}
