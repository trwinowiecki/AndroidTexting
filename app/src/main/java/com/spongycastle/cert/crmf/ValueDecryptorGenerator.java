package com.spongycastle.cert.crmf;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.operator.InputDecryptor;

public interface ValueDecryptorGenerator
{
    InputDecryptor getValueDecryptor(AlgorithmIdentifier keyAlg, AlgorithmIdentifier symmAlg, byte[] encKey)
        throws CRMFException;
}
