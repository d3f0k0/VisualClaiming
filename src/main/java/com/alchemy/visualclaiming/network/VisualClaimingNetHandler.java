package com.alchemy.visualclaiming.network;

import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;

public class VisualClaimingNetHandler {
    static final NetworkWrapper VC_CLAIMS = NetworkWrapper.newWrapper("vc_claims");

    public static void init() {
        VC_CLAIMS.register(new MessageVisualClaimingRequest());
    }
}
