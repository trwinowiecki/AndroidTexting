package com.spongycastle.crypto.engines;

public class AESWrapPadEngine
    extends RFC5649WrapEngine
{
    public AESWrapPadEngine()
    {
        super(new AESEngine());
    }
}
