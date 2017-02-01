package com.spongycastle.operator;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;

public interface DigestCalculatorProvider
{
    DigestCalculator get(AlgorithmIdentifier digestAlgorithmIdentifier)
        throws OperatorCreationException;
}
