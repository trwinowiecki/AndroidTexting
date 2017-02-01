package com.spongycastle.openpgp.operator;

import com.spongycastle.openpgp.PGPException;
import com.spongycastle.openpgp.PGPPrivateKey;

public interface PGPContentSignerBuilder
{
    public PGPContentSigner build(final int signatureType, final PGPPrivateKey privateKey)
        throws PGPException;
}
