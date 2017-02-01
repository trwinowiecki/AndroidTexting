package com.spongycastle.openpgp.operator;

import com.spongycastle.openpgp.PGPException;

public interface PublicKeyDataDecryptorFactory
    extends PGPDataDecryptorFactory
{
    public byte[] recoverSessionData(int keyAlgorithm, byte[][] secKeyData)
            throws PGPException;
}
