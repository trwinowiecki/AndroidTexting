package com.spongycastle.openpgp.operator;

import com.spongycastle.bcpg.PublicKeyPacket;
import com.spongycastle.openpgp.PGPException;

public interface KeyFingerPrintCalculator
{
    byte[] calculateFingerprint(PublicKeyPacket publicPk)
        throws PGPException;
}
