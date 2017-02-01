package com.spongycastle.operator;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;

public interface KeyUnwrapper
{
    AlgorithmIdentifier getAlgorithmIdentifier();

    GenericKey generateUnwrappedKey(AlgorithmIdentifier encryptionKeyAlgorithm, byte[] encryptedKey)
        throws OperatorException;
}
