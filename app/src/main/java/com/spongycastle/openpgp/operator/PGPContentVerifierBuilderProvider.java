package com.spongycastle.openpgp.operator;

import com.spongycastle.openpgp.PGPException;

public interface PGPContentVerifierBuilderProvider
{
    public PGPContentVerifierBuilder get(int keyAlgorithm, int hashAlgorithm)
        throws PGPException;
}
