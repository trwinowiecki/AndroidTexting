package com.spongycastle.openpgp.operator;

import com.spongycastle.openpgp.PGPException;
import com.spongycastle.openpgp.PGPPublicKey;

public interface PGPContentVerifierBuilder
{
    public PGPContentVerifier build(final PGPPublicKey publicKey)
        throws PGPException;
}
